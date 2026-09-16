package com.gl.myparadise.common.mongo.file;

import com.gl.myparadise.common.mongo.fn.PageResult;
import com.gl.myparadise.common.mongo.utils.ExportUtils;
import com.gl.myparadise.common.mongo.utils.RemainingTimeUtils;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSUploadStream;
import com.mongodb.client.gridfs.model.GridFSUploadOptions;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

/**
 * 分片 CSV 导出（FBMPLAY-1712）：每满 rowsPerFile 行关闭当前 GridFS 文件、另开一个，每片各自一条下载中心记录。
 * <p>
 * 与 {@link MongodbFileService#saveAsync} 刻意分开：那条路十几个服务在用，这里只服务大数据量导出，
 * 迴圈逻辑有重复是接受的代价。分页游标（如 ES search_after）由 method/doNext 自行维护，本类只负责切檔。
 */
@Service
@Slf4j
public class MongodbShardedExportService {
    /** 安全上限，与 MongodbFileService 一致 */
    private static final int LIMIT = 100000000;
    private static final AtomicInteger THREAD_SEQ = new AtomicInteger();

    @Autowired
    private GridFSBucket gridFSBucket;
    @Autowired
    private GlExportService glExportService;
    @Autowired
    private MessageSource messageSource;

    // ponytail: 固定 2 线程 + 无界队列，多余的大导出排队而非拒绝；量大再改成可配置。
    // 不进共用 asyncExecutor：它是 CallerRunsPolicy，塞满时会把整个导出跑在 Tomcat 请求线程上
    private final ExecutorService executor = Executors.newFixedThreadPool(2, runnable -> {
        Thread thread = new Thread(runnable, "shardedExport-" + THREAD_SEQ.incrementAndGet());
        thread.setDaemon(true);
        return thread;
    });

    /**
     * @param exportClass 导出行 DTO 的 class；查询结果为空时仍输出带完整表头的空文件
     * @param rowsPerFile 每片行数，>0
     * @param onFinish    导出结束（成功或失败）后回调，供呼叫方释放锁等
     */
    public <T, R> void saveAsync(
            @NonNull Long userId,
            @NonNull String rawFileName,
            @NonNull Function<R, PageResult<T>> method,
            @NonNull Function<R, R> doNext,
            @NonNull R args,
            @Nullable Class<? extends T> exportClass,
            int rowsPerFile,
            @Nullable Runnable onFinish
    ) {
        if (rowsPerFile <= 0) {
            throw new IllegalArgumentException("rowsPerFile must be positive: " + rowsPerFile);
        }
        Locale locale = LocaleContextHolder.getLocale();
        String fileName = StringUtils.trimToEmpty(messageSource.getMessage(rawFileName, null, rawFileName, locale));
        // 首片先不带 _part1；只有真的切出第 2 片时才把首片改名
        String id = glExportService.startExport(userId, fileName);
        String mainTraceId = MDC.get("traceId") == null ? UUID.randomUUID().toString() : MDC.get("traceId");
        log.info("开始异步分片导出 {} rowsPerFile={}", mainTraceId, rowsPerFile);
        executor.execute(() -> {
            try {
                save(mainTraceId, userId, id, fileName, method, doNext, args, locale, exportClass, rowsPerFile);
            } finally {
                if (onFinish != null) {
                    try {
                        onFinish.run();
                    } catch (Exception ex) {
                        log.error("分片导出 onFinish 失败", ex);
                    }
                }
            }
        });
    }

    private static String partFileName(String fileName, int part) {
        return fileName + "_part" + part;
    }

    private <T, R> void save(
            String mainTraceId,
            @NonNull Long userId,
            @Nullable String exportId,
            @NonNull String fileName,
            @NonNull Function<R, PageResult<T>> method,
            @NonNull Function<R, R> doNext,
            @NonNull R args,
            Locale locale,
            @Nullable Class<? extends T> exportClass,
            int rowsPerFile
    ) {
        MDC.put("traceId", mainTraceId);
        GridFSUploadStream stream = null;
        try {
            int part = 1;
            String currentTitle = fileName;
            stream = gridFSBucket.openUploadStream(currentTitle + ".csv", uploadOptions());
            String objectId = stream.getObjectId().toString();
            long startTime = System.nanoTime();
            int totalCount = 0;
            int rowsInCurrentFile = 0;
            notify(startTime, 100, totalCount, objectId, exportId, currentTitle, false);
            PageResult<T> currentPage;

            while (true) {
                currentPage = method.apply(args);
                if (isEmpty(currentPage)) {
                    log.info("分片导出结束:无数据");
                    break;
                }
                //安全限制不要拿掉
                if (totalCount > LIMIT) {
                    log.info("分片导出结束:最大限制");
                    break;
                }
                if (currentPage.getTotal() <= totalCount) {
                    log.info("分片导出结束:到達total");
                    break;
                }
                List<T> rows = currentPage.getResult();
                int offset = 0;
                while (offset < rows.size()) {
                    // 还有行要写而当前片已满 → 先收掉当前片、开下一片。只在有行可写时切，不会产生空尾片
                    if (rowsInCurrentFile >= rowsPerFile) {
                        stream.flush();
                        stream.close();
                        if (part == 1) {
                            // 确定有第 2 片了，首片补上 _part1（GridFS 文件记录要 close 后才存在，才能 rename）
                            currentTitle = partFileName(fileName, 1);
                            gridFSBucket.rename(new ObjectId(objectId), currentTitle + ".csv");
                        }
                        glExportService.completeExport(exportId, objectId, currentTitle);
                        log.info("分片导出完成 part={} rows={} total={}", part, rowsInCurrentFile, totalCount);
                        // 已完成的片不该再被 catch 里的 failExport 改成失败；新片建好前先清掉
                        exportId = null;

                        part++;
                        currentTitle = partFileName(fileName, part);
                        exportId = glExportService.startExport(userId, currentTitle);
                        stream = gridFSBucket.openUploadStream(currentTitle + ".csv", uploadOptions());
                        objectId = stream.getObjectId().toString();
                        rowsInCurrentFile = 0;
                    }
                    // 只写到本片剩余容量，跨界的那页拆两段写
                    int chunkEnd = Math.min(rows.size(), offset + rowsPerFile - rowsInCurrentFile);
                    stream.write(ExportUtils.getExportData(rows.subList(offset, chunkEnd), rowsInCurrentFile == 0, messageSource, locale));
                    rowsInCurrentFile += chunkEnd - offset;
                    totalCount += chunkEnd - offset;
                    offset = chunkEnd;
                }

                notify(startTime, currentPage.getTotal(), totalCount, objectId, exportId, currentTitle, false);
                args = doNext.apply(args);
            }
            // 查询结果为空时仍输出完整表头，避免下载到全空文件
            if (totalCount == 0 && exportClass != null) {
                byte[] headerData = ExportUtils.getHeaderData(exportClass, messageSource, locale);
                if (headerData != null) {
                    stream.write(headerData);
                }
            }
            notify(startTime, currentPage == null ? 0 : currentPage.getTotal(), totalCount, objectId, exportId, currentTitle, true);

        } catch (Exception ex) {
            log.error("分片导出失败", ex);
            glExportService.failExport(exportId);
        } finally {
            try {
                if (stream != null) {
                    stream.flush();
                    stream.close();
                }
            } catch (Exception ex) {
                log.error("gridFSUploadStream", ex);
            }
            MDC.remove("traceId");
        }
    }

    private <T> boolean isEmpty(PageResult<T> page) {
        return Objects.isNull(page) || CollectionUtils.isEmpty(page.getResult());
    }

    private GridFSUploadOptions uploadOptions() {
        Document metadata = new Document();
        metadata.put("_contentType", "application/csv");
        return new GridFSUploadOptions().metadata(metadata);
    }

    /** 进度回写到当前片的下载中心记录；isDone 时标记完成 */
    private void notify(long startTime, long total, long done, String objectId, String exportId, String title, boolean isDone) {
        RemainingTimeUtils.ProcessVO vo = RemainingTimeUtils.getRemainingTimeInfo(startTime, System.nanoTime(), total, done, objectId, isDone);
        if (vo.isDone()) {
            glExportService.completeExport(exportId, vo.getObjectId(), title);
        } else {
            String progressTitle = String.format("%s_(%.0f%%)", title, BigDecimal.valueOf(100).multiply(vo.getProcess()));
            glExportService.updateTitle(exportId, progressTitle, Objects.toString(vo.getRemainingTime()));
        }
        log.info("分片导出进度:剩余时间:{} 总数:{} 已完成:{}", vo.getRemainingTime(), total, done);
    }
}
