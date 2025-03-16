package com.gl.myparadise.common.mvc.config;

import com.gl.myparadise.common.mvc.Interceptor.FeignInterceptor;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GlobalFeignConfig {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return new FeignInterceptor();
    }
}
