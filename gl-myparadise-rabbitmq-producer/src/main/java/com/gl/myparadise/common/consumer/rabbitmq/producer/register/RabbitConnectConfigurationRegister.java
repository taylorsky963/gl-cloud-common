package com.gl.myparadise.common.consumer.rabbitmq.producer.register;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gl.myparadise.common.consumer.rabbitmq.producer.config.RabbitMQProductConfigurationProperties;
import com.gl.myparadise.common.consumer.rabbitmq.producer.constants.RabbitMqProducerConstants;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: RabbitConnectConfigurationRegister
 * @ProjectName: gl-myparadise-common
 * @Date 2023-05-26 - 1:55
 */
@Configuration
@EnableConfigurationProperties({RabbitMQProductConfigurationProperties.class})
@ConditionalOnProperty(prefix = RabbitMqProducerConstants.RABBITMQ_PRODUCT_BUSINESSCODE_CONFIG_TAG, name = "names")
public class RabbitConnectConfigurationRegister{

    private static final Logger logger = LoggerFactory.getLogger(RabbitConnectConfigurationRegister.class);

    @Autowired
    RabbitMQProductConfigurationProperties rabbitMQProductConfigurationProperties;


    @Bean(name="connectionFactory")
    public CachingConnectionFactory connectionFactory() {

        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();

        connectionFactory.setUsername(rabbitMQProductConfigurationProperties.getUsername());

        connectionFactory.setPassword(rabbitMQProductConfigurationProperties.getPassword());

        connectionFactory.setVirtualHost(rabbitMQProductConfigurationProperties.getVirtualhost());

        connectionFactory.setConnectionTimeout(rabbitMQProductConfigurationProperties.getConnectiontimeout());

        if(StringUtils.isNotEmpty(rabbitMQProductConfigurationProperties.getPublisherConfirmsType())){

            if(CachingConnectionFactory.ConfirmType.CORRELATED.equals(rabbitMQProductConfigurationProperties.getPublisherConfirmsType().toUpperCase())){

                connectionFactory.setPublisherConfirmType(CachingConnectionFactory.ConfirmType.CORRELATED);

            }else if(CachingConnectionFactory.ConfirmType.SIMPLE.equals(rabbitMQProductConfigurationProperties.getPublisherConfirmsType().toUpperCase())){

                connectionFactory.setPublisherConfirmType(CachingConnectionFactory.ConfirmType.SIMPLE);

            }else{

                connectionFactory.setPublisherConfirmType(CachingConnectionFactory.ConfirmType.NONE);

            }

        }


        connectionFactory.setPublisherReturns(rabbitMQProductConfigurationProperties.getPublisherReturns());

        connectionFactory.setRequestedHeartBeat(rabbitMQProductConfigurationProperties.getRequestedHeartbeat());

        if(StringUtils.isNotEmpty(rabbitMQProductConfigurationProperties.getCacheMode())){

            if(CachingConnectionFactory.CacheMode.CHANNEL.equals(rabbitMQProductConfigurationProperties.getCacheMode().toUpperCase())){

                connectionFactory.setCacheMode(CachingConnectionFactory.CacheMode.CHANNEL);

            }else if(CachingConnectionFactory.CacheMode.CONNECTION.equals(rabbitMQProductConfigurationProperties.getCacheMode().toUpperCase())){

                connectionFactory.setCacheMode(CachingConnectionFactory.CacheMode.CONNECTION);

            }

        }

        connectionFactory.setChannelCacheSize(rabbitMQProductConfigurationProperties.getChannelCachesize());

        connectionFactory.setChannelCheckoutTimeout(rabbitMQProductConfigurationProperties.getChannelCacheCheckoutTimeout());

        //该方法配置多个host，在当前连接host down掉的时候会自动去重连后面的host
        connectionFactory.setAddresses(rabbitMQProductConfigurationProperties.getHostAddress());

        return connectionFactory;

    }

    @Bean(name="rabbitAdmin")
    public RabbitAdmin rabbitAdmin(){
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory());

        rabbitAdmin.getRabbitTemplate().setMessageConverter(new Jackson2JsonMessageConverter(mapper));

        try {

            if (rabbitMQProductConfigurationProperties.getReturnCallback() != null) {

                rabbitAdmin.getRabbitTemplate().setMandatory(true);

                rabbitAdmin.getRabbitTemplate().setReturnCallback((RabbitTemplate.ReturnCallback) Class.forName(rabbitMQProductConfigurationProperties.getReturnCallback()).newInstance());

            }

            if (rabbitMQProductConfigurationProperties.getConfirmCallback() != null) {

                rabbitAdmin.getRabbitTemplate().setConfirmCallback((RabbitTemplate.ConfirmCallback) Class.forName(rabbitMQProductConfigurationProperties.getConfirmCallback()).newInstance());

            }

        }catch (Exception ex){

            logger.error("rabbitAdmin init", ex);
        }

        return rabbitAdmin;

    }


}
