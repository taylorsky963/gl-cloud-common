package com.gl.myparadise.common.threadpool.config;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: ThreadPoolInfoConfigurationProperties
 * @ProjectName: gl-myparadise-common
 * @Date 2023-05-22 - 14:37
 */
public class ThreadPoolInfoConfigurationProperties {

    private String beanName;
    //核心线程数量
    private int corePoolSize = 1;
    //最大线程数量
    private int maxPoolSize = 10;
    //如果线程数大于corePoolSize, 空闲时间超过keepAliveSeconds, 线程将被回收
    private int keepAliveSeconds = 60;
    //等待队列的数量，如果线程数等于corePoolSize ，并且队列满了，才会创建新的线程
    private int queueCapacity = 5;
    //线程池中的线程的名称前缀
    private String threadNamePrefix = "glTheadPool";
    //核心线程是否空闲超时销毁，默认是FALSE, 直到线程数量为0
    private boolean allowCoreThreadTimeOut = false;
    //关机时是否等待任务结束， 其实没啥用，基本都是KILL -9 关机或者DOCKER容器直接销毁
    private boolean waitForTasksToCompleteOnShutdown = false;
    //和waitForTasksToCompleteOnShutdown 配合使用， 就是等待时间
    private Long awaitTerminationMillis = 60L;

    /*AbortPolicy策略：默认策略，如果线程池队列满了丢掉这个任务并且抛出RejectedExecutionException异常。
    DiscardPolicy策略：如果线程池队列满了，会直接丢掉这个任务并且不会有任何异常。
    DiscardOldestPolicy策略：如果队列满了，会将最早进入队列的任务删掉腾出空间，再尝试加入队列。
    CallerRunsPolicy策略：如果添加到线程池失败，那么主线程会自己去执行该任务，不会等待线程池中的线程去执行。
    */
    private String rejectedHandler;

    public int getCorePoolSize() {
        return corePoolSize;
    }

    public void setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    public void setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public int getKeepAliveSeconds() {
        return keepAliveSeconds;
    }

    public void setKeepAliveSeconds(int keepAliveSeconds) {
        this.keepAliveSeconds = keepAliveSeconds;
    }

    public int getQueueCapacity() {
        return queueCapacity;
    }

    public void setQueueCapacity(int queueCapacity) {
        this.queueCapacity = queueCapacity;
    }

    public String getThreadNamePrefix() {
        return threadNamePrefix;
    }

    public void setThreadNamePrefix(String threadNamePrefix) {
        this.threadNamePrefix = threadNamePrefix;
    }

    public boolean isAllowCoreThreadTimeOut() {
        return allowCoreThreadTimeOut;
    }

    public void setAllowCoreThreadTimeOut(boolean allowCoreThreadTimeOut) {
        this.allowCoreThreadTimeOut = allowCoreThreadTimeOut;
    }

    public boolean isWaitForTasksToCompleteOnShutdown() {
        return waitForTasksToCompleteOnShutdown;
    }

    public void setWaitForTasksToCompleteOnShutdown(boolean waitForTasksToCompleteOnShutdown) {
        this.waitForTasksToCompleteOnShutdown = waitForTasksToCompleteOnShutdown;
    }

    public Long getAwaitTerminationMillis() {
        return awaitTerminationMillis;
    }

    public void setAwaitTerminationMillis(Long awaitTerminationMillis) {
        this.awaitTerminationMillis = awaitTerminationMillis;
    }

    public String getRejectedHandler() {
        return rejectedHandler;
    }

    public void setRejectedHandler(String rejectedHandler) {
        this.rejectedHandler = rejectedHandler;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }
}
