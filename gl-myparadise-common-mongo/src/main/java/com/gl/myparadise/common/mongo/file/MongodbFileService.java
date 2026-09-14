package com.gl.myparadise.common.mongo.file;

import com.gl.myparadise.common.mongo.fn.PageResult;
import com.gl.myparadise.common.mongo.utils.ExportUtils;
import com.gl.myparadise.common.mongo.utils.RemainingTimeUtils;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.GridFSUploadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.client.gridfs.model.GridFSUploadOptions;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Nullable;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Function;

@Service
@Slf4j
public class MongodbFileService {
    final Integer limit = 100000000;
    @Autowired
    private GridFsTemplate gridFsTemplate;
    @Autowired
    private GridFSBucket gridFSBucket;
    @Autowired
    private ThreadPoolTaskExecutor executor;
    @Autowired
    private GlExportService glExportService;
    @Autowired
    private MessageSource messageSource;

    @PostConstruct
    public void confirm() {
        log.info("MongodbFileService init");
    }

    public <T, R> void saveAsync(
            @NonNull Long userId,
            @NonNull String rawFileName,
            @NonNull Function<R, PageResult<T>> method,
            @NonNull Function<R, R> doNext,
            @NonNull R args
    ) {
        saveAsync(userId, rawFileName, method, doNext, args, null);
    }

    /**
     * @param exportClass 导出行 DTO 的 class；传入后查询结果为空时仍会输出带完整表头的空文件
     */
    public <T, R> void saveAsync(
            @NonNull Long userId,
            @NonNull String rawFileName,
            @NonNull Function<R, PageResult<T>> method,
            @NonNull Function<R, R> doNext,
            @NonNull R args,
            @Nullable Class<? extends T> exportClass
    ) {
        Locale locale = LocaleContextHolder.getLocale();
        String fileName = StringUtils.trimToEmpty(messageSource.getMessage(rawFileName, null, rawFileName, locale));
        String id = glExportService.startExport(userId, fileName);
        String mainTraceId = MDC.get("traceId") == null ? UUID.randomUUID().toString() : MDC.get("traceId");
        log.info("开始异步导出 {}", mainTraceId);
        executor.execute(() -> save(mainTraceId, userId, id, fileName, method, doNext, args, (data) -> defaultExportCallBacker(id, fileName).accept(data), locale, exportClass, null));
    }

    /**
     * 分片导出：每满 rowsPerFile 行关闭当前 CSV 并另开一个，每片各自一条下载中心记录（title 带 _partN）。
     * 分页游标由 method/doNext 自行维护（如 ES search_after），本方法只负责切檔。
     */
    public <T, R> void saveAsyncSharded(
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
        // 首片先不带 _part1；只有真的切出第 2 片时才把首片改名（见 save 里的切檔分支）
        String id = glExportService.startExport(userId, fileName);
        String mainTraceId = MDC.get("traceId") == null ? UUID.randomUUID().toString() : MDC.get("traceId");
        log.info("开始异步分片导出 {} rowsPerFile={}", mainTraceId, rowsPerFile);
        // 大导出可能跑数十分钟，不进共用 asyncExecutor：它是 CallerRunsPolicy，塞满时会把整个导出跑在 Tomcat 请求线程上
        shardedExportExecutor.execute(() -> {
            try {
                save(mainTraceId, userId, id, fileName, method, doNext, args, (data) -> defaultExportCallBacker(id, fileName).accept(data), locale, exportClass, rowsPerFile);
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

    // ponytail: 固定 2 线程 + 无界队列，多余的大导出排队而非拒绝；量大再改成可配置
    private final ExecutorService shardedExportExecutor = Executors.newFixedThreadPool(2, runnable -> {
        Thread thread = new Thread(runnable, "shardedExport-" + SHARDED_THREAD_SEQ.incrementAndGet());
        thread.setDaemon(true);
        return thread;
    });
    private static final AtomicInteger SHARDED_THREAD_SEQ = new AtomicInteger();

    private static String partFileName(String fileName, int part) {
        return fileName + "_part" + part;
    }

    /**
     * @param rowsPerFile null = 单文件（原行为）；>0 = 每满该行数切一个新 CSV + 新下载中心记录
     */
    @SneakyThrows
    private <T, R> void save(
            String mainTraceId,
            @NonNull Long userId,
            @Nullable String exportId,
            @NonNull String fileName,
            @NonNull Function<R, PageResult<T>> method,
            @NonNull Function<R, R> doNext,
            @NonNull R args,
            @Nullable Consumer<RemainingTimeUtils.ProcessVO> callBacker,
            Locale locale,
            @Nullable Class<? extends T> exportClass,
            @Nullable Integer rowsPerFile
    ) {
        MDC.put("traceId", mainTraceId);
        log.info("开始异步导出 save {}", mainTraceId);
        GridFSUploadStream gridFSUploadStream = null;
        try {
            boolean sharded = rowsPerFile != null;
            int part = 1;
            String currentTitle = fileName;
            gridFSUploadStream = gridFSBucket.openUploadStream(currentTitle + ".csv", computeUploadOptionsFor());
            String objectId = gridFSUploadStream.getObjectId().toString();
            long startTime = System.nanoTime();
            int totalDownloadElementCount = 0;
            int rowsInCurrentFile = 0;
            invokeNotify(startTime, 100, totalDownloadElementCount, objectId, callBacker, false);
            PageResult<T> currentPage;

            while (true) {
                log.info("开始异步导出 save2 {}", mainTraceId);
                currentPage = method.apply(args);
                if (isEmpty(currentPage)) {
                    log.info("导出结束:无数据");
                    break;
                }
                //安全限制不要拿掉
                if (totalDownloadElementCount > limit) {
                    log.info("导出结束:最大限制");
                    break;
                }
                if (currentPage.getTotal() <= totalDownloadElementCount) {
                    log.info("导出结束:到達total");
                    break;
                }
                List<T> rows = currentPage.getResult();
                int offset = 0;
                while (offset < rows.size()) {
                    // 分片模式：还有行要写而当前片已满 → 先收掉当前片、开下一片。只在有行可写时切，不会产生空尾片
                    if (sharded && rowsInCurrentFile >= rowsPerFile) {
                        gridFSUploadStream.flush();
                        gridFSUploadStream.close();
                        if (part == 1) {
                            // 确定有第 2 片了，首片补上 _part1（GridFS 文件名要 close 后才存在，才能 rename）
                            currentTitle = partFileName(fileName, 1);
                            gridFSBucket.rename(new ObjectId(objectId), currentTitle + ".csv");
                        }
                        glExportService.completeExport(exportId, objectId, currentTitle);
                        log.info("分片导出完成 part={} rows={} total={}", part, rowsInCurrentFile, totalDownloadElementCount);
                        // 已完成的片不该再被 catch 里的 failExport 改成失败；新片建好前先清掉
                        exportId = null;

                        part++;
                        currentTitle = partFileName(fileName, part);
                        exportId = glExportService.startExport(userId, currentTitle);
                        gridFSUploadStream = gridFSBucket.openUploadStream(currentTitle + ".csv", computeUploadOptionsFor());
                        objectId = gridFSUploadStream.getObjectId().toString();
                        rowsInCurrentFile = 0;
                        // 后续分片改由各自的 exportId 更新进度/完成
                        final String partExportId = exportId;
                        final String partTitle = currentTitle;
                        callBacker = (data) -> defaultExportCallBacker(partExportId, partTitle).accept(data);
                    }
                    // 单文件模式一次写完；分片模式只写到本片剩余容量
                    int chunkEnd = sharded ? Math.min(rows.size(), offset + rowsPerFile - rowsInCurrentFile) : rows.size();
                    write(gridFSUploadStream, rows.subList(offset, chunkEnd), rowsInCurrentFile == 0, locale);
                    rowsInCurrentFile += chunkEnd - offset;
                    totalDownloadElementCount += chunkEnd - offset;
                    offset = chunkEnd;
                }

                invokeNotify(startTime, currentPage.getTotal(), totalDownloadElementCount, objectId, callBacker, false);
                args = doNext.apply(args);
            }
            // 查询结果为空时仍输出完整表头，避免下载到全空文件
            if (totalDownloadElementCount == 0 && exportClass != null) {
                byte[] headerData = ExportUtils.getHeaderData(exportClass, messageSource, locale);
                if (headerData != null) {
                    gridFSUploadStream.write(headerData);
                }
            }
            invokeNotify(startTime, currentPage == null ? 0 : currentPage.getTotal(), totalDownloadElementCount, objectId, callBacker, true);

        } catch (Exception ex) {
            log.error("异步导出失败", ex);
            glExportService.failExport(exportId);
        } finally {
            try {
                if (gridFSUploadStream != null) {
                    gridFSUploadStream.flush();
                    gridFSUploadStream.close();
                }
            } catch (Exception ex) {
                log.error("gridFSUploadStream", ex);
            }
            MDC.remove("traceId");
        }
    }

    private <T> boolean isEmpty(PageResult<T> currentPage) {
        return Objects.isNull(currentPage) || CollectionUtils.isEmpty(currentPage.getResult());
    }

    private <T> void write(GridFSUploadStream gridFSUploadStream, List<T> tmp, boolean isNewDoc, Locale locale) {
        gridFSUploadStream.write(ExportUtils.getExportData(tmp, isNewDoc, messageSource, locale));
    }

    private GridFSUploadOptions computeUploadOptionsFor() {
        Document targetMetadata = new Document();
        targetMetadata.put("_contentType", "application/csv");
        GridFSUploadOptions options = new GridFSUploadOptions();
        options.metadata(targetMetadata);
        return options;
    }

    public void invokeNotify(long startTime, long totalProcessMetrics, long doneProcessMetrics, String objectId, Consumer<RemainingTimeUtils.ProcessVO> callBacker, boolean isDone) {
        RemainingTimeUtils.ProcessVO processVO = RemainingTimeUtils.getRemainingTimeInfo(startTime, System.nanoTime(), totalProcessMetrics, doneProcessMetrics, objectId, isDone);

        if (Objects.nonNull(callBacker)) {
            callBacker.accept(processVO);
        }

        log.info("导出任务:任务完成剩下时间:{} 总数:{} 本次完成数:{}", processVO.getRemainingTime(), totalProcessMetrics, doneProcessMetrics);
    }

    public Consumer<RemainingTimeUtils.ProcessVO> defaultExportCallBacker(String id, String fileName) {
        return (data) -> {
            if (data.isDone()) {
                glExportService.completeExport(id, data.getObjectId(), fileName);
            } else {
                String format = String.format("%s_(%.0f%%)", fileName, BigDecimal.valueOf(100).multiply(data.getProcess()));
                glExportService.updateTitle(id, format, Objects.toString(data.getRemainingTime()));
            }
        };
    }

    private GridFsResource getById(String id) {
        // MongoDB 查询文件元数据
        long mongoQueryStart = System.currentTimeMillis();
        GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(id)));
        long mongoQueryTime = System.currentTimeMillis() - mongoQueryStart;
        log.info("MongoDB 查询文件元数据完成 - id: {}, 耗时: {}ms", id, mongoQueryTime);

        if (Objects.isNull(gridFSFile)) {
            log.warn("MongoDB 未找到文件 - id: {}, 查询耗时: {}ms", id, mongoQueryTime);
            return null;
        }

        // MongoDB 打开下载流
        long mongoStreamStart = System.currentTimeMillis();
        GridFSDownloadStream downloadStream = gridFSBucket.openDownloadStream(Objects.requireNonNull(gridFSFile).getObjectId());
        long mongoStreamTime = System.currentTimeMillis() - mongoStreamStart;
        log.info("MongoDB 打开下载流完成 - id: {}, 耗时: {}ms, 文件大小: {} bytes",
                id, mongoStreamTime, gridFSFile.getLength());

        return new GridFsResource(gridFSFile, downloadStream);
    }

    public void download(String id, HttpServletResponse response) {
        GridFsResource gridFsResource = getById(id);
        if (Objects.isNull(gridFsResource)) {
            return;
        }
        try {
            String filename = gridFsResource.getFilename();
            log.info("文件名称是{}", filename);
            response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            log.error("download", e);
            return;
        }

        try (InputStream inputStream = gridFsResource.getInputStream(); OutputStream outputStream = response.getOutputStream()) {
            response.setContentType(gridFsResource.getContentType());
            IOUtils.copy(inputStream, outputStream);
            outputStream.flush();
        } catch (Exception ex) {
            log.error("download", ex);
            return;
        }
    }

    @SneakyThrows
    public String uploadFile(MultipartFile file) {
        String contentType = file.getContentType();
        ObjectId objectId = gridFsTemplate.store(
                file.getInputStream(),
                UUID.randomUUID().toString(),
                contentType
        );
        return Optional.ofNullable(contentType)
                .map(data -> data.split("/"))
                .map(data -> data[1])
                .map(StringUtils::trimToNull)
                .map("."::concat)
                .map(objectId.toHexString()::concat)
                .orElse(objectId.toHexString());
    }

    public void loadFile(String id, HttpServletResponse response) {
        long startTime = System.currentTimeMillis();
        log.info("loadFile 开始, id: {}", id);

        if (id.contains(".")) {
            id = id.split("\\.")[0];
        }

        long queryStartTime = System.currentTimeMillis();
        GridFsResource gridFsResource = getById(id);
        long queryTime = System.currentTimeMillis() - queryStartTime;

        if (Objects.isNull(gridFsResource)) {
            log.warn("loadFile 文件不存在, id: {}, 查询耗时: {}ms", id, queryTime);
            return;
        }

        try {
            String fileName = gridFsResource.getFilename();
            String contentType = gridFsResource.getContentType();
            long contentLength = gridFsResource.contentLength();

            log.info("loadFile 文件信息 - id: {}, 文件名: {}, 文件类型: {}, 文件大小: {} bytes ({} MB), 查询耗时: {}ms",
                    id, fileName, contentType, contentLength,
                    contentLength > 0 ? String.format("%.2f", contentLength / 1024.0 / 1024.0) : "未知",
                    queryTime);

            long headerStartTime = System.currentTimeMillis();
            response.setContentType(contentType);
            String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString()).replace("+", "%20");
            response.setHeader("content-disposition", "attachment; filename*=" + encodedFileName);
            long headerTime = System.currentTimeMillis() - headerStartTime;
            log.info("loadFile 设置响应头完成, id: {}, 耗时: {}ms", id, headerTime);

            // 获取输入流（可能触发 MongoDB 首次读取）
            long getInputStreamStart = System.currentTimeMillis();
            InputStream inputStream = null;
            OutputStream outputStream = null;
            try {
                inputStream = gridFsResource.getInputStream();
                long getInputStreamTime = System.currentTimeMillis() - getInputStreamStart;
                log.info("MongoDB 获取输入流完成 - id: {}, 耗时: {}ms", id, getInputStreamTime);

                // 获取输出流
                long getOutputStreamStart = System.currentTimeMillis();
                outputStream = response.getOutputStream();
                long getOutputStreamTime = System.currentTimeMillis() - getOutputStreamStart;
                log.info("获取响应输出流完成 - id: {}, 耗时: {}ms", id, getOutputStreamTime);

                // 记录第一次读取的耗时（MongoDB 首次读取通常最慢）
                long firstReadStart = System.currentTimeMillis();
                byte[] firstBuffer = new byte[8192];
                int firstBytesRead = inputStream.read(firstBuffer);
                long firstReadTime = System.currentTimeMillis() - firstReadStart;
                log.info("MongoDB 首次读取数据 - id: {}, 读取字节数: {} bytes, 耗时: {}ms",
                        id, firstBytesRead > 0 ? firstBytesRead : 0, firstReadTime);

                long transferStartTime = System.currentTimeMillis();
                long totalBytesTransferred = 0;

                // 如果第一次读取有数据，先写入
                if (firstBytesRead > 0) {
                    outputStream.write(firstBuffer, 0, firstBytesRead);
                    totalBytesTransferred += firstBytesRead;
                }

                // 继续读取剩余数据
                byte[] buffer = new byte[16384]; // 16KB 缓冲区
                int bytesRead;
                int readCount = 1; // 第一次已经读取了
                long slowReadCount = 0; // 记录慢读取次数（>100ms）

                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    long readStart = System.currentTimeMillis();
                    outputStream.write(buffer, 0, bytesRead);
                    long readTime = System.currentTimeMillis() - readStart;
                    totalBytesTransferred += bytesRead;
                    readCount++;

                    // 记录慢读取
                    if (readTime > 100) {
                        slowReadCount++;
                        log.warn("MongoDB 慢读取检测 - id: {}, 第{}次读取, 字节数: {} bytes, 耗时: {}ms",
                                id, readCount, bytesRead, readTime);
                    }
                }

                outputStream.flush();
                long transferTime = System.currentTimeMillis() - transferStartTime;
                long totalTime = System.currentTimeMillis() - startTime;

                log.info("loadFile 传输完成 - id: {}, 总传输字节数: {} bytes, 读取次数: {}, 慢读取次数: {}, 传输耗时: {}ms, 总耗时: {}ms, 平均速度: {} KB/s",
                        id, totalBytesTransferred, readCount, slowReadCount, transferTime, totalTime,
                        transferTime > 0 ? String.format("%.2f", totalBytesTransferred / 1024.0 / (transferTime / 1000.0)) : "0");
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (Exception e) {
                        log.error("关闭输入流异常", e);
                    }
                }
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (Exception e) {
                        log.error("关闭输出流异常", e);
                    }
                }
            }
        } catch (Exception ex) {
            long totalTime = System.currentTimeMillis() - startTime;
            log.error("loadFile 异常, id: {}, 总耗时: {}ms", id, totalTime, ex);
            return;
        }
    }
}
