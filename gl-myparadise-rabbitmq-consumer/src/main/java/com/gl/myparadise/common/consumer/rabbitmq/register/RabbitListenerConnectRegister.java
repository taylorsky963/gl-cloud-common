package com.gl.myparadise.common.consumer.rabbitmq.register;

import com.gl.myparadise.common.consumer.rabbitmq.config.RabbitMQListenerConfigurationProperties;
import com.gl.myparadise.common.consumer.rabbitmq.config.RabbitMQListenerSimpleConfigurationProperties;
import com.gl.myparadise.common.consumer.rabbitmq.config.RabbitMQListenerSimpleRetryConfigurationProperties;
import com.gl.myparadise.common.consumer.rabbitmq.constants.RabbitMqConsumerConstants;
import com.gl.myparadise.common.consumer.rabbitmq.error.DefaultRejectAndDontRequeueRecoverer;
import com.gl.myparadise.common.consumer.rabbitmq.error.RabbiteMQDefaultErrorHandler;
import com.gl.myparadise.common.tools.util.ListUtil;
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
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
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
@AutoConfigureAfter(RabbitConsumerConnectRegister.class)
@ConditionalOnProperty(prefix = RabbitMqConsumerConstants.RABBITMQ_CONSUMER_LISTENER_CONFIG_TAG, name = "names")
public class RabbitListenerConnectRegister implements BeanDefinitionRegistryPostProcessor, EnvironmentAware, ApplicationContextAware {

    private static final Logger logger = LoggerFactory.getLogger(RabbitListenerConnectRegister.class);

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

                logger.error("mq listener pops file error", e);

            }

            if (rabbitMQListenerSimpleConfigurationProperties != null) {

                return multiListenerContainer(containerFactoryName);

            }

        }

        return null;

    }


    private SimpleRabbitListenerContainerFactory multiListenerContainer(String containerFactoryName) {

        SimpleRabbitListenerContainerFactory factory = buildSimpleRabbitListenerContainerFactory();

        if (StringUtils.isNotEmpty(rabbitMQListenerSimpleConfigurationProperties.getAcknowledgeMode())) {

            if (AcknowledgeMode.AUTO.toString().equals(rabbitMQListenerSimpleConfigurationProperties.getAcknowledgeMode().toUpperCase())) {

                factory.setAcknowledgeMode(AcknowledgeMode.AUTO);

            } else if (AcknowledgeMode.MANUAL.toString().equals(rabbitMQListenerSimpleConfigurationProperties.getAcknowledgeMode().toUpperCase())) {

                factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);

            } else {

                factory.setAcknowledgeMode(AcknowledgeMode.NONE);

            }

        }

        return factory;

    }

    private SimpleRabbitListenerContainerFactory buildSimpleRabbitListenerContainerFactory() {

        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();

        factory.setConnectionFactory(applicationContext.getBean(RabbitMqConsumerConstants.RABBITMQ_CONSUMER_CONNECT_FACTORY_NAME, ConnectionFactory.class));

        factory.setMessageConverter(new Jackson2JsonMessageConverter());

        factory.setConcurrentConsumers(rabbitMQListenerSimpleConfigurationProperties.getConcurrency());

        factory.setMaxConcurrentConsumers(rabbitMQListenerSimpleConfigurationProperties.getMaxConcurrency());

        factory.setPrefetchCount(rabbitMQListenerSimpleConfigurationProperties.getPrefetch());

        factory.setReceiveTimeout(rabbitMQListenerSimpleConfigurationProperties.getReceiveTimeout());

        factory.setDefaultRequeueRejected(rabbitMQListenerSimpleConfigurationProperties.getDefaultRequeueRejected());

        factory.setIdleEventInterval(rabbitMQListenerSimpleConfigurationProperties.getIdleEventInterval());

        factory.setAutoStartup(rabbitMQListenerSimpleConfigurationProperties.getAutoStartup());

        if (rabbitMQListenerSimpleRetryConfigurationProperties != null
                && rabbitMQListenerSimpleRetryConfigurationProperties.getEnabled()) {

            MethodInvocationRecoverer methodInvocationRecoverer = null;

            try {

               methodInvocationRecoverer = (MethodInvocationRecoverer) Class.forName(rabbitMQListenerSimpleRetryConfigurationProperties.getRecoverer()).newInstance();

            } catch (Exception e) {

                logger.error("methodInvocationRecoverer is error", e);

                methodInvocationRecoverer = new DefaultRejectAndDontRequeueRecoverer();

            }

            if (rabbitMQListenerSimpleRetryConfigurationProperties.getStateless()) {

                factory.setAdviceChain(new Advice[]{getRetryOperationsInterceptor(methodInvocationRecoverer)});

            } else {

                factory.setAdviceChain(new Advice[]{getStatefulRetryOperationsInterceptor(methodInvocationRecoverer)});

            }

        }

        try {

            if (StringUtils.isNotEmpty(rabbitMQListenerSimpleConfigurationProperties.getErrorHandler())) {

                factory.setErrorHandler((ErrorHandler) Class.forName(rabbitMQListenerSimpleConfigurationProperties.getErrorHandler()).newInstance());

            }

        } catch (Exception e) {

            logger.error("create ErrorHandler newInstance is error", e);

            factory.setErrorHandler(new RabbiteMQDefaultErrorHandler());

        }

        return factory;
    }

    //设置重试的最大次数
    private SimpleRetryPolicy getRetryPolicy() {

        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();

        retryPolicy.setMaxAttempts(rabbitMQListenerSimpleRetryConfigurationProperties.getMaxAttempts());

        return retryPolicy;
    }

    private ExponentialBackOffPolicy getBackOffPolicy() {

        ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
        // 重试间隔
        backOffPolicy.setInitialInterval(rabbitMQListenerSimpleRetryConfigurationProperties.getInitialInterval());
        // 重试最大间隔
        backOffPolicy.setMaxInterval(rabbitMQListenerSimpleRetryConfigurationProperties.getMaxInterval());
        // 重试间隔乘法策略
        backOffPolicy.setMultiplier(rabbitMQListenerSimpleRetryConfigurationProperties.getMultiplier());

        return backOffPolicy;
    }


    private RetryOperationsInterceptor getRetryOperationsInterceptor(MethodInvocationRecoverer methodInvocationRecoverer) {

        return RetryInterceptorBuilder
                .stateless()
                //重试次数：包括第一次消费
                .recoverer(methodInvocationRecoverer)
                //重试间隔  指数递增时间参数 最大间隔时间  此处是指每一次重试时间间隔都是上一次的2倍  50000000：2^12*10000约为该数据
                .retryOperations(getRetryTemplate())
                .build();

    }

    private StatefulRetryOperationsInterceptor getStatefulRetryOperationsInterceptor(MethodInvocationRecoverer methodInvocationRecoverer) {

        return RetryInterceptorBuilder
                .stateful()
                //重试次数：包括第一次消费
                .recoverer(methodInvocationRecoverer)
                //重试间隔  指数递增时间参数 最大间隔时间  此处是指每一次重试时间间隔都是上一次的2倍  50000000：2^12*10000约为该数据
                .retryOperations(getRetryTemplate())
                .build();

    }

    private RetryTemplate getRetryTemplate() {

        RetryTemplate retryTemplate = new RetryTemplate();

        retryTemplate.setBackOffPolicy(getBackOffPolicy());

        retryTemplate.setRetryPolicy(getRetryPolicy());

        return retryTemplate;

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
