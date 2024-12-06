package com.gl.myparadise.common.mvc.model;

import com.gl.myparadise.common.bean.exception.BaseErrorInfoInterface;
import com.gl.myparadise.common.bean.model.ExceptionEnum;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: WebExceptionEnum
 * @ProjectName: myparadise-common
 * @Date 2023-05-11 - 9:10
 */
public class WebExceptionEnum implements BaseErrorInfoInterface {

    /**
     * 错误码
     */
    private final String responseCode;

    /**
     * 错误描述
     */
    private final String responseMsg;

    WebExceptionEnum(String responseCode, String responseMsg) {

        this.responseCode = responseCode;

        this.responseMsg = responseMsg;

    }

    @Override
    public String getResponseCode() {

        return this.responseCode;

    }

    @Override
    public String getResponseMsg() {

        return this.responseMsg;

    }
}
