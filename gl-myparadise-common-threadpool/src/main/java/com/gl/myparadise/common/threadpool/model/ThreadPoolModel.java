package com.gl.myparadise.common.threadpool.model;

import com.gl.myparadise.common.threadpool.config.ThreadPoolInfoConfigurationProperties;

import java.io.Serializable;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: ThreadPoolConfigurationModel
 * @ProjectName: gl-myparadise-common
 * @Date 2023-05-21 - 23:03
 */
public class ThreadPoolModel implements Serializable  {

    private static final long serialVersionUID = 6820713091869971179L;

    private String threadPoolName;

    private String threadPoolBeanName;

    public String getThreadPoolBeanName() {
        return threadPoolBeanName;
    }

    public void setThreadPoolBeanName(String threadPoolBeanName) {
        this.threadPoolBeanName = threadPoolBeanName;
    }

    public String getThreadPoolName() {
        return threadPoolName;
    }

    public void setThreadPoolName(String threadPoolName) {
        this.threadPoolName = threadPoolName;
    }

}
