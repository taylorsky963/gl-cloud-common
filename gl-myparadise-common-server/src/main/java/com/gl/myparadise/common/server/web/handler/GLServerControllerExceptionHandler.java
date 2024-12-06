package com.gl.myparadise.common.server.web.handler;

import com.gl.myparadise.common.bean.model.ExceptionEnum;
import com.gl.myparadise.common.bean.model.ServiceRsModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: WebControllerExceptionHandler
 * @ProjectName: myparadise-common
 * @Date 2023-05-01 - 23:53
 */
@ControllerAdvice
@AutoConfigureAfter(GLServerGlobalExceptionHandler.class)
public class GLServerControllerExceptionHandler extends GLServerGlobalExceptionHandler{

    private static final Logger logger = LoggerFactory.getLogger(GLServerControllerExceptionHandler.class);

    /**
     * Validator 参数校验异常处理
     *
     * @param bindingEx
     * @return
     */
    @ExceptionHandler(value = BindException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ServiceRsModel handleMethodVoArgumentNotValidExceptionAjax(BindException bindingEx) {

        return super.errorMsg(ExceptionEnum.PARAMETER_NOT_MATCH.getResponseCode(),
                ExceptionEnum.PARAMETER_NOT_MATCH.getResponseMsg());

    }
}
