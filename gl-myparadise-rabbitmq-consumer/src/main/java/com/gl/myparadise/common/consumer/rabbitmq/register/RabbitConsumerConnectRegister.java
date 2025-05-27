package com.gl.myparadise.common.consumer.rabbitmq.register;

import com.gl.myparadise.common.consumer.rabbitmq.config.RabbitMQConsumerConfigurationProperties;
import com.gl.myparadise.common.consumer.rabbitmq.constants.RabbitMqConsumerConstants;
import com.gl.myparadise.common.consumer.rabbitmq.init.RabbitMqConsumerBeanFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.api.RabbitListenerErrorHandler;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: RabbitConsumerConnectRegister
 * @ProjectName: gl-myparadise-common
 * @Date 2023-05-26 - 1:38
 */
@Configuration
@Slf4j
public class RabbitConsumerConnectRegister implements BeanDefinitionRegistryPostProcessor, EnvironmentAware {

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

    @Bean
    public RabbitListenerErrorHandler rabbitListenerErrorHandler(){

        return RabbitMqConsumerBeanFactory.createErrorHandler();

    }


    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {

        // 注册 ConnectionFactory
        beanDefinitionRegistry.registerBeanDefinition(
                RabbitMqConsumerConstants.RABBITMQ_CONSUMER_CONNECT_FACTORY_NAME,
                BeanDefinitionBuilder.genericBeanDefinition(ConnectionFactory.class,
                        () -> RabbitMqConsumerBeanFactory.createConsumerConnectionFactory(rabbitMQListenerConfigurationProperties)).getBeanDefinition());

        // 注册 RabbitAdmin
        beanDefinitionRegistry.registerBeanDefinition(
                RabbitMqConsumerConstants.RABBITMQ_CONSUMER_RABBIT_ADMIN_NAME,
                BeanDefinitionBuilder.genericBeanDefinition(RabbitAdmin.class,
                        () -> RabbitMqConsumerBeanFactory.createRabbitAdmin(RabbitMqConsumerBeanFactory.createConsumerConnectionFactory(rabbitMQListenerConfigurationProperties))).getBeanDefinition());

    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

    }
}
