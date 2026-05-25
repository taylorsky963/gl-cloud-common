package com.gl.myparadise.common.bean.report.operationlog;

import lombok.Data;

import java.io.Serializable;

/**
 * 操作日誌忽略規則 Feign 傳輸用 DTO。
 * <p>由 {@code bgauth-service} 提供，{@code common-mvc} 的 cache 拉取後組成本地快照，
 * 命中即跳過該請求的操作日誌記錄。</p>
 */
@Data
public class PlatformOperationLogIgnoreRuleDto implements Serializable {

    private static final long serialVersionUID = -3492015488274190821L;

    /**
     * GET / POST / PUT / DELETE，null 或空字串 = 任何 method。
     */
    private String requestMethod;

    /**
     * AntPathMatcher 樣式，例如 {@code /bg/**}、{@code /actuator/**}。
     */
    private String urlPattern;

    /**
     * 是否啟用 (0/1)。
     */
    private Integer isEnable;
}
