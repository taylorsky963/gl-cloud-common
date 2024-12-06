package com.gl.myparadise.common.consumer.rabbitmq.config;

import com.gl.myparadise.common.consumer.rabbitmq.constants.RabbitMqConsumerConstants;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: RabbitMQConsumerConfigurationProperties
 * @ProjectName: gl-myparadise-common
 * @Date 2023-05-29 - 16:35
 */
public class RabbitMQConsumerConfigurationProperties {

    private String hostAddress;

    private String username;

    private String password;

    private String virtualhost;

    private int connectiontimeout = 60000;

    private int requestedHeartbeat = 60;

    private String cacheMode;

    private int channelCachesize = 0;

    private long channelCacheCheckoutTimeout = 0L;

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
}
