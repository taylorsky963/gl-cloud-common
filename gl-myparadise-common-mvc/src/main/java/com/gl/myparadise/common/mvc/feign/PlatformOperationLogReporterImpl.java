package com.gl.myparadise.common.mvc.feign;

import com.gl.myparadise.common.bean.report.operationlog.PlatformOperationLogReportDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 上報器實作 - 使用 @Async 線程池 (operationLogExecutor) 包住 Feign 呼叫,
 * 業務請求不會被阻塞。Feign 失敗只記 warn,不向上拋出。
 */
@Slf4j
@Component
public class PlatformOperationLogReporterImpl implements PlatformOperationLogReporter {

    /**
     * 使用 required=false: 若該服務未啟用 Feign 或沒網路到 bgauth-service,
     * 也不應影響業務啟動。
     */
    @Autowired(required = false)
    private BgAuthOperationLogReportAPI bgAuthOperationLogReportAPI;

    @Async("operationLogExecutor")
    @Override
    public void report(PlatformOperationLogReportDto dto) {
        if (bgAuthOperationLogReportAPI == null) {
            log.debug("PlatformOperationLogReporterImpl - feign client unavailable, skip url={}", dto.getRequestUrl());
            return;
        }
        try {
            bgAuthOperationLogReportAPI.reportOperation(dto);
        } catch (Exception e) {
            log.warn("PlatformOperationLogReporterImpl.report feign call failed, url={}", dto.getRequestUrl(), e);
        }
    }
}
