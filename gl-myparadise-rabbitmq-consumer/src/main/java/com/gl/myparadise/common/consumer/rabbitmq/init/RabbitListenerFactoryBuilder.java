package com.gl.myparadise.common.consumer.rabbitmq.init;

import com.gl.myparadise.common.consumer.rabbitmq.config.RabbitMQListenerSimpleConfigurationProperties;
import com.gl.myparadise.common.consumer.rabbitmq.config.RabbitMQListenerSimpleRetryConfigurationProperties;
import com.gl.myparadise.common.consumer.rabbitmq.error.DefaultRejectAndDontRequeueRecoverer;
import com.gl.myparadise.common.consumer.rabbitmq.error.RabbiteMQDefaultErrorHandler;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.aop.Advice;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.interceptor.MethodInvocationRecoverer;
import org.springframework.retry.interceptor.RetryInterceptorBuilder;
import org.springframework.retry.interceptor.RetryOperationsInterceptor;
import org.springframework.retry.interceptor.StatefulRetryOperationsInterceptor;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.util.ErrorHandler;

/**
 * @author Vincent
 * @description:
 * @className: RabbitListenerFactoryBuilder
 * @projectName: gl-cloud-base
 * @date 2025-05-22 - 23:09
 */
@Slf4j
public class RabbitListenerFactoryBuilder {

    private RabbitMQListenerSimpleConfigurationProperties rabbitMQListenerSimpleConfigurationProperties;

    private RabbitMQListenerSimpleRetryConfigurationProperties rabbitMQListenerSimpleRetryConfigurationProperties;

    public RabbitListenerFactoryBuilder(RabbitMQListenerSimpleConfigurationProperties rabbitMQListenerProperties,
                                        RabbitMQListenerSimpleRetryConfigurationProperties rabbitMQListenerRetryProperties) {

        rabbitMQListenerSimpleConfigurationProperties = rabbitMQListenerProperties;

        rabbitMQListenerSimpleRetryConfigurationProperties = rabbitMQListenerRetryProperties;

    }

    public SimpleRabbitListenerContainerFactory multiListenerContainer(ConnectionFactory connectionFactory) {

        SimpleRabbitListenerContainerFactory factory = buildSimpleRabbitListenerContainerFactory(connectionFactory);

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

    private SimpleRabbitListenerContainerFactory buildSimpleRabbitListenerContainerFactory(ConnectionFactory connectionFactory) {

        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();

        factory.setConnectionFactory(connectionFactory);

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

                log.error("methodInvocationRecoverer is error", e);

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

            log.error("create ErrorHandler newInstance is error", e);

            factory.setErrorHandler(new RabbiteMQDefaultErrorHandler());

        }

        return factory;
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

}
