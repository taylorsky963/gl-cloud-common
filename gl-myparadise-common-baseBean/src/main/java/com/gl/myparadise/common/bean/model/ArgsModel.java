package com.gl.myparadise.common.bean.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Vincent
 * @Description: 拦截器参数MODEL
 * @ClassName: ArgsModel
 * @ProjectName: ly-common
 * @Date 2023-04-27 - 21:50
 */
@Data
public class ArgsModel implements Serializable {

    private String argName;

    private Object argValue;

}
