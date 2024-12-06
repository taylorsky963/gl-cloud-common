package com.gl.myparadise.common.bean.model;

import com.gl.myparadise.common.bean.constant.BaseExceptionConstants;
import com.gl.myparadise.common.bean.exception.BaseErrorInfoInterface;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: ExceptionEnum
 * @ProjectName: myparadise-common
 * @Date 2023-05-07 - 23:57
 */
public enum ExceptionEnum implements BaseErrorInfoInterface {

    // 数据操作错误定义
    SUCCESS(BaseExceptionConstants.PLATFORM_SYSTEM_CODE_S000001, BaseExceptionConstants.PLATFORM_SYSTEM_MESSAGE_S000001),
    PARAMETER_NOT_MATCH(BaseExceptionConstants.PLATFORM_SYSTEM_CODE_E900001,BaseExceptionConstants.PLATFORM_SYSTEM_MESSAGE_E900001),
    PARAMETER_ANALYSIS_WRONG(BaseExceptionConstants.PLATFORM_SYSTEM_CODE_E900002,BaseExceptionConstants.PLATFORM_SYSTEM_MESSAGE_E900002),
    NOT_FOUND(BaseExceptionConstants.PLATFORM_SYSTEM_CODE_E400004, BaseExceptionConstants.PLATFORM_SYSTEM_MESSAGE_E400004),
    SERVER_ERROR(BaseExceptionConstants.PLATFORM_SYSTEM_CODE_E500001, BaseExceptionConstants.PLATFORM_SYSTEM_MESSAGE_E500001),
    SERVER_BUSY(BaseExceptionConstants.PLATFORM_SYSTEM_CODE_E500002, BaseExceptionConstants.PLATFORM_SYSTEM_MESSAGE_E500002),
    SERVER_CONNECTION(BaseExceptionConstants.PLATFORM_SYSTEM_CODE_E500003, BaseExceptionConstants.PLATFORM_SYSTEM_MESSAGE_E500003);

    /**
     * 错误码
     */
    private final String responseCode;

    /**
     * 错误描述
     */
    private final String responseMsg;

    ExceptionEnum(String responseCode, String responseMsg) {

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
