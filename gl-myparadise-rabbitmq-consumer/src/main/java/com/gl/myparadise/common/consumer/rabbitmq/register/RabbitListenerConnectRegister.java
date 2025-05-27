package com.gl.myparadise.common.consumer.rabbitmq.register;

import com.gl.myparadise.common.consumer.rabbitmq.config.RabbitMQListenerConfigurationProperties;
import com.gl.myparadise.common.consumer.rabbitmq.config.RabbitMQListenerSimpleConfigurationProperties;
import com.gl.myparadise.common.consumer.rabbitmq.config.RabbitMQListenerSimpleRetryConfigurationProperties;
import com.gl.myparadise.common.consumer.rabbitmq.constants.RabbitMqConsumerConstants;
import com.gl.myparadise.common.consumer.rabbitmq.error.DefaultRejectAndDontRequeueRecoverer;
import com.gl.myparadise.common.consumer.rabbitmq.error.RabbiteMQDefaultErrorHandler;
import com.gl.myparadise.common.consumer.rabbitmq.init.RabbitListenerFactoryBuilder;
import com.gl.myparadise.common.tools.util.ListUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aopalliance.aop.Advice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.interceptor.MethodInvocationRecoverer;
import org.springframework.retry.interceptor.RetryInterceptorBuilder;
import org.springframework.retry.interceptor.RetryOperationsInterceptor;
import org.springframework.retry.interceptor.StatefulRetryOperationsInterceptor;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.util.ErrorHandler;

import java.util.List;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: RabbitListenerConnectRegister
 * @ProjectName: gl-myparadise-common
 * @Date 2023-05-29 - 22:55
 */
@Configuration
@ConditionalOnBean(RabbitConsumerConnectRegister.class)
@ConditionalOnProperty(prefix = RabbitMqConsumerConstants.RABBITMQ_CONSUMER_LISTENER_CONFIG_TAG, name = "names")
@Slf4j
public class RabbitListenerConnectRegister implements BeanDefinitionRegistryPostProcessor, EnvironmentAware, ApplicationContextAware {

    private RabbitMQListenerSimpleConfigurationProperties rabbitMQListenerSimpleConfigurationProperties;

    private RabbitMQListenerSimpleRetryConfigurationProperties rabbitMQListenerSimpleRetryConfigurationProperties;

    private RabbitMQListenerConfigurationProperties rabbitMQListenerSimpleConfigProps;

    private ApplicationContext applicationContext;

    private Environment environment;

    private Binder binder;

    @Override
    public void setEnvironment(Environment environment) {

        this.environment = environment;

        binder = Binder.get(environment);

        this.rabbitMQListenerSimpleConfigProps
                = binder.bind((RabbitMqConsumerConstants.RABBITMQ_CONSUMER_LISTENER_CONFIG_TAG).trim(), RabbitMQListenerConfigurationProperties.class).get();

    }

    private SimpleRabbitListenerContainerFactory buildListenerConnectBean(String containerFactoryName) {

        if (StringUtils.isNotEmpty(containerFactoryName)) {

            try {

                this.rabbitMQListenerSimpleConfigurationProperties
                        = binder.bind((RabbitMqConsumerConstants.RABBITMQ_CONSUMER_LISTENER_CONFIG_TAG
                        + RabbitMqConsumerConstants.PROPERTIES_SPLIT_TAG
                        + containerFactoryName).trim(), RabbitMQListenerSimpleConfigurationProperties.class).get();

                rabbitMQListenerSimpleRetryConfigurationProperties = rabbitMQListenerSimpleConfigurationProperties.getRetry();

            } catch (Exception e) {

                log.error("mq listener pops file error", e);

            }

            if (rabbitMQListenerSimpleConfigurationProperties != null) {

                ConnectionFactory connectionFactory = applicationContext.getBean(RabbitMqConsumerConstants.RABBITMQ_CONSUMER_CONNECT_FACTORY_NAME, ConnectionFactory.class);

                RabbitListenerFactoryBuilder rabbitListenerFactoryBuilder = new RabbitListenerFactoryBuilder(rabbitMQListenerSimpleConfigurationProperties,
                        rabbitMQListenerSimpleRetryConfigurationProperties);

                return rabbitListenerFactoryBuilder.multiListenerContainer(connectionFactory);

            }

        }

        return null;

    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {

        if (rabbitMQListenerSimpleConfigProps != null) {

            List<String> containerFactoryNameList = rabbitMQListenerSimpleConfigProps.getNames();

            if (ListUtil.isNotEmpty(containerFactoryNameList)) {

                for (int i = 0; i < containerFactoryNameList.size(); i++) {

                    String containerFactoryName = containerFactoryNameList.get(i);

                    BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(SimpleRabbitListenerContainerFactory.class, () -> {

                        return buildListenerConnectBean(containerFactoryName);

                    });

                    BeanDefinition beanSqlSessionFactory = builder.getRawBeanDefinition();

                    beanDefinitionRegistry.registerBeanDefinition(containerFactoryName, beanSqlSessionFactory);

                }
            }

        }

    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        this.applicationContext = applicationContext;

    }
}
