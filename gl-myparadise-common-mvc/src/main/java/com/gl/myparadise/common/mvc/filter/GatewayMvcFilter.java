package com.gl.myparadise.common.mvc.filter;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@Order(0)
@Component
@WebFilter(urlPatterns = "/*", filterName = "requestLogTraceFilter")
public class GatewayMvcFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        String traceId = UUID.randomUUID().toString();
        MDC.put("traceId", traceId);

        HttpServletResponse response = (HttpServletResponse) servletResponse;
        response.setHeader("TID", traceId);

        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            MDC.clear();
            LocaleContextHolder.resetLocaleContext();
        }
    }
}