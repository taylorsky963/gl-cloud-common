package com.gl.myparadise.common.mvc.handler;

import com.gl.myparadise.common.bean.model.ExceptionEnum;
import com.gl.myparadise.common.bean.model.ServiceRsModel;
import com.gl.myparadise.common.mvc.model.ModelJson;
import com.gl.myparadise.common.tools.util.ListUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: ResponseAdvice
 * @ProjectName: myparadise-common
 * @Date 2023-05-08 - 15:45
 */
@RestControllerAdvice(basePackages = "com.gl.myparadise")
public class ResponseAdvice implements ResponseBodyAdvice<Object> {

    private static final Logger logger = LoggerFactory.getLogger(ResponseAdvice.class);

    @Override
    public boolean supports(MethodParameter methodParameter, Class<?
            extends HttpMessageConverter<?>> aClass) {

        Class<?> classz = methodParameter.getParameterType();

        logger.debug("ResponseAdvice  classz name : " + classz.getName());

        if(classz.equals(String.class)){

            return false;

        }

        if(classz.equals(ModelJson.class)){

            return false;

        }

        return true;

    }

    @Override
    public Object beforeBodyWrite(Object object, MethodParameter methodParameter,
                                  MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass,
                                  ServerHttpRequest serverHttpRequest,
                                  ServerHttpResponse serverHttpResponse) {

        if(object.getClass().equals(ModelJson.class)){

            return ((ModelJson) object).updateRsMessagesI18n();

        }else{

            if(object.getClass().equals(ServiceRsModel.class)){

                ServiceRsModel serviceRsModel = (ServiceRsModel) object;

                Object data = null;

                if(serviceRsModel.isSuccess()){

                    if(ListUtil.isNotEmpty(serviceRsModel.getRsDataList())){

                        data = serviceRsModel.getRsDataList();

                    }else if(serviceRsModel.getRsData() != null){

                        data = serviceRsModel.getRsData();

                    }

                    return ModelJson.success(data);

                }else{

                    String rsMessages = serviceRsModel.getRsMessages();

                    String rsCode = serviceRsModel.getRsCode();

                    if(StringUtils.isEmpty(rsMessages)){

                        rsMessages = ExceptionEnum.SERVER_BUSY.getResponseMsg();

                    }

                    if(StringUtils.isEmpty(rsCode)){

                        rsCode = ExceptionEnum.SERVER_ERROR.getResponseCode();

                    }

                    return ModelJson.error(rsCode, rsMessages);

                }

            }

            return new ModelJson().setRsData(object);

        }

    }
}
