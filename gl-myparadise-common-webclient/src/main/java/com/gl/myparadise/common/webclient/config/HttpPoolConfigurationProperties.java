package com.gl.myparadise.common.webclient.config;

import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: HttpPoolConfigurationProperties
 * @ProjectName: gl-common
 * @Date 2023-06-22 - 19:28
 */
@Configuration
public class HttpPoolConfigurationProperties {

    //SPRING 注入的BEAN NAME
    private String beanName;

    //连接池的最大连接数
    private int maxTotalConnect = 200 ;

    //客户端和服务器建立连接超时，默认2s
    private int connectTimeout = 2 * 1000;

    //指客户端从服务器读取数据包的间隔超时时间,不是总读取时间，默认30s
    private int readTimeout = 30;


    private int maxInMemorySize = 2 * 1024 * 1024;

    //等待队列大小
    private int pendingAcquireMaxCount = 400;

    //等待队列超时时间
    private int pendingAcquireTimeout = 60;

    //空闲时连接回收的时间,单位秒
    private int maxIdleTime = 20;

    //连接的生命周期时间,单位秒
    private int maxLifeTime = 60;

    //证书地址
    private String sslPath = null;

    private int writeTimeout = 30;

    //地址
    private String baseUrl = null;

    //是否是主链接池
    private Boolean isPrimary = false;

    private Map<String, String> headerMap = new HashMap<String, String>();

    private Map<String, String> cookieMap = new HashMap<String, String>();

    public Boolean getPrimary() {
        return isPrimary;
    }

    public void setPrimary(Boolean primary) {
        isPrimary = primary;
    }

    public Map<String, String> getHeaderMap() {
        return headerMap;
    }

    public void setHeaderMap(Map<String, String> headerMap) {
        this.headerMap = headerMap;
    }

    public Map<String, String> getCookieMap() {
        return cookieMap;
    }

    public void setCookieMap(Map<String, String> cookieMap) {
        this.cookieMap = cookieMap;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public int getWriteTimeout() {
        return writeTimeout;
    }

    public void setWriteTimeout(int writeTimeout) {
        this.writeTimeout = writeTimeout;
    }

    public String getSslPath() {
        return sslPath;
    }

    public void setSslPath(String sslPath) {
        this.sslPath = sslPath;
    }

    public int getMaxIdleTime() {
        return maxIdleTime;
    }

    public void setMaxIdleTime(int maxIdleTime) {
        this.maxIdleTime = maxIdleTime;
    }

    public int getMaxLifeTime() {
        return maxLifeTime;
    }

    public void setMaxLifeTime(int maxLifeTime) {
        this.maxLifeTime = maxLifeTime;
    }

    public int getPendingAcquireTimeout() {
        return pendingAcquireTimeout;
    }

    public void setPendingAcquireTimeout(int pendingAcquireTimeout) {
        this.pendingAcquireTimeout = pendingAcquireTimeout;
    }

    public int getPendingAcquireMaxCount() {
        return pendingAcquireMaxCount;
    }

    public void setPendingAcquireMaxCount(int pendingAcquireMaxCount) {
        this.pendingAcquireMaxCount = pendingAcquireMaxCount;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    public int getMaxTotalConnect() {
        return maxTotalConnect;
    }

    public void setMaxTotalConnect(int maxTotalConnect) {
        this.maxTotalConnect = maxTotalConnect;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public int getMaxInMemorySize() {
        return maxInMemorySize;
    }

    public void setMaxInMemorySize(int maxInMemorySize) {
        this.maxInMemorySize = maxInMemorySize;
    }
}
