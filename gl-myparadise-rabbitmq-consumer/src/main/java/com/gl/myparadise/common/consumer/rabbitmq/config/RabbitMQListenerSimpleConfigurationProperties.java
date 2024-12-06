package com.gl.myparadise.common.consumer.rabbitmq.config;

import com.gl.myparadise.common.consumer.rabbitmq.constants.RabbitMqConsumerConstants;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: RabbitMQListenerSimpleConfigurationProperties
 * @ProjectName: gl-myparadise-common
 * @Date 2023-05-26 - 1:32
 */
public class RabbitMQListenerSimpleConfigurationProperties {

    private Long receiveTimeout;

    //最小的消费者数量
    private Integer concurrency;

    //指定一个请求能处理多少个消息，如果有事务的话，必须大于等于transaction数量.
    private Integer prefetch;

    //最大的消费者数量
    private Integer maxConcurrency;

    //表示消息确认方式，其有三种配置方式，分别是none、manual和auto；默认NONE
    private String acknowledgeMode;

    //决定被拒绝的消息是否重新入队；默认是true（与参数acknowledge-mode有关系）
    private Boolean defaultRequeueRejected;

    //多少长时间发布空闲容器时间，单位毫秒
    private Long idleEventInterval;

    //是否启动时自动启动容器
    private Boolean autoStartup;

    private String errorHandler;

    private RabbitMQListenerSimpleRetryConfigurationProperties retry;

    public RabbitMQListenerSimpleRetryConfigurationProperties getRetry() {
        return retry;
    }

    public void setRetry(RabbitMQListenerSimpleRetryConfigurationProperties retry) {
        this.retry = retry;
    }

    public Long getReceiveTimeout() {
        return receiveTimeout;
    }

    public void setReceiveTimeout(Long receiveTimeout) {
        this.receiveTimeout = receiveTimeout;
    }

    public Integer getConcurrency() {
        return concurrency;
    }

    public void setConcurrency(Integer concurrency) {
        this.concurrency = concurrency;
    }

    public Integer getPrefetch() {
        return prefetch;
    }

    public void setPrefetch(Integer prefetch) {
        this.prefetch = prefetch;
    }

    public Integer getMaxConcurrency() {
        return maxConcurrency;
    }

    public void setMaxConcurrency(Integer maxConcurrency) {
        this.maxConcurrency = maxConcurrency;
    }

    public String getAcknowledgeMode() {
        return acknowledgeMode;
    }

    public void setAcknowledgeMode(String acknowledgeMode) {
        this.acknowledgeMode = acknowledgeMode;
    }

    public Boolean getDefaultRequeueRejected() {
        return defaultRequeueRejected;
    }

    public void setDefaultRequeueRejected(Boolean defaultRequeueRejected) {
        this.defaultRequeueRejected = defaultRequeueRejected;
    }

    public Long getIdleEventInterval() {
        return idleEventInterval;
    }

    public void setIdleEventInterval(Long idleEventInterval) {
        this.idleEventInterval = idleEventInterval;
    }

    public Boolean getAutoStartup() {
        return autoStartup;
    }

    public void setAutoStartup(Boolean autoStartup) {
        this.autoStartup = autoStartup;
    }

    public String getErrorHandler() {
        return errorHandler;
    }

    public void setErrorHandler(String errorHandler) {
        this.errorHandler = errorHandler;
    }
}
