package com.gl.myparadise.common.server.web.handler;

import com.gl.myparadise.common.server.web.config.FeignConfiguration;
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


@Slf4j
@Order(0)
@Component
@WebFilter(urlPatterns = "/*", filterName = "requestLogTraceFilter")
public class FeignTraceFilter implements Filter {


    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;

        String traceId = request.getHeader(FeignConfiguration.TRACE_ID_HEADER);

        if (StringUtils.isEmpty(traceId)) {
            chain.doFilter(req, res);
            return;
        }

        MDC.put("traceId", traceId);

        try {
            chain.doFilter(req, res);
        } finally {
            MDC.clear();
            LocaleContextHolder.resetLocaleContext();
        }
    }
}