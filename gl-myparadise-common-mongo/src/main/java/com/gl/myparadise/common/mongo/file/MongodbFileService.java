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
        executor.execute(() -> save(mainTraceId, fileName, method, doNext, args, (data) -> defaultExportCallBacker(id, fileName).accept(data), locale));
    }

    @SneakyThrows
    private <T, R> void save(
            String mainTraceId,
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
        GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(id)));
        if (Objects.isNull(gridFSFile)) {
            return null;
        }
        GridFSDownloadStream downloadStream = gridFSBucket.openDownloadStream(Objects.requireNonNull(gridFSFile).getObjectId());
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
        if (id.contains(".")) {
            id = id.split("\\.")[0];
        }
        GridFsResource gridFsResource = getById(id);
        if (Objects.isNull(gridFsResource)) {
            return;
        }
        try (InputStream inputStream = gridFsResource.getInputStream(); OutputStream outputStream = response.getOutputStream()) {
            response.setContentType(gridFsResource.getContentType());
            String fileName = gridFsResource.getFilename();
            String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString()).replace("+", "%20");
            response.setHeader("content-disposition", "attachment; filename*=" + encodedFileName);
            IOUtils.copy(inputStream, outputStream);
            outputStream.flush();
        } catch (Exception ex) {
            log.error("loadFile", ex);
            return;
        }
    }
}
