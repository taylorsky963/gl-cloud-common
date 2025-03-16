package com.gl.myparadise.common.server.web.handler;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Locale;
import java.util.UUID;

@Slf4j
@Order(0)
@Component
@WebFilter(urlPatterns = "/*", filterName = "requestLogTraceFilter")
public class RequestLogTraceFilter implements Filter {

    private static final String TRACE_ID_HEADER = "X-FEIGN-TRACE-ID";
    private static final String LANG_HEADER = "X-FEIGN-LANG";
    private static final Locale DEFAULT_LANG = Locale.US;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        String traceId = request.getHeader(TRACE_ID_HEADER);
        if (StringUtils.isEmpty(traceId)) {
            traceId = UUID.randomUUID().toString();
        }
        MDC.put("traceId", traceId);

        String lang = request.getHeader(LANG_HEADER);
        if (!StringUtils.isEmpty(lang)) {
            try {
                Locale locale = Locale.forLanguageTag(lang);
                LocaleContextHolder.setLocale(locale);
            } catch (Exception e) {
                LocaleContextHolder.setLocale(DEFAULT_LANG);
            }
        }

        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            MDC.clear();
            LocaleContextHolder.resetLocaleContext();
        }
    }
}