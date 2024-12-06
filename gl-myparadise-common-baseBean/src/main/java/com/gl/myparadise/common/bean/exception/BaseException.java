package com.gl.myparadise.common.bean.exception;

import com.gl.myparadise.common.bean.constant.BaseExceptionConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: BaseException
 * @ProjectName: myparadise-common
 * @Date 2023-05-01 - 20:36
 */
public class BaseException extends RuntimeException {

    private static final long serialVersionUID = 779097140579256830L;

    private String errCode;

    private List<String> params = new ArrayList<String>(0);

    private String errMessage;

    public BaseException() {

        this(null, null, null);

    }

    public BaseException(Exception e) {
        this(null, null, e);
    }

    public BaseException(String errMessage, Exception e) {
        this(errMessage, null, e);
    }

    protected BaseException(String errMessage, String errCode, Exception e) {

        super(e);

        this.errMessage = errMessage == null ? "" : errMessage;

        this.errCode = errCode == null ? BaseExceptionConstants.PLATFORM_SYSTEM_MESSAGE_E500001 : errCode;

    }

    public BaseException(BaseErrorInfoInterface errorInfoInterface) {

        super(errorInfoInterface.getResponseCode());

        this.errCode = errorInfoInterface.getResponseCode();

        this.errMessage = errorInfoInterface.getResponseMsg();

    }

    public BaseException(BaseErrorInfoInterface errorInfoInterface, Throwable cause) {

        super(errorInfoInterface.getResponseCode(), cause);

        this.errCode = errorInfoInterface.getResponseCode();

        this.errMessage = errorInfoInterface.getResponseMsg();
    }

    public BaseException(String errCode) {

        this.errCode = errCode;

        if (errMessage == null) {

            this.errMessage = errCode;

        }
    }

    public BaseException(String errCode, List<String> params) {

        this.errCode = errCode;

        this.params = params;

        if (errMessage == null) {

            this.errMessage = errCode;

        }
    }

    public BaseException(String errCode, String[] params) {

        this.errCode = errCode;

        for (String s : params) {

            this.params.add(s);

        }

        if (errMessage == null) {

            this.errMessage = errCode;

        }
    }

    public BaseException(String errCode, String errMessage) {

        this.errCode = errCode;

        this.errMessage = errMessage;

    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {

        this.errCode = errCode;

        if (errMessage == null) {

            this.errMessage = errCode;

        }
    }

    public List<String> getParams() {
        return params;
    }

    public void setParams(List<String> params) {
        this.params = params;
    }

    @Override
    public String getMessage() {

        if (this.errMessage != null && this.errMessage.length() > 0) {

            return this.errMessage;

        }

        return super.getMessage();

    }

    public void setMessage(String errMessage) {
        this.errMessage = errMessage;
    }

    public void addParam(String p) {
        params.add(p);
    }

}
