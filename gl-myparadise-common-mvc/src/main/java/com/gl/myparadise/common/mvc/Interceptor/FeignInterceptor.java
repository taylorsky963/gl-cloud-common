package com.gl.myparadise.common.mvc.Interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.UUID;

public class FeignInterceptor implements RequestInterceptor {

    private static final String TRACE_ID_HEADER = "X-FEIGN-TRACE-ID";
    private static final String LANG_HEADER = "X-FEIGN-LANG";

    @Override
    public void apply(RequestTemplate requestTemplate) {
        String language = LocaleContextHolder.getLocale().getLanguage();
        String traceId = MDC.get("traceId");

        if (StringUtils.isEmpty(traceId)) {
            traceId = UUID.randomUUID().toString();
            MDC.put("traceId", traceId);
        }

        requestTemplate.header(TRACE_ID_HEADER, language);
        requestTemplate.header(LANG_HEADER, traceId);
    }
}
