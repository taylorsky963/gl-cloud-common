package com.gl.myparadise.common.bean.model;

import java.io.Serializable;

/**
 * @author Vincent
 * @Description: 拦截器参数MODEL
 * @ClassName: ArgsModel
 * @ProjectName: ly-common
 * @Date 2023-04-27 - 21:50
 */
public class ArgsModel implements Serializable {

    private String argName;

    private Object argValue;

    public String getArgName() {
        return argName;
    }

    public void setArgName(String argName) {
        this.argName = argName;
    }

    public Object getArgValue() {
        return argValue;
    }

    public void setArgValue(Object argValue) {
        this.argValue = argValue;
    }
}
