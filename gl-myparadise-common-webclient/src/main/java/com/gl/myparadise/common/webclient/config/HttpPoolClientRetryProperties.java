package com.gl.myparadise.common.webclient.config;

/**
 * @author Vincent
 * @description:
 * @className: HttpPoolCilentRetryProperties
 * @projectName: gl-cloud-base
 * @date 2025-04-02 - 02:15
 */
public class HttpPoolClientRetryProperties {

    private String charset = "UTF-8";

    //重试次数,默认2次
    private int retryTimes = 2;

    //第一次和第二次尝试发布或传递消息之间的间隔
    private int retryInitialInterval = 5;

    //应用于上一重试间隔的乘数
    private int retryMultiplier = 2;

    //最大重试时间间隔
    private int retryMaxInterval = 60;

    public int getRetryInitialInterval() {
        return retryInitialInterval;
    }

    public void setRetryInitialInterval(int retryInitialInterval) {
        this.retryInitialInterval = retryInitialInterval;
    }

    public int getRetryMultiplier() {
        return retryMultiplier;
    }

    public void setRetryMultiplier(int retryMultiplier) {
        this.retryMultiplier = retryMultiplier;
    }

    public int getRetryMaxInterval() {
        return retryMaxInterval;
    }

    public void setRetryMaxInterval(int retryMaxInterval) {
        this.retryMaxInterval = retryMaxInterval;
    }

    //设置长连接保持时间,单位 s
    private int keepAliveTime = 60;

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public int getRetryTimes() {
        return retryTimes;
    }

    public void setRetryTimes(int retryTimes) {
        this.retryTimes = retryTimes;
    }

    public int getKeepAliveTime() {
        return keepAliveTime;
    }

    public void setKeepAliveTime(int keepAliveTime) {
        this.keepAliveTime = keepAliveTime;
    }
}
