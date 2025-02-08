package com.gl.myparadise.common.bean.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: PlatformCacheMqModel
 * @ProjectName: gl-matrix-parent
 * @Date 2023-09-08 - 22:19
 */
@Data
@ToString(callSuper = true)
@Accessors(chain = true)
public class WebCacheMqModel implements Serializable {

    private static final long serialVersionUID = 7169531739932400680L;

    private Object data;

    /**
     * 接收端操作代码
     */
    private String opTypeCode;

    /**
     * 广播还是直连
     */
    private String sendType;

    /**
     * mq 得配置代码
     */
    private String sendBussesCode;

}