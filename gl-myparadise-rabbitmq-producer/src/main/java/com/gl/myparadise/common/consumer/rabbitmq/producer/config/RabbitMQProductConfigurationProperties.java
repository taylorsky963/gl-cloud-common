package com.gl.myparadise.common.consumer.rabbitmq.producer.config;

import com.gl.myparadise.common.consumer.rabbitmq.producer.constants.RabbitMqProducerConstants;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: RabbitMQConfigurationProperties
 * @ProjectName: gl-myparadise-common
 * @Date 2023-05-26 - 0:48
 */
@Configuration
@ConfigurationProperties(prefix = RabbitMqProducerConstants.RABBITMQ_PRODUCT_CONFIG_TAG)
public class RabbitMQProductConfigurationProperties {

    private String hostAddress;

    private String username;

    private String password;

    private String virtualhost;

    private int connectiontimeout = 60000;

    private String confirmCallback;

    private String returnCallback;

    private boolean templatemandatory;

    private int channelCachesize = 0;

    private long channelCacheCheckoutTimeout = 0L;

    private String publisherConfirmsType = "NONE";

    private boolean publisherReturns = false;

    private String cacheMode;

    private int requestedHeartbeat = 60;

    private boolean ssl = false;

    public boolean isSsl() {
        return ssl;
    }

    public void setSsl(boolean ssl) {
        this.ssl = ssl;
    }

    public String getHostAddress() {
        return hostAddress;
    }

    public void setHostAddress(String hostAddress) {
        this.hostAddress = hostAddress;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getVirtualhost() {
        return virtualhost;
    }

    public void setVirtualhost(String virtualhost) {
        this.virtualhost = virtualhost;
    }

    public int getConnectiontimeout() {
        return connectiontimeout;
    }

    public void setConnectiontimeout(int connectiontimeout) {
        this.connectiontimeout = connectiontimeout;
    }

    public boolean isTemplatemandatory() {
        return templatemandatory;
    }

    public void setTemplatemandatory(boolean templatemandatory) {
        this.templatemandatory = templatemandatory;
    }

    public String getConfirmCallback() {
        return confirmCallback;
    }

    public void setConfirmCallback(String confirmCallback) {
        this.confirmCallback = confirmCallback;
    }

    public String getReturnCallback() {
        return returnCallback;
    }

    public void setReturnCallback(String returnCallback) {
        this.returnCallback = returnCallback;
    }

    public int getChannelCachesize() {
        return channelCachesize;
    }

    public void setChannelCachesize(int channelCachesize) {
        this.channelCachesize = channelCachesize;
    }

    public long getChannelCacheCheckoutTimeout() {
        return channelCacheCheckoutTimeout;
    }

    public void setChannelCacheCheckoutTimeout(long channelCacheCheckoutTimeout) {
        this.channelCacheCheckoutTimeout = channelCacheCheckoutTimeout;
    }

    public String getPublisherConfirmsType() {
        return publisherConfirmsType;
    }

    public void setPublisherConfirmsType(String publisherConfirmsType) {
        this.publisherConfirmsType = publisherConfirmsType;
    }

    public boolean getPublisherReturns() {
        return publisherReturns;
    }

    public void setPublisherReturns(boolean publisherReturns) {
        this.publisherReturns = publisherReturns;
    }

    public int getRequestedHeartbeat() {
        return requestedHeartbeat;
    }

    public void setRequestedHeartbeat(int requestedHeartbeat) {
        this.requestedHeartbeat = requestedHeartbeat;
    }


    public String getCacheMode() {
        return cacheMode;
    }

    public void setCacheMode(String cacheMode) {
        this.cacheMode = cacheMode;
    }

}
