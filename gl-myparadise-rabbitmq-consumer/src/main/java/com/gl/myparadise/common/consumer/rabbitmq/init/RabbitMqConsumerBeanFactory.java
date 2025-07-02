package com.gl.myparadise.common.consumer.rabbitmq.init;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gl.myparadise.common.consumer.rabbitmq.config.RabbitMQConsumerConfigurationProperties;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.api.RabbitListenerErrorHandler;
import org.springframework.amqp.rabbit.support.ListenerExecutionFailedException;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import lombok.extern.slf4j.Slf4j;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

/**
 * @author Vincent
 * @description:
 * @className: RabbitMqConsumerBeanFactory
 * @projectName: gl-cloud-base
 * @date 2025-05-22 - 22:35
 */
@Slf4j
public class RabbitMqConsumerBeanFactory {

    public static ConnectionFactory createConsumerConnectionFactory(RabbitMQConsumerConfigurationProperties rabbitMQListenerConfigurationProperties) {

        com.rabbitmq.client.ConnectionFactory rabbitFactory = new com.rabbitmq.client.ConnectionFactory();

        rabbitFactory.setUsername(rabbitMQListenerConfigurationProperties.getUsername());

        rabbitFactory.setPassword(rabbitMQListenerConfigurationProperties.getPassword());

        rabbitFactory.setVirtualHost(rabbitMQListenerConfigurationProperties.getVirtualhost());

        rabbitFactory.setConnectionTimeout(rabbitMQListenerConfigurationProperties.getConnectiontimeout());

        if (rabbitMQListenerConfigurationProperties.isSsl()) {

            try {

                rabbitFactory.useSslProtocol(); // 自动使用默认 SSLContext（如 AWS MQ）

            } catch (NoSuchAlgorithmException e) {

                throw new RuntimeException(e);

            } catch (KeyManagementException e) {

                throw new RuntimeException(e);
            }
        }

        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(rabbitFactory);

        connectionFactory.setRequestedHeartBeat(rabbitMQListenerConfigurationProperties.getRequestedHeartbeat());

        if(StringUtils.isNotEmpty(rabbitMQListenerConfigurationProperties.getCacheMode())){

            if(CachingConnectionFactory.CacheMode.CHANNEL.toString().equals(rabbitMQListenerConfigurationProperties.getCacheMode())){

                connectionFactory.setCacheMode(CachingConnectionFactory.CacheMode.CHANNEL);

            }else if(CachingConnectionFactory.CacheMode.CONNECTION.toString().equals(rabbitMQListenerConfigurationProperties.getCacheMode())){

                connectionFactory.setCacheMode(CachingConnectionFactory.CacheMode.CONNECTION);

            }

        }

        connectionFactory.setChannelCacheSize(rabbitMQListenerConfigurationProperties.getChannelCachesize());

        connectionFactory.setChannelCheckoutTimeout(rabbitMQListenerConfigurationProperties.getChannelCacheCheckoutTimeout());

        //该方法配置多个host，在当前连接host down掉的时候会自动去重连后面的host
        connectionFactory.setAddresses(rabbitMQListenerConfigurationProperties.getHostAddress());

        return connectionFactory;

    }

    public static RabbitAdmin createRabbitAdmin(ConnectionFactory connectionFactory) {

        RabbitAdmin admin = new RabbitAdmin(connectionFactory);

        ObjectMapper mapper = new ObjectMapper();

        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        admin.getRabbitTemplate().setMessageConverter(new Jackson2JsonMessageConverter(mapper));

        return admin;

    }


    public static RabbitListenerErrorHandler createErrorHandler() {
        return new RabbitListenerErrorHandler() {
            @Override
            public Object handleError(Message amqpMessage, org.springframework.messaging.Message<?> message, ListenerExecutionFailedException exception) throws Exception {

                log.error("rabbitListenerErrorHandler : " + message, exception);

                throw exception;
            }
        };
    }
}
