package com.gl.myparadise.common.server.web.handler;

import com.gl.myparadise.common.bean.model.ServiceRsModel;
import com.gl.myparadise.common.server.web.annotation.ResponseServerNoAdvice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: ResponseAdvice
 * @ProjectName: myparadise-common
 * @Date 2023-05-08 - 15:45
 */
@RestControllerAdvice(basePackages = "com.gl.myparadise")
public class GLServerResponseAdvice implements ResponseBodyAdvice<Object> {

    private static final Logger logger = LoggerFactory.getLogger(GLServerResponseAdvice.class);

    @Override
    public boolean supports(MethodParameter methodParameter, Class<?
            extends HttpMessageConverter<?>> aClass) {

        Class<?> classz = methodParameter.getParameterType();

        logger.debug("GLServerResponseAdvice  classz name : " + classz.getName());

        if (methodParameter.getDeclaringClass().isAnnotationPresent(ResponseServerNoAdvice.class) ||
                methodParameter.hasMethodAnnotation(ResponseServerNoAdvice.class)) {

            return false;

        }


        if(classz.equals(String.class)){

            return false;

        }

        if(classz.equals(ServiceRsModel.class)){

            return false;

        }

        return true;

    }

    @Override
    public Object beforeBodyWrite(Object object, MethodParameter methodParameter,
                                  MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass,
                                  ServerHttpRequest serverHttpRequest,
                                  ServerHttpResponse serverHttpResponse) {

        if (object == null) {

            return new ServiceRsModel().setRsData(null);

        }

        if(object.getClass().equals(ServiceRsModel.class)){

            return object;

        }else{

            return new ServiceRsModel().setRsData(object);

        }

    }
}
