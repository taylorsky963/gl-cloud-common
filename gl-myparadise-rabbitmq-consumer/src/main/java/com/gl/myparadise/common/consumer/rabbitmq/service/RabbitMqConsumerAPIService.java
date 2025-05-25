package com.gl.myparadise.common.consumer.rabbitmq.service;

import com.gl.myparadise.common.consumer.rabbitmq.config.RabbitMQConsumerConfigurationProperties;
import com.gl.myparadise.common.consumer.rabbitmq.config.RabbitMQListenerQueueProperties;
import com.gl.myparadise.common.consumer.rabbitmq.config.RabbitMQListenerSimpleConfigurationProperties;
import com.gl.myparadise.common.consumer.rabbitmq.config.RabbitMQListenerSimpleRetryConfigurationProperties;

/**
 * @author Vincent
 * @description:
 * @className: RabbitConsumerRegister
 * @projectName: gl-cloud-base
 * @date 2025-05-23 - 02:45
 */
public interface RabbitMqConsumerAPIService {

    void rabbitConsumerRegister(String rabbitBeanName, RabbitMQConsumerConfigurationProperties rabbitMQListenerConfigurationProperties,
                                RabbitMQListenerSimpleConfigurationProperties rabbitMQListenerSimpleConfigurationProperties,
                                RabbitMQListenerSimpleRetryConfigurationProperties rabbitMQListenerSimpleRetryConfigurationProperties,
                                RabbitMQListenerQueueProperties exchangeProperties);
}
