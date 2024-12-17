package com.gl.myparadise.common.mongo.file;

import com.gl.myparadise.common.mongo.fn.Page;
import com.gl.myparadise.common.mongo.fn.InvokeMethod;
import com.gl.myparadise.common.mongo.utils.ExportUtils;
import com.gl.myparadise.common.mongo.utils.RemainingTimeUtils;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSUploadStream;
import com.mongodb.client.gridfs.model.GridFSUploadOptions;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.function.Function;

@Service
@Slf4j
public class MongodbFileService {


    final Integer limit = 100000;
    @Autowired
    private GridFSBucket gridFSBucket;
    @Autowired
    @Qualifier("taskExecutor")
    private Executor executor;
    @Autowired
    private GlExportService glExportService;

    public <T, R> void saveAsync(
            @NonNull Integer userId,
            @NonNull String fileName,
            @NonNull InvokeMethod<T> method,
            @NonNull Function<R, R> doNext,
            @NonNull R args
    ) {
        String id = glExportService.startExport(userId, fileName);
        executor.execute(() -> save(fileName, method, doNext, args, (data) -> defaultExportCallBacker(id, fileName).accept(data)));
    }

    @SneakyThrows
    private <T, R> void save(
            @NonNull String fileName,
            @NonNull InvokeMethod<T> method,
            @NonNull Function<R, R> doNext,
            @NonNull R args,
            @Nullable Consumer<RemainingTimeUtils.ProcessVO> callBacker
    ) {
        GridFSUploadStream gridFSUploadStream = gridFSBucket.openUploadStream(fileName + ".csv", computeUploadOptionsFor());
        String objectId = gridFSUploadStream.getObjectId().toString();
        long startTime = System.nanoTime();
        int totalDownloadElementCount = 0;
        invokeNotify(startTime, 100, totalDownloadElementCount, objectId, callBacker, false);
        Page<T> currentPage;

        while (true) {
            currentPage = method.invoke(args);
            if (isEmpty(currentPage)) {
                log.info("导出结束:无数据");
                break;
            }
            //安全限制不要拿掉
            if (totalDownloadElementCount > limit) {
                log.info("导出结束:最大限制");
                break;
            }
            write(gridFSUploadStream, currentPage.getResult(), totalDownloadElementCount == 0);
            totalDownloadElementCount = totalDownloadElementCount + currentPage.getResult().size();

            invokeNotify(startTime, currentPage.getTotal(), totalDownloadElementCount, objectId, callBacker, false);
            args = doNext.apply(args);
        }
        invokeNotify(startTime, currentPage.getTotal(), totalDownloadElementCount, objectId, callBacker, true);
        gridFSUploadStream.flush();
        gridFSUploadStream.close();
    }

    private <T> boolean isEmpty(Page<T> currentPage) {
        return Objects.isNull(currentPage) || CollectionUtils.isEmpty(currentPage.getResult());
    }

    private <T> void write(GridFSUploadStream gridFSUploadStream, List<T> tmp, boolean isNewDoc) {
        gridFSUploadStream.write(ExportUtils.getExportData(tmp, isNewDoc));
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
}
