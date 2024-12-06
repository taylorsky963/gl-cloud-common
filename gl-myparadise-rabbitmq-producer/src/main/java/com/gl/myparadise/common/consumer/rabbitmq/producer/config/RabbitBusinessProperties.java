package com.gl.myparadise.common.consumer.rabbitmq.producer.config;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: RabbitBusinessProperties
 * @ProjectName: gl-myparadise-common
 * @Date 2023-05-26 - 0:50
 */
public class RabbitBusinessProperties {

    private String businessCode;

    private String routingkey;

    private String exchangeName;

    public String getBusinessCode() {
        return businessCode;
    }

    public void setBusinessCode(String businessCode) {
        this.businessCode = businessCode;
    }

    public String getRoutingkey() {
        return routingkey;
    }

    public void setRoutingkey(String routingkey) {
        this.routingkey = routingkey;
    }

    public String getExchangeName() {
        return exchangeName;
    }

    public void setExchangeName(String exchangeName) {
        this.exchangeName = exchangeName;
    }

}
