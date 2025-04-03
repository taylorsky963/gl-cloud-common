package com.gl.myparadise.common.webclient.util;

import java.io.Serializable;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: MatrixResponseData
 * @ProjectName: gl-matrix-parent
 * @Date 2023-07-27 - 8:01 p.m.
 */
public class HttpResponseData implements Serializable {

    private static final long serialVersionUID = 7920556578653659423L;

    private String responseStatus;

    private String responseErrorCode;

    private String responseErrorMessages;

    public String getResponseErrorMessages() {
        return responseErrorMessages;
    }

    public void setResponseErrorMessages(String responseErrorMessages) {
        this.responseErrorMessages = responseErrorMessages;
    }

    public String getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(String responseStatus) {
        this.responseStatus = responseStatus;
    }

    public String getResponseErrorCode() {
        return responseErrorCode;
    }

    public void setResponseErrorCode(String responseErrorCode) {
        this.responseErrorCode = responseErrorCode;
    }
}