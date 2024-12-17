package com.gl.myparadise.common.bean.model;

import java.io.Serializable;
import java.util.List;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: ServiceRsModel
 * @ProjectName: gl-myparadise-common
 * @Date 2023-06-08 - 22:00
 */
public class ServiceRsModel<T> implements Serializable {

    private static final long serialVersionUID = -5614231774598972404L;

    /**
     *操作是否成功
     */
    private boolean success = true;

    /**
     * 返回信息code
     */
    private String rsCode = ExceptionEnum.SUCCESS.getResponseCode();

    /**
     * 返回信息描述
     */
    private String rsMessages = ExceptionEnum.SUCCESS.getResponseMsg();

    /**
     * 返回的数据object
     */
    private T rsData = null;

    /**
     * 返回的数据list
     */
    private List<T> rsDataList = null;

    private String version = null;

    public T getRsData() {
        return rsData;
    }

    public ServiceRsModel<T> setRsData(T rsData) {

        this.rsData = rsData;

        return this;
    }

    public List<T> getRsDataList() {

        return rsDataList;

    }

    public ServiceRsModel<T> setRsDataList(List<T> rsDataList) {

        this.rsDataList = rsDataList;

        return this;
    }

    public ServiceRsModel<T> error(){

        error(ExceptionEnum.SERVER_ERROR.getResponseMsg());

        return this;

    }

    public ServiceRsModel<T> error(String rsMessages){

        error(ExceptionEnum.SERVER_ERROR.getResponseCode(), rsMessages);

        return this;

    }

    public ServiceRsModel<T> error(String rsCode, String rsMessages){

        setSuccess(false);

        setRsMessages(rsMessages);

        setRsCode(rsCode);

        return this;
    }

    public boolean isSuccess() {
        return success;
    }

    public ServiceRsModel<T> setSuccess(boolean success) {

        this.success = success;

        return this;

    }

    public String getRsCode() {
        return rsCode;
    }

    public ServiceRsModel<T> setRsCode(String rsCode) {

        this.rsCode = rsCode;

        return this;
    }

    public String getRsMessages() {
        return rsMessages;
    }

    public ServiceRsModel<T> setRsMessages(String rsMessages) {

        this.rsMessages = rsMessages;

        return this;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public static <T> ServiceRsModel<T> ok(T t) {
        ServiceRsModel<T> model = new ServiceRsModel<>();
        model.setRsData(t);
        return model;
    }
    public static <T> ServiceRsModel<T> fail() {
        ServiceRsModel<T> model = new ServiceRsModel<>();
        model.setSuccess(false);
        model.setRsMessages(ExceptionEnum.SERVER_ERROR.getResponseMsg());
        model.setRsCode(ExceptionEnum.SERVER_ERROR.getResponseCode());
        return model;
    }
}
