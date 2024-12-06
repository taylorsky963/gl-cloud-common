package com.gl.myparadise.common.mvc.model;

import com.gl.myparadise.common.bean.model.ExceptionEnum;
import com.gl.myparadise.common.mvc.language.I18nUtils;
import com.gl.myparadise.common.tools.jason.GJonUtil;
import org.springframework.ui.ModelMap;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author Vincent
 * @Description: web端页面交互MODEL
 * @ClassName: ModelJson
 * @ProjectName: ly-common
 * @Date 2023-04-27 - 21:50
 */
public class ModelJson extends ModelMap {

    /**
     * 返回信息编码
     */
    String rsCode = "";

    /**
     * 操作是否成功
     */
    Boolean success = true;

    /**
     * 返回信息说明
     */
    String rsMessages = "";

    /**
     * 返回数据
     */
    Object rsData = null;

//    /**
//     * 无数据不用管，为了生成统一的api 文档格式
//     */
//    private List rsDataList = null;
//
//    /**
//     * 无数据不用管，为了生成统一的api 文档格式
//     */
//    private String version = null;

    public ModelJson(){
        put("success", success);
        put("rsCode", ExceptionEnum.SUCCESS.getResponseCode());
        put("rsMessages", I18nUtils.get(ExceptionEnum.SUCCESS.getResponseMsg()));
        put("rsData", rsData);
    }

    public static ModelJson success() {
        return new ModelJson();
    }

    public static ModelJson success(Object rsData) {
        return new ModelJson().setRsData(rsData);
    }

    public static ModelJson error(String rsMessages) {
        return new ModelJson().setRsCode(ExceptionEnum.SERVER_ERROR.getResponseCode()).setSuccess(false).setRsMessages(rsMessages);
    }

    public static ModelJson error(String code, String rsMessages) {
        return new ModelJson().setSuccess(false).setRsCode(code).setRsMessages(rsMessages);
    }

    public static ModelJson error(String code, String rsMessages, Object rsData) {
        return new ModelJson().setSuccess(false).setRsCode(code).setRsMessages(rsMessages).setRsData(rsData);
    }

    public String getRsCode() {
        return rsCode;
    }

    public ModelJson setRsCode(String rsCode) {
        this.rsCode = rsCode;
        put("rsCode", rsCode);
        return this;
    }

    public Boolean getSuccess() {
        return (Boolean) get("success");
    }

    public ModelJson setSuccess(Boolean success) {
        this.success = success;
        put("success", success);
        return this;
    }

    public String getRsMessages() {
        return rsMessages;
    }

    public ModelJson updateRsMessagesI18n() {

        return setRsMessages((String)get("rsCode"));
    }

    public ModelJson setRsMessages(String rsMessages) {

        rsMessages = I18nUtils.get(rsMessages);

        this.rsMessages = rsMessages;
        put("rsMessages", rsMessages);
        return this;
    }

    public ModelJson setRsMessages(String rsMessages, Object... args) {

        rsMessages = I18nUtils.get(rsMessages, args);

        this.rsMessages = rsMessages;
        put("rsMessages", rsMessages);
        return this;
    }

    public Object getRsData() {
        return rsData;
    }

    public ModelJson setRsData(Object rsData) {
        this.rsData = rsData;
        put("rsData", rsData);
        return this;
    }

    @Override
    public String toString() {

        return GJonUtil.toJson(this);
    }

    public ModelJson add(String key, Object value) {
        if (null == value) {
            return this;
        }
        super.put(key, value);
        return this;
    }

    public ModelJson addAll(Map<String, ?> value) {
        if (null == value) {
            return this;
        }
        super.addAllAttributes(value);
        return this;
    }

    public ModelJson addAll(Collection<?> value) {
        if (null == value) {
            return this;
        }
        super.addAllAttributes(value);
        return this;
    }

    @Override
    public ModelJson addAttribute(String attributeName, Object attributeValue) {
        if(null == attributeValue){
            return this;
        }
        super.addAttribute(attributeName, attributeValue);
        return this;
    }

    @Override
    public ModelMap addAllAttributes(Map<String, ?> attributes) {
        super.addAllAttributes(attributes);
        return this;
    }

    @Override
    public ModelMap addAllAttributes(Collection<?> attributeValues) {
        super.addAllAttributes(attributeValues);
        return this;
    }

    @Override
    public ModelMap addAttribute(Object attributeValue) {
        super.addAttribute(attributeValue);
        return this;
    }

}
