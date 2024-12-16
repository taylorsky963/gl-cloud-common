package com.gl.myparadise.common.mvc.Interceptor;

import com.gl.myparadise.common.mvc.controller.BaseWebController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;

/**
 * @author Vincent
 * @description: 修改Controller 基礎訪問根
 * @className: BaseWebFormRequestMappingHandler
 * @projectName: gl-new-cloud-base
 * @date 2024-12-15 - 16:50
 */
public class BaseWebFormRequestMappingHandler extends RequestMappingHandlerMapping {

    private static final Logger logger = LoggerFactory.getLogger(BaseWebFormRequestMappingHandler.class);

    @Value("${web.base.mapping.url:/bg/auth}")
    private String baseMappingUrl;

    @Override
    protected RequestMappingInfo getMappingForMethod(Method method, Class<?> handlerType) {

        RequestMappingInfo requestMappingInfo = super.getMappingForMethod(method, handlerType);

        if (requestMappingInfo != null) {

            if (BaseWebController.class.isAssignableFrom(handlerType)) {

                RequestMapping classRequestMapping = handlerType.getAnnotation(RequestMapping.class);

                if (classRequestMapping != null) {

                    // 动态设置类路径并返回新的 RequestMappingInfo
                    return RequestMappingInfo.paths(baseMappingUrl).build().combine(requestMappingInfo);


                } else {

                    logger.error("requestMappingInfo classRequestMapping is null");

                }

            }

        }

        return requestMappingInfo;

    }
}
