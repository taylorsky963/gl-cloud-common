package com.gl.myparadise.common.bean.report.operationlog;

import lombok.Data;

import java.io.Serializable;

/**
 * 切面 → auth-server 上報用 DTO。
 * <p>結構與 {@code platformOperationLog} 表大致一致。</p>
 * <p>放在 common-baseBean 是為了讓 auth-server (寫入端) 與 common-mvc (上報切面端)
 * 都能引用,而避免 auth-server 必須依賴 common-mvc。</p>
 */
@Data
public class PlatformOperationLogReportDto implements Serializable {

    private static final long serialVersionUID = 8943218275410023781L;

    private Long opUserInfoId;

    private String opUserName;

    private String menuId;

    private String menuName;

    private String serviceName;

    private String requestMethod;

    private String requestUrl;

    private String requestParams;

    private String responseSummary;

    private String rsCode;

    private String rsMessages;

    private String clientIp;

    private String userAgent;

    private String traceId;

    private Integer durationMs;

    private String exceptionMsg;

    /**
     * 實際操作時間 (毫秒). server 端會轉為 Timestamp.
     */
    private Long operationTime;
}
