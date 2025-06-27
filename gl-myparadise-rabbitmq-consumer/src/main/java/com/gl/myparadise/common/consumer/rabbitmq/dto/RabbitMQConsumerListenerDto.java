package com.gl.myparadise.common.consumer.rabbitmq.dto;

import com.gl.myparadise.common.consumer.rabbitmq.config.RabbitMQListenerQueueProperties;
import com.gl.myparadise.common.consumer.rabbitmq.config.RabbitMQListenerSimpleConfigurationProperties;
import com.gl.myparadise.common.consumer.rabbitmq.config.RabbitMQListenerSimpleRetryConfigurationProperties;

import java.io.Serializable;

/**
 * @author Vincent
 * @description:
 * @className: RabbitMQConsumerConfigurationDto
 * @projectName: gl-cloud-base
 * @date 2025-06-26 - 02:01
 */

public class RabbitMQConsumerListenerDto implements Serializable {

    private static final long serialVersionUID = 3458466432457936442L;

    private String rabbitBeanName;

    private RabbitMQListenerSimpleConfigurationProperties rabbitMQListenerSimpleConfigurationProperties;

    private RabbitMQListenerSimpleRetryConfigurationProperties rabbitMQListenerSimpleRetryConfigurationProperties;

    private RabbitMQListenerQueueProperties rabbitMQListenerQueueProperties;

    public String getRabbitBeanName() {
        return rabbitBeanName;
    }

    public void setRabbitBeanName(String rabbitBeanName) {
        this.rabbitBeanName = rabbitBeanName;
    }

    public RabbitMQListenerSimpleConfigurationProperties getRabbitMQListenerSimpleConfigurationProperties() {
        return rabbitMQListenerSimpleConfigurationProperties;
    }

    public void setRabbitMQListenerSimpleConfigurationProperties(RabbitMQListenerSimpleConfigurationProperties rabbitMQListenerSimpleConfigurationProperties) {
        this.rabbitMQListenerSimpleConfigurationProperties = rabbitMQListenerSimpleConfigurationProperties;
    }

    public RabbitMQListenerSimpleRetryConfigurationProperties getRabbitMQListenerSimpleRetryConfigurationProperties() {
        return rabbitMQListenerSimpleRetryConfigurationProperties;
    }

    public void setRabbitMQListenerSimpleRetryConfigurationProperties(RabbitMQListenerSimpleRetryConfigurationProperties rabbitMQListenerSimpleRetryConfigurationProperties) {
        this.rabbitMQListenerSimpleRetryConfigurationProperties = rabbitMQListenerSimpleRetryConfigurationProperties;
    }

    public RabbitMQListenerQueueProperties getRabbitMQListenerQueueProperties() {
        return rabbitMQListenerQueueProperties;
    }

    public void setRabbitMQListenerQueueProperties(RabbitMQListenerQueueProperties rabbitMQListenerQueueProperties) {
        this.rabbitMQListenerQueueProperties = rabbitMQListenerQueueProperties;
    }
}
