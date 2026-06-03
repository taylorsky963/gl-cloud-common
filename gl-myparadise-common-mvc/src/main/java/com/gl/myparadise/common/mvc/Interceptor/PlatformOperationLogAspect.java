package com.gl.myparadise.common.mvc.Interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gl.myparadise.common.bean.model.ServiceRsModel;
import com.gl.myparadise.common.bean.report.operationlog.PlatformOperationLogReportDto;
import com.gl.myparadise.common.mvc.feign.PlatformOperationLogReporter;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 後台操作日誌切面 (寫 MySQL 版本)。
 * <p>與舊的 {@link SystemOperationLogAspect} (寫 ES) 並存:</p>
 * <ul>
 *     <li>舊切面: 必須 menuId+uid 雙存在,寫到 ES</li>
 *     <li>本切面: 只要 uid 存在即記錄,經由 Feign 非同步寫到 MySQL platformOperationLog 表</li>
 * </ul>
 * <p>排除機制:</p>
 * <ol>
 *     <li>方法上有 {@link com.gl.myparadise.common.mvc.annotation.IgnoreOperationLog} 註解</li>
 *     <li>URL 命中內建黑名單 (heartbeat / actuator / get-logo-url / /log/ 等)</li>
 *     <li>URL 命中 DB 動態忽略規則 ({@link PlatformOperationLogIgnoreRuleCache}, 由
 *         {@code platformOperationLogIgnoreRule} 表維護, 帶 TTL 本地快照)</li>
 *     <li>無 uid header (未認證請求)</li>
 * </ol>
 */
@Aspect
@Component
@Slf4j
public class PlatformOperationLogAspect {

    /**
     * 敏感欄位 key,值在序列化參數時會被遮罩為 ***。
     */
    private static final List<String> SENSITIVE_KEYS = Arrays.asList(
            "password", "loginpass", "loginPass", "loginPassword", "oldpass", "oldPass",
            "oldPassword", "newpass", "newPass", "newPassword", "googleauthkey", "googleAuthKey",
            "mobileCode", "smsCode", "verifyCode", "captcha"
    );

    /**
     * URL 黑名單 (前綴或子字串命中即跳過)。
     */
    private static final List<String> IGNORED_URL_KEYWORDS = Arrays.asList(
            "/log/report-operation",
            "/log/query-list-where",
            "/actuator",
            "/heartbeat",
            "get-logo-url",
            "/swagger",
            "/v2/api-docs",
            "favicon.ico"
    );

    private static final int MAX_PARAM_LEN = 8000;
    private static final int MAX_RESULT_LEN = 8000;
    private static final int MAX_EXCEPTION_LEN = 2000;

    private static final Pattern SENSITIVE_PATTERN = buildSensitivePattern();

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PlatformOperationLogReporter platformOperationLogReporter;

    @Autowired(required = false)
    private PlatformOperationLogIgnoreRuleCache ignoreRuleCache;

    @Value("${spring.application.name:unknown}")
    private String serviceName;

    @Pointcut("execution(* com.gl.myparadise.background..controller..*(..))")
    public void backendPointCut() {
    }

    /**
     * 只攔截真正的 HTTP handler (帶任一 mapping 註解的 method),
     * 避免把 {@code @ModelAttribute} / {@code @InitBinder} / {@code @ExceptionHandler}
     * 等輔助方法也一起攔截 (Spring MVC 對每個請求都會先呼叫這些輔助方法,
     * 否則一個請求會被記錄成兩次以上)。
     */
    @Pointcut(
            "@annotation(org.springframework.web.bind.annotation.RequestMapping) || "
            + "@annotation(org.springframework.web.bind.annotation.GetMapping) || "
            + "@annotation(org.springframework.web.bind.annotation.PostMapping) || "
            + "@annotation(org.springframework.web.bind.annotation.PutMapping) || "
            + "@annotation(org.springframework.web.bind.annotation.DeleteMapping) || "
            + "@annotation(org.springframework.web.bind.annotation.PatchMapping)")
    public void mappingPointCut() {
    }

    @Pointcut("@annotation(com.gl.myparadise.common.mvc.annotation.IgnoreOperationLog)")
    public void ignorePointCut() {
    }

    @Pointcut("backendPointCut() && mappingPointCut() && !ignorePointCut()")
    public void pointCut() {
    }

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        ServletRequestAttributes attributes;
        try {
            attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        } catch (Exception e) {
            return pjp.proceed();
        }
        if (attributes == null) {
            return pjp.proceed();
        }
        HttpServletRequest request = attributes.getRequest();
        String requestUrl = request.getRequestURI();

        if (isUrlIgnored(requestUrl)) {
            return pjp.proceed();
        }

        if (ignoreRuleCache != null && ignoreRuleCache.shouldIgnore(request.getMethod(), requestUrl)) {
            return pjp.proceed();
        }

        String uid = request.getHeader("uid");
        if (StringUtils.isBlank(uid) || "null".equalsIgnoreCase(uid) || "undefined".equalsIgnoreCase(uid)) {
            return pjp.proceed();
        }

        long startTs = System.currentTimeMillis();
        Object result = null;
        Throwable thrown = null;
        try {
            result = pjp.proceed();
            return result;
        } catch (Throwable t) {
            thrown = t;
            throw t;
        } finally {
            try {
                long duration = System.currentTimeMillis() - startTs;
                PlatformOperationLogReportDto dto = buildDto(pjp, request, uid, requestUrl, result, thrown, duration, startTs);
                platformOperationLogReporter.report(dto);
            } catch (Exception ex) {
                log.warn("PlatformOperationLogAspect build/report failed, url={}", requestUrl, ex);
            }
        }
    }

    private PlatformOperationLogReportDto buildDto(ProceedingJoinPoint pjp,
                                                   HttpServletRequest request,
                                                   String uid,
                                                   String requestUrl,
                                                   Object result,
                                                   Throwable thrown,
                                                   long duration,
                                                   long startTs) {
        PlatformOperationLogReportDto dto = new PlatformOperationLogReportDto();
        dto.setServiceName(serviceName);
        dto.setRequestMethod(request.getMethod());
        dto.setRequestUrl(requestUrl);
        dto.setOperationTime(startTs);
        dto.setDurationMs((int) Math.min(duration, Integer.MAX_VALUE));

        dto.setClientIp(resolveClientIp(request));
        dto.setUserAgent(truncate(request.getHeader("User-Agent"), 500));
        dto.setTraceId(MDC.get("traceId"));

        try {
            dto.setOpUserInfoId(Long.parseLong(uid));
        } catch (NumberFormatException e) {
            dto.setOpUserInfoId(null);
        }

        String menuId = request.getHeader("menuId");
        if (StringUtils.isNotBlank(menuId) && !"null".equalsIgnoreCase(menuId) && !"undefined".equalsIgnoreCase(menuId)) {
            dto.setMenuId(menuId);
        }

        try {
            String[] paramNames = ((MethodSignature) pjp.getSignature()).getParameterNames();
            Object[] args = pjp.getArgs();
            dto.setRequestParams(truncate(serializeParams(paramNames, args), MAX_PARAM_LEN));
        } catch (Exception e) {
            log.debug("serialize params failed url={}", requestUrl, e);
        }

        if (result instanceof ServiceRsModel) {
            ServiceRsModel<?> rs = (ServiceRsModel<?>) result;
            dto.setRsCode(rs.getRsCode() == null ? null : String.valueOf(rs.getRsCode()));
            dto.setRsMessages(truncate(rs.getRsMessages(), 1000));
            try {
                HashMap<String, Object> summary = Maps.newHashMap();
                summary.put("rsCode", rs.getRsCode());
                summary.put("rsMessages", rs.getRsMessages());
                dto.setResponseSummary(truncate(objectMapper.writeValueAsString(summary), MAX_RESULT_LEN));
            } catch (Exception ignore) {
            }
        }

        if (thrown != null) {
            dto.setExceptionMsg(truncate(thrown.getClass().getName() + ": " + thrown.getMessage(), MAX_EXCEPTION_LEN));
        }

        return dto;
    }

    private String serializeParams(String[] paramNames, Object[] args) throws Exception {
        if (paramNames == null || args == null) {
            return null;
        }
        HashMap<String, Object> params = Maps.newHashMap();
        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];
            if (arg == null) {
                continue;
            }
            if (arg instanceof ServletRequest
                    || arg instanceof ServletResponse
                    || arg instanceof MultipartFile
                    || arg instanceof BindingResult) {
                continue;
            }
            params.put(paramNames[i], arg);
        }
        String json = objectMapper.writeValueAsString(params);
        return maskSensitive(json);
    }

    private static String maskSensitive(String json) {
        if (StringUtils.isBlank(json)) {
            return json;
        }
        return SENSITIVE_PATTERN.matcher(json).replaceAll("$1\"***\"");
    }

    private static Pattern buildSensitivePattern() {
        StringBuilder sb = new StringBuilder();
        sb.append("(\"(?:");
        for (int i = 0; i < SENSITIVE_KEYS.size(); i++) {
            if (i > 0) {
                sb.append("|");
            }
            sb.append(Pattern.quote(SENSITIVE_KEYS.get(i)));
        }
        sb.append(")\"\\s*:\\s*)\"[^\"]*\"");
        return Pattern.compile(sb.toString());
    }

    private static boolean isUrlIgnored(String url) {
        if (StringUtils.isBlank(url)) {
            return true;
        }
        for (String kw : IGNORED_URL_KEYWORDS) {
            if (url.contains(kw)) {
                return true;
            }
        }
        return false;
    }

    private static String resolveClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (StringUtils.isNotBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
            int idx = ip.indexOf(',');
            return truncate(idx > 0 ? ip.substring(0, idx).trim() : ip.trim(), 50);
        }
        ip = request.getHeader("X-Real-IP");
        if (StringUtils.isNotBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
            return truncate(ip.trim(), 50);
        }
        return truncate(request.getRemoteAddr(), 50);
    }

    private static String truncate(String s, int max) {
        if (s == null) {
            return null;
        }
        if (s.length() <= max) {
            return s;
        }
        return s.substring(0, max) + "...(truncated)";
    }
}
