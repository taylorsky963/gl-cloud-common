package com.gl.myparadise.common.threadpool.model;

import java.io.Serializable;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: ThreadPoolMonitorModel
 * @ProjectName: gl-myparadise-common
 * @Date 2023-05-23 - 20:45
 */
public class ThreadPoolMonitorModel implements Serializable {

    private static final long serialVersionUID = 2364290923362207910L;

    private String threadPoolName;

    //最短执行时间
    private long minCostTime = 0L;

    //最长执行时间
    private long maxCostTime = 0L;

    private long averageCostTime = 0L;

    //核心线程数量
    private int corePoolSize = 0;

    //曾经最大线程数字
    private int largestPoolSize = 0;

    //最大线程数字
    private int maximumPoolSize = 0;

    //目前工作的线程数字
    private int poolSize = 0;

    //目前等待队列的数
    private int queueSize = 0;

    //目前正在处理任务的线程数
    private int activeCount;

    //已经完成的任务数
    private long completedTaskCount = 0L;

    //总任务数，包括未完成等待的
    private long taskCount = 0L;

    private long rejectedTaskCount = 0L;

    public String getThreadPoolName() {
        return threadPoolName;
    }

    public void setThreadPoolName(String threadPoolName) {
        this.threadPoolName = threadPoolName;
    }

    public long getMinCostTime() {
        return minCostTime;
    }

    public void setMinCostTime(long minCostTime) {
        this.minCostTime = minCostTime;
    }

    public long getMaxCostTime() {
        return maxCostTime;
    }

    public void setMaxCostTime(long maxCostTime) {
        this.maxCostTime = maxCostTime;
    }

    public long getAverageCostTime() {
        return averageCostTime;
    }

    public void setAverageCostTime(long averageCostTime) {
        this.averageCostTime = averageCostTime;
    }

    public int getCorePoolSize() {
        return corePoolSize;
    }

    public void setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public int getLargestPoolSize() {
        return largestPoolSize;
    }

    public void setLargestPoolSize(int largestPoolSize) {
        this.largestPoolSize = largestPoolSize;
    }

    public int getMaximumPoolSize() {
        return maximumPoolSize;
    }

    public void setMaximumPoolSize(int maximumPoolSize) {
        this.maximumPoolSize = maximumPoolSize;
    }

    public int getPoolSize() {
        return poolSize;
    }

    public void setPoolSize(int poolSize) {
        this.poolSize = poolSize;
    }

    public int getQueueSize() {
        return queueSize;
    }

    public void setQueueSize(int queueSize) {
        this.queueSize = queueSize;
    }

    public int getActiveCount() {
        return activeCount;
    }

    public void setActiveCount(int activeCount) {
        this.activeCount = activeCount;
    }

    public long getCompletedTaskCount() {
        return completedTaskCount;
    }

    public void setCompletedTaskCount(long completedTaskCount) {
        this.completedTaskCount = completedTaskCount;
    }

    public long getTaskCount() {
        return taskCount;
    }

    public void setTaskCount(long taskCount) {
        this.taskCount = taskCount;
    }

    public long getRejectedTaskCount() {
        return rejectedTaskCount;
    }

    public void setRejectedTaskCount(long rejectedTaskCount) {
        this.rejectedTaskCount = rejectedTaskCount;
    }

    @Override
    public String toString() {

        return threadPoolName + " {" +
                "minCostTime=" + minCostTime + '\'' +
                ", maxCostTime=" + maxCostTime +
                ", averageCostTime=" + averageCostTime +
                ", corePoolSize=" + corePoolSize +
                ", largestPoolSize=" + largestPoolSize +
                ", maximumPoolSize=" + maximumPoolSize +
                ", poolSize=" + poolSize +
                ", queueSize=" + queueSize +
                ", activeCount=" + activeCount +
                ", completedTaskCount=" + completedTaskCount +
                ", taskCount=" + taskCount +
                ", rejectedTaskCount=" + rejectedTaskCount +
                '}';
    }
}
