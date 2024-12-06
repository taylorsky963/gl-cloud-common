package com.gl.myparadise.common.consumer.rabbitmq.producer.config;

import java.util.Map;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: RabbitExchangeProperties
 * @ProjectName: gl-myparadise-common
 * @Date 2023-05-26 - 0:51
 */
public class RabbitExchangeProperties {

    //交换机名
    private String exchangeName;

    //交换机类型
    private String exchangeType;

    //自定义交换机类型
    private String customType;

    private Boolean durable = Boolean.TRUE;

    private Boolean autoDelete = Boolean.FALSE;

    /**
     * 交换机参数（自定义交换机）
     */
    private Map<String, Object> arguments;

    public String getExchangeName() {
        return exchangeName;
    }

    public void setExchangeName(String exchangeName) {
        this.exchangeName = exchangeName;
    }

    public String getExchangeType() {
        return exchangeType;
    }

    public void setExchangeType(String exchangeType) {
        this.exchangeType = exchangeType;
    }

    public String getCustomType() {
        return customType;
    }

    public void setCustomType(String customType) {
        this.customType = customType;
    }

    public Map<String, Object> getArguments() {
        return arguments;
    }

    public void setArguments(Map<String, Object> arguments) {
        this.arguments = arguments;
    }

    public Boolean getDurable() {
        return durable;
    }

    public void setDurable(Boolean durable) {
        this.durable = durable;
    }

    public Boolean getAutoDelete() {
        return autoDelete;
    }

    public void setAutoDelete(Boolean autoDelete) {
        this.autoDelete = autoDelete;
    }

}
