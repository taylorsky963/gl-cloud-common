package com.gl.myparadise.common.bean.exception;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: BaseErrorInfoInterface
 * @ProjectName: myparadise-common
 * @Date 2023-05-01 - 20:46
 */
public interface  BaseErrorInfoInterface {

    /**
     *  错误码
     * @return
     */
    String getResponseCode();

    /**
     * 错误描述
     * @return
     */
    String getResponseMsg();

}
