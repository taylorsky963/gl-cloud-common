package com.gl.myparadise.common.consumer.rabbitmq.config;

import java.util.List;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: RabbitMQListenerConfigurationProperties
 * @ProjectName: gl-myparadise-common
 * @Date 2023-05-30 - 11:31
 */
public class RabbitMQListenerConfigurationProperties {

    private List<String> names;

    public List<String> getNames() {
        return names;
    }

    public void setNames(List<String> names) {
        this.names = names;
    }
}
