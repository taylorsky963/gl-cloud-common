package com.gl.myparadise.common.mvc.Interceptor;

import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * @author Vincent
 * @description:
 * @className: BaseWebFormUrlConfig
 * @projectName: gl-new-cloud-base
 * @date 2024-12-15 - 23:06
 */
@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
public class BaseWebFormUrlConfig implements WebMvcRegistrations {

    /**
     * 將baseUrl 加入到 requestMapping 的url
     * @return
     */
    @Override
    public RequestMappingHandlerMapping getRequestMappingHandlerMapping(){

        return new BaseWebFormRequestMappingHandler();

    }
}
