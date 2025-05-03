package com.gl.myparadise.common.server.web.handler;

import com.gl.myparadise.common.bean.exception.BaseException;
import com.gl.myparadise.common.bean.model.ExceptionEnum;
import com.gl.myparadise.common.bean.model.ServiceRsModel;
import org.apache.catalina.connector.ClientAbortException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: GlobalExceptionHandler
 * @ProjectName: myparadise-common
 * @Date 2023-05-01 - 22:54
 */
@ControllerAdvice
@Order(1)
public class GLServerGlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GLServerGlobalExceptionHandler.class);

    @ExceptionHandler(value = {HttpMessageNotReadableException.class, MissingServletRequestPartException.class, MissingServletRequestParameterException.class, MultipartException.class})
    @ResponseBody
    public ServiceRsModel handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {

        logger.error("参数解析异常 : ", e);

        return errorMsg(ExceptionEnum.PARAMETER_ANALYSIS_WRONG.getResponseCode(),
                ExceptionEnum.PARAMETER_ANALYSIS_WRONG.getResponseMsg());

    }

    @ExceptionHandler(value = {IllegalArgumentException.class})
    @ResponseBody
    public ServiceRsModel handleIllegalArgumentException(IllegalArgumentException e) {
        logger.error("参数异常 : ", e);

        return errorMsg(ExceptionEnum.PARAMETER_NOT_MATCH.getResponseCode(),
                ExceptionEnum.PARAMETER_NOT_MATCH.getResponseMsg());
    }

    @ExceptionHandler(value = {HttpMediaTypeNotSupportedException.class})
    @ResponseBody
    public ServiceRsModel handleHttpMessageNotReadableException(HttpMediaTypeNotSupportedException e, HttpServletRequest request) {
        String requestUrl = request.getRequestURL().toString();

        String contentType = request.getContentType();

        logger.error("参数类型不支持异常, URL: {}, Content-Type: {}, 錯誤訊息: ", requestUrl, contentType, e);

        return errorMsg(ExceptionEnum.PARAMETER_NOT_MATCH.getResponseCode(),
                ExceptionEnum.PARAMETER_NOT_MATCH.getResponseMsg());
    }

    @ExceptionHandler(value = {ClientAbortException.class})
    @ResponseBody
    public ServiceRsModel ClientAbortException(ClientAbortException e) {
        logger.error("客户端丢失连接 : ", e);

        return errorMsg(ExceptionEnum.SERVER_CONNECTION.getResponseCode(),
                ExceptionEnum.SERVER_CONNECTION.getResponseMsg());
    }

    @ExceptionHandler(value = {HttpMediaTypeNotAcceptableException.class})
    @ResponseBody
    public ServiceRsModel handleHttpMessageNotAcceptableException(HttpMediaTypeNotAcceptableException e) {
        logger.error("参数类型不接受异常 : ", e);

        return errorMsg(ExceptionEnum.PARAMETER_NOT_MATCH.getResponseCode(),
                ExceptionEnum.PARAMETER_NOT_MATCH.getResponseMsg());
    }

    @ExceptionHandler(value = BaseException.class)
    @ResponseBody
    public ServiceRsModel handler(BaseException e) {

        logger.error("自定义异常 : ", e);

        return errorMsg(e.getErrCode(),
                e.getMessage());

    }

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ServiceRsModel handler(Exception e) {

        logger.error("系统异常 : ", e);

        return errorMsg(ExceptionEnum.SERVER_BUSY.getResponseCode(),
                ExceptionEnum.SERVER_BUSY.getResponseMsg());

    }

    public ServiceRsModel errorMsg(String rsCode, String messages){

        logger.error("error code : " + rsCode);

        logger.error("error message : " + messages);

        return new ServiceRsModel().error(rsCode, messages);

    }

}
