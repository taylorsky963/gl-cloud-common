package com.gl.myparadise.common.mvc.Interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Vincent
 * @description: 暂时不用
 * @className: BaseWebFormInterceptor1
 * @projectName: gl-new-cloud-base
 * @date 2024-12-16 - 01:53
 */
//@Component
public class BaseFeignClientInterceptor implements RequestInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(BaseWebFormInterceptor.class);

    @Value("${bg.feign.client.mapping.url:/bg/auth1}") // 动态前缀
    private String baseFeignClientMappingUrl;

    @Override
    public void apply(RequestTemplate requestTemplate) {

        String originalUrl = requestTemplate.url();

        String modifiedUrl = baseFeignClientMappingUrl + originalUrl;

        logger.debug("BaseFeignClientInterceptor modifiedUrl is : " + modifiedUrl);

        // 设置修改后的路径
        requestTemplate.uri(modifiedUrl);

    }
}
