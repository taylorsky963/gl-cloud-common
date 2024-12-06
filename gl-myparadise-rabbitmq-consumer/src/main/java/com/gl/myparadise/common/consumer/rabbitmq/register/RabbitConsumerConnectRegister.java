package com.gl.myparadise.common.consumer.rabbitmq.register;

import com.gl.myparadise.common.consumer.rabbitmq.config.RabbitMQConsumerConfigurationProperties;
import com.gl.myparadise.common.consumer.rabbitmq.config.RabbitMQListenerConfigurationProperties;
import com.gl.myparadise.common.consumer.rabbitmq.constants.RabbitMqConsumerConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.api.RabbitListenerErrorHandler;
import org.springframework.amqp.rabbit.support.ListenerExecutionFailedException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: RabbitConsumerConnectRegister
 * @ProjectName: gl-myparadise-common
 * @Date 2023-05-26 - 1:38
 */
@Configuration
public class RabbitConsumerConnectRegister implements BeanDefinitionRegistryPostProcessor, EnvironmentAware {

    private static final Logger logger = LoggerFactory.getLogger(RabbitConsumerConnectRegister.class);

    private Environment environment;

    private Binder binder;

    private RabbitMQConsumerConfigurationProperties rabbitMQListenerConfigurationProperties;

    @Override
    public void setEnvironment(Environment environment) {

        this.environment = environment;

        binder = Binder.get(environment);

        this.rabbitMQListenerConfigurationProperties
                = binder.bind((RabbitMqConsumerConstants.RABBITMQ_CONSUMER_CONFIG_TAG).trim(), RabbitMQConsumerConfigurationProperties.class).get();


    }

    private ConnectionFactory connectionFactoryListener() {

        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();

        connectionFactory.setUsername(rabbitMQListenerConfigurationProperties.getUsername());

        connectionFactory.setPassword(rabbitMQListenerConfigurationProperties.getPassword());

        connectionFactory.setVirtualHost(rabbitMQListenerConfigurationProperties.getVirtualhost());

        connectionFactory.setRequestedHeartBeat(rabbitMQListenerConfigurationProperties.getRequestedHeartbeat());

        connectionFactory.setConnectionTimeout(rabbitMQListenerConfigurationProperties.getConnectiontimeout());

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

    @Bean
    public RabbitListenerErrorHandler rabbitListenerErrorHandler(){
        return new RabbitListenerErrorHandler() {
            @Override
            public Object handleError(Message amqpMessage, org.springframework.messaging.Message<?> message, ListenerExecutionFailedException exception) throws Exception {

                logger.error("rabbitListenerErrorHandler : " + message, exception);

                throw exception;
            }
        };

    }


    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {

        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(ConnectionFactory.class, () -> {
            // 这个方法可以定义这个bean的实例创建方式，如构造函数之后还想调用其他的方法，也可以在这里做
            return connectionFactoryListener();
        });

        BeanDefinition beanDefinition = beanDefinitionBuilder.getRawBeanDefinition();

        beanDefinition.setPrimary(true);
        //注册bean定义
        beanDefinitionRegistry.registerBeanDefinition(RabbitMqConsumerConstants.RABBITMQ_CONSUMER_CONNECT_FACTORY_NAME, beanDefinition);

    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

    }
}
