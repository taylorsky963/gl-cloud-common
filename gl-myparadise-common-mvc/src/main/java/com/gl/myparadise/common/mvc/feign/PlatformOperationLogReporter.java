package com.gl.myparadise.common.mvc.feign;

import com.gl.myparadise.common.bean.report.operationlog.PlatformOperationLogReportDto;

/**
 * 操作日誌上報器抽象。實作由 {@link PlatformOperationLogReporterImpl} 提供 (異步 Feign)。
 */
public interface PlatformOperationLogReporter {

    /**
     * 非同步上報一筆操作日誌。
     *
     * <p>呼叫端不阻塞 - 即使上報失敗也不會影響業務請求。</p>
     */
    void report(PlatformOperationLogReportDto dto);
}
