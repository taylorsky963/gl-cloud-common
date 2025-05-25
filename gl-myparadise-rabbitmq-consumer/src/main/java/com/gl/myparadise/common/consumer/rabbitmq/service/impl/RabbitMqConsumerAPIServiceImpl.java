package com.gl.myparadise.common.consumer.rabbitmq.service.impl;

import com.gl.myparadise.common.consumer.rabbitmq.config.RabbitMQConsumerConfigurationProperties;
import com.gl.myparadise.common.consumer.rabbitmq.config.RabbitMQListenerQueueProperties;
import com.gl.myparadise.common.consumer.rabbitmq.config.RabbitMQListenerSimpleConfigurationProperties;
import com.gl.myparadise.common.consumer.rabbitmq.config.RabbitMQListenerSimpleRetryConfigurationProperties;
import com.gl.myparadise.common.consumer.rabbitmq.constants.RabbitMqConsumerConstants;
import com.gl.myparadise.common.consumer.rabbitmq.init.RabbitListenerFactoryBuilder;
import com.gl.myparadise.common.consumer.rabbitmq.init.RabbitMqConsumerBeanFactory;

import com.gl.myparadise.common.consumer.rabbitmq.service.RabbitMqConsumerAPIService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.api.RabbitListenerErrorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;


/**
 * @author Vincent
 * @description:
 * @className: RabbitConsumerServiceImpl
 * @projectName: gl-cloud-base
 * @date 2025-05-23 - 02:47
 */
@Component
@Slf4j
public class RabbitMqConsumerAPIServiceImpl implements RabbitMqConsumerAPIService {

    @Autowired
    private ConfigurableApplicationContext configurableContext;

    @Override
    public void rabbitConsumerRegister(String rabbitBeanName, RabbitMQConsumerConfigurationProperties rabbitMQListenerConfigurationProperties,
                                       RabbitMQListenerSimpleConfigurationProperties rabbitMQListenerSimpleConfigurationProperties,
                                       RabbitMQListenerSimpleRetryConfigurationProperties rabbitMQListenerSimpleRetryConfigurationProperties,
                                       RabbitMQListenerQueueProperties exchangeProperties) {

        RabbitAdmin rabbitAdmin = configurableContext.getBean(RabbitMqConsumerConstants.RABBITMQ_CONSUMER_RABBIT_ADMIN_NAME, RabbitAdmin.class);

        if(rabbitAdmin == null){

            rabbitConsumerConnectRegister(rabbitMQListenerConfigurationProperties);

        }

        rabbitListenerConnectRegister(rabbitBeanName, rabbitMQListenerSimpleConfigurationProperties, rabbitMQListenerSimpleRetryConfigurationProperties);

        rabbitMQConsumerQueuesRegister(exchangeProperties);

    }

    /**
     * 注册rabbit 得 连接
     * @param rabbitMQListenerConfigurationProperties
     */
    private void rabbitConsumerConnectRegister(RabbitMQConsumerConfigurationProperties rabbitMQListenerConfigurationProperties){

        configurableContext.getBeanFactory().registerSingleton(RabbitMqConsumerConstants.RABBITMQ_CONSUMER_CONNECT_FACTORY_NAME, RabbitMqConsumerBeanFactory.createConsumerConnectionFactory(rabbitMQListenerConfigurationProperties));

        configurableContext.getBeanFactory().registerSingleton(RabbitMqConsumerConstants.RABBITMQ_CONSUMER_RABBIT_ADMIN_NAME, RabbitMqConsumerBeanFactory.createRabbitAdmin(RabbitMqConsumerBeanFactory.createConsumerConnectionFactory(rabbitMQListenerConfigurationProperties)));

        RabbitListenerErrorHandler errorHandler = RabbitMqConsumerBeanFactory.createErrorHandler();

        configurableContext.getBeanFactory().registerSingleton("rabbitListenerErrorHandler", errorHandler);

    }

    /**
     * 注册rabbit 得listener
     * @param rabbitBeanName
     * @param rabbitMQListenerSimpleConfigurationProperties
     * @param rabbitMQListenerSimpleRetryConfigurationProperties
     */
    private void rabbitListenerConnectRegister(String rabbitBeanName, RabbitMQListenerSimpleConfigurationProperties rabbitMQListenerSimpleConfigurationProperties,
                                               RabbitMQListenerSimpleRetryConfigurationProperties rabbitMQListenerSimpleRetryConfigurationProperties){

        ConnectionFactory connectionFactory = configurableContext.getBean(RabbitMqConsumerConstants.RABBITMQ_CONSUMER_CONNECT_FACTORY_NAME, ConnectionFactory.class);

        RabbitListenerFactoryBuilder rabbitListenerFactoryBuilder = new RabbitListenerFactoryBuilder(rabbitMQListenerSimpleConfigurationProperties,
                rabbitMQListenerSimpleRetryConfigurationProperties);

        SimpleRabbitListenerContainerFactory simpleRabbitListenerContainerFactory = rabbitListenerFactoryBuilder.multiListenerContainer(connectionFactory);

        configurableContext.getBeanFactory().registerSingleton(rabbitBeanName, simpleRabbitListenerContainerFactory);

    }

    private void rabbitMQConsumerQueuesRegister(RabbitMQListenerQueueProperties exchangeProperties){

        Queue queue = new Queue(exchangeProperties.getQueuesName(), exchangeProperties.getDurable(),
                exchangeProperties.getExclusive(), exchangeProperties.getAutoDelete(), exchangeProperties.getArgs());

        RabbitAdmin rabbitAdmin = configurableContext.getBean(RabbitMqConsumerConstants.RABBITMQ_CONSUMER_RABBIT_ADMIN_NAME, RabbitAdmin.class);

        rabbitAdmin.declareQueue(queue);

        bindingQueueToExchange(rabbitAdmin, exchangeProperties, queue);

    }

    private void bindingQueueToExchange(RabbitAdmin rabbitAdmin, RabbitMQListenerQueueProperties rabbitMQListenerQueueProperties, Queue queue){

        Binding binding = null;

        binding = new Binding(queue.getName(), Binding.DestinationType.QUEUE,
                rabbitMQListenerQueueProperties.getExchangeName(), rabbitMQListenerQueueProperties.getRoutingkey(), null);

        rabbitAdmin.declareBinding(binding);

    }

}
