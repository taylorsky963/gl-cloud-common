package com.gl.myparadise.common.threadpool.config;

import com.gl.myparadise.common.threadpool.config.ThreadPoolInfoConfigurationProperties;

import java.io.Serializable;
import java.util.List;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: ThreadPoolConfigurationProperties
 * @ProjectName: gl-myparadise-common
 * @Date 2023-05-22 - 14:22
 */
public class ThreadPoolConfigurationProperties {

    private static final long serialVersionUID = -4448900081392491474L;

    private List<String> names;

    private ThreadPoolInfoConfigurationProperties threadPoolConfigProps;

    public List<String> getNames() {
        return names;
    }

    public void setNames(List<String> names) {
        this.names = names;
    }

    public ThreadPoolInfoConfigurationProperties getThreadPoolConfigProps() {
        return threadPoolConfigProps;
    }

    public void setThreadPoolConfigProps(ThreadPoolInfoConfigurationProperties threadPoolConfigProps) {
        this.threadPoolConfigProps = threadPoolConfigProps;
    }
}
