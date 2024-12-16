package com.gl.myparadise.common.bean.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExchangeEnum {

    globalReport("global-report-exchange", "全局的事件上报Exchange"),
    globalUser("global-user-exchange", "全局的用户资料上报Exchange"),

    ;

    private final String exchangeName;
    private final String desc;


}