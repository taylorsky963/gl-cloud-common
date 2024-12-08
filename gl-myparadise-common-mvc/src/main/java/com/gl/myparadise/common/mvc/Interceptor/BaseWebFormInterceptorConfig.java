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
        registry.addInterceptor(baseWebFormInterceptor()).addPathPatterns("/**");
        registry.addInterceptor(glWebAPITimeInterceptor()).addPathPatterns("/**").order(1);

    }

    @Bean
    public BaseWebFormInterceptor baseWebFormInterceptor() {

        return new BaseWebFormInterceptor();

    }

    /**
     * api 接口访问时间超过30 打印log
     * @return
     */
    @Bean
    public GLWebAPITimeInterceptor glWebAPITimeInterceptor() {

        return new GLWebAPITimeInterceptor();

    }

}
