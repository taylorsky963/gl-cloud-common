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
        Locale locale = LocaleContextHolder.getLocale();
        String fileName = StringUtils.trimToEmpty(messageSource.getMessage(rawFileName, null, rawFileName, locale));
        String id = glExportService.startExport(userId, fileName);
        String mainTraceId = MDC.get("traceId") == null ? UUID.randomUUID().toString() : MDC.get("traceId");
        log.info("开始异步导出 {}", mainTraceId);
        executor.execute(() -> save(mainTraceId, id, fileName, method, doNext, args, (data) -> defaultExportCallBacker(id, fileName).accept(data), locale));
    }

    @SneakyThrows
    private <T, R> void save(
            String mainTraceId,
            @Nullable String exportId,
            @NonNull String fileName,
            @NonNull Function<R, PageResult<T>> method,
            @NonNull Function<R, R> doNext,
            @NonNull R args,
            @Nullable Consumer<RemainingTimeUtils.ProcessVO> callBacker,
            Locale locale
    ) {
        MDC.put("traceId", mainTraceId);
        log.info("开始异步导出 save {}", mainTraceId);
        GridFSUploadStream gridFSUploadStream = null;
        try {
            gridFSUploadStream = gridFSBucket.openUploadStream(fileName + ".csv", computeUploadOptionsFor());
            String objectId = gridFSUploadStream.getObjectId().toString();
            long startTime = System.nanoTime();
            int totalDownloadElementCount = 0;
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
                write(gridFSUploadStream, currentPage.getResult(), totalDownloadElementCount == 0, locale);
                totalDownloadElementCount = totalDownloadElementCount + currentPage.getResult().size();

                invokeNotify(startTime, currentPage.getTotal(), totalDownloadElementCount, objectId, callBacker, false);
                args = doNext.apply(args);
            }
            invokeNotify(startTime, currentPage.getTotal(), totalDownloadElementCount, objectId, callBacker, true);

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
