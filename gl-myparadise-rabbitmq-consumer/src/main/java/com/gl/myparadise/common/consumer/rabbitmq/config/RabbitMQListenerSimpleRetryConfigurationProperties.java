package com.gl.myparadise.common.consumer.rabbitmq.config;

import com.gl.myparadise.common.consumer.rabbitmq.constants.RabbitMqConsumerConstants;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: RabbitMQListenerSimpleRetryConfigurationProperties
 * @ProjectName: gl-myparadise-common
 * @Date 2023-05-29 - 16:56
 */
public class RabbitMQListenerSimpleRetryConfigurationProperties {

    //监听重试是否可用
    private Boolean enabled = false;
    //最大重试次数
    private Integer maxAttempts = 3;
    //第一次和第二次尝试发布或传递消息之间的间隔
    private Integer initialInterval = 5000;
    //应用于上一重试间隔的乘数
    private Integer multiplier = 2;
    //最大重试时间间隔
    private Integer maxInterval = 30000;
    //重试是有状态or无状态
    private boolean stateless = true;
    //最大次数后的处理类
    private String recoverer = RabbitMqConsumerConstants.RABBITMQ_CONSUMER_LISTENER_RETRY_RECOVERER;

    public String getRecoverer() {
        return recoverer;
    }

    public void setRecoverer(String recoverer) {
        this.recoverer = recoverer;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Integer getMaxAttempts() {
        return maxAttempts;
    }

    public void setMaxAttempts(Integer maxAttempts) {
        this.maxAttempts = maxAttempts;
    }

    public Integer getInitialInterval() {
        return initialInterval;
    }

    public void setInitialInterval(Integer initialInterval) {
        this.initialInterval = initialInterval;
    }

    public Integer getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(Integer multiplier) {
        this.multiplier = multiplier;
    }

    public Integer getMaxInterval() {
        return maxInterval;
    }

    public void setMaxInterval(Integer maxInterval) {
        this.maxInterval = maxInterval;
    }

    public boolean getStateless() {
        return stateless;
    }

    public void setStateless(boolean stateless) {
        this.stateless = stateless;
    }
}
