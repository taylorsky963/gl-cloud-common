package com.gl.myparadise.common.mvc.language;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gl.myparadise.common.tools.jason.GJonUtil;

import java.util.HashMap;
import java.util.Map;

public class I18nMsgModel {
    private String id;
    private Map<String, Object> data;

    @JsonIgnore
    private String[] keys;

    @JsonIgnore
    private Object[] vals;

    /**
     * 国际化编号构造方法
     * @param id 国际化编号
     */
    public I18nMsgModel(String id) {
        this.id = id;
        this.data = new HashMap<>();
    }

    /**
     * 国际化编号 + 国际化参数 构造方法
     * @param id 国际化编号
     * @param data 国际化参数
     */
    public I18nMsgModel(String id, Map<String, Object> data) {
        this.id = id;
        this.data = data;
    }

    /**
     * 往国际化参数的map里边，添加一组参数
     * @param name
     * @param val
     * @return
     */
    public I18nMsgModel add(String name, Object val) {
        data.put(name, val);
        return this;
    }

    /**
     * 将消息体转成 json 字符串
     * @return
     */
    @Override
    public String toString() {
        return GJonUtil.toJson(this);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public I18nMsgModel addKeys(String... keys) {
        this.keys = keys;
        return this;
    }

    public I18nMsgModel addVals(Object... vals) {
        this.vals = vals;
        return this;
    }

    public String build() {
        if (null != keys) {
            for (int i = 0; i < keys.length; i++) {
                data.put(keys[i], vals[i]);
            }
            keys = null;
            vals = null;
        }
        return toString();
    }

    /**
     * 静态国际化参数构造方法
     * @param id
     * @return
     */
    public static I18nMsgModel of(String id) {
        return new I18nMsgModel(id);
    }

    /**
     * 仅仅含有 id，无参数的消息
     *
     * @param id
     * @return
     */
    public static String just(String id) {
        return new I18nMsgModel(id).toString();
    }


}
