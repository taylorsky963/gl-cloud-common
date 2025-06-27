package com.gl.myparadise.common.consumer.rabbitmq.service;

import com.gl.myparadise.common.consumer.rabbitmq.config.RabbitMQConsumerConfigurationProperties;
import com.gl.myparadise.common.consumer.rabbitmq.config.RabbitMQListenerQueueProperties;
import com.gl.myparadise.common.consumer.rabbitmq.config.RabbitMQListenerSimpleConfigurationProperties;
import com.gl.myparadise.common.consumer.rabbitmq.config.RabbitMQListenerSimpleRetryConfigurationProperties;
import com.gl.myparadise.common.consumer.rabbitmq.dto.RabbitMQConsumerListenerDto;

import java.util.List;

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

    void rabbitConsumerMoreListenerRegister(RabbitMQConsumerConfigurationProperties rabbitMQListenerConfigurationProperties,
                                            List<RabbitMQConsumerListenerDto> rabbitMQConsumerConfigurationDtoList);
}
