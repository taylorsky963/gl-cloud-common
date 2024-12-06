package com.gl.myparadise.common.mvc.Interceptor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: BaseWebFormInterceptorConfig
 * @Date 2020-06-15 - 16:19
 */
@Configuration
public class BaseWebFormInterceptorConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //设置URL全路径参数MYBASEPATH
        registry.addInterceptor(BaseWebFormInterceptor()).addPathPatterns("/**");

    }

    @Bean
    public BaseWebFormInterceptor BaseWebFormInterceptor() {

        return new BaseWebFormInterceptor();

    }

}
