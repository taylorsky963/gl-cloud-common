package com.gl.myparadise.common.threadpool.executor;

import com.gl.myparadise.common.threadpool.rejected.LGRejectedExecutionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;

import java.util.Objects;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: ThreadPoolMonitorExecutor
 * @ProjectName: gl-myparadise-common
 * @Date 2023-05-22 - 18:22
 */

public class ThreadPoolMonitorExecutor extends ThreadPoolExecutor {

    private static final Logger logger = LoggerFactory.getLogger(ThreadPoolMonitorExecutor.class);

    //线程池名称，一般以业务名称命名，方便区分
    private String threadPoolName;

    //最短执行时间
    private long minCostTime = 0L;

    //最长执行时间
    private long maxCostTime = 0L;

    //核心线程是否空闲超时销毁，默认是FALSE, 直到线程数量为0
    private boolean allowCoreThreadTimeOut = false;

    //关机时是否等待任务结束， 其实没啥用，基本都是KILL -9 关机或者DOCKER容器直接销毁
    private boolean waitForTasksToCompleteOnShutdown = false;
    //和waitForTasksToCompleteOnShutdown 配合使用， 就是等待时间
    private long awaitTerminationMillis = 0;

    //线程池中的线程的名称前缀
    private String threadNamePrefix = "glTheadPool";

    //总的耗时
    private AtomicLong totalCostTime = new AtomicLong();

    private ThreadLocal<Long> startTimeThreadLocal = new ThreadLocal<>();

    //默认拒绝策略
    private RejectedExecutionHandler rejectedExecutionHandler = new LGRejectedExecutionHandler.AbortPolicy();

    /**
     * @param corePoolSize    线程池核心线程数
     * @param maximumPoolSize 线程池最大线程数
     * @param keepAliveTime   线程的最大空闲时间
     * @param workQueueSize   保存被提交任务的队列
     * @param threadNamePrefix   线程前缀
     * @param threadPoolName        线程池名称
     */
    public ThreadPoolMonitorExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime,
                            int workQueueSize, String threadNamePrefix, String threadPoolName) {

        this(corePoolSize, maximumPoolSize, keepAliveTime, workQueueSize,
                new MonitorThreadFactory(threadNamePrefix), threadPoolName);

    }



    /**
     * @param corePoolSize       线程池核心线程数
     * @param maximumPoolSize    线程池最大线程数
     * @param keepAliveTime      线程的最大空闲时间
     * @param workQueueSize      保存被提交任务的队列
     * @param handler            拒绝策略
     * @param threadNamePrefix   线程前缀
     * @param threadPoolName           线程池名称
     */
    public ThreadPoolMonitorExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime,
                             int workQueueSize, RejectedExecutionHandler handler, String threadNamePrefix, String threadPoolName) {

        this(corePoolSize, maximumPoolSize, keepAliveTime, workQueueSize,
                new MonitorThreadFactory(threadNamePrefix), handler, threadPoolName);
    }

    /**
     * @param corePoolSize    线程池核心线程数
     * @param maximumPoolSize 线程池最大线程数
     * @param keepAliveTime   线程的最大空闲时间
     * @param workQueueSize   保存被提交任务的队列
     * @param threadFactory   线程工厂
     * @param threadPoolName        线程池名称
     */
    private ThreadPoolMonitorExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime,
                                int workQueueSize,
                             ThreadFactory threadFactory, String threadPoolName) {

        this(corePoolSize, maximumPoolSize, keepAliveTime, workQueueSize, threadFactory, new LGRejectedExecutionHandler.AbortPolicy(), threadPoolName);

        this.threadPoolName = threadPoolName;
    }


    /**
     * @param corePoolSize    线程池核心线程数
     * @param maximumPoolSize 线程池最大线程数
     * @param keepAliveTime   线程的最大空闲时间
     * @param workQueueSize   保存被提交任务的队列
     * @param threadFactory   线程工厂
     * @param handler         拒绝策略
     * @param threadPoolName        线程池名称
     */
    private ThreadPoolMonitorExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime,
                             int workQueueSize,
                             ThreadFactory threadFactory, RejectedExecutionHandler handler, String threadPoolName) {

        super(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, createQueue(workQueueSize), threadFactory, handler);

        this.threadPoolName = threadPoolName;

    }


    protected static BlockingQueue<Runnable> createQueue(int queueCapacity) {

        if (queueCapacity > 0) {

            return new LinkedBlockingQueue<>(queueCapacity);

        }else {

            return new SynchronousQueue<>();

        }
    }

    public boolean isAllowCoreThreadTimeOut() {
        return allowCoreThreadTimeOut;
    }

    public void setAllowCoreThreadTimeOut(boolean allowCoreThreadTimeOut) {

        if(allowCoreThreadTimeOut){

            this.allowCoreThreadTimeOut(true);
        }

        this.allowCoreThreadTimeOut = allowCoreThreadTimeOut;
    }

    /**
     * 线程池延迟关闭时（等待线程池里的任务都执行完毕），统计线程池情况
     */
    @Override
    public void shutdown() {

        if (this != null) {

            if (this.waitForTasksToCompleteOnShutdown) {

                super.shutdown();

            }else {

                logger.debug("{} 立即关闭线程池，已执行任务: {}, 正在执行任务: {}, 未执行任务数量: {}",
                        this.threadPoolName, this.getCompletedTaskCount(), this.getActiveCount(), this.getQueue().size());

                for (Runnable remainingTask : shutdownNow()) {

                    cancelRemainingTask(remainingTask);

                }

            }

            awaitTerminationIfNecessary(this);
        }
        // 统计已执行任务、正在执行任务、未执行任务数量
        logger.debug("{} 关闭线程池， 已执行任务: {}, 正在执行任务: {}, 未执行任务数量: {}",
                this.threadPoolName, this.getCompletedTaskCount(), this.getActiveCount(), this.getQueue().size());
        super.shutdown();
    }

    protected void cancelRemainingTask(Runnable task) {
        if (task instanceof Future) {
            ((Future<?>) task).cancel(true);
        }
    }

    public boolean isWaitForTasksToCompleteOnShutdown() {
        return waitForTasksToCompleteOnShutdown;
    }

    public void setWaitForTasksToCompleteOnShutdown(boolean waitForTasksToCompleteOnShutdown) {
        this.waitForTasksToCompleteOnShutdown = waitForTasksToCompleteOnShutdown;
    }

    /**
     * Wait for the executor to terminate, according to the value of the
     * {@link #setAwaitTerminationSeconds "awaitTerminationSeconds"} property.
     */
    private void awaitTerminationIfNecessary(ExecutorService executor) {
        if (this.awaitTerminationMillis > 0) {
            try {
                if (!executor.awaitTermination(this.awaitTerminationMillis, TimeUnit.MILLISECONDS)) {
                    if (logger.isWarnEnabled()) {
                        logger.warn("Timed out while waiting for executor" +
                                (this.threadPoolName != null ? " '" + this.threadPoolName + "'" : "") + " to terminate");
                    }
                }
            }
            catch (InterruptedException ex) {
                if (logger.isWarnEnabled()) {
                    logger.warn("Interrupted while waiting for executor" +
                            (this.threadPoolName != null ? " '" + this.threadPoolName + "'" : "") + " to terminate");
                }
                Thread.currentThread().interrupt();
            }
        }
    }

    public void setAwaitTerminationSeconds(int awaitTerminationSeconds) {
        this.awaitTerminationMillis = awaitTerminationSeconds * 1000;
    }

    /**
     * Variant of {@link #setAwaitTerminationSeconds} with millisecond precision.
     * @since 5.2.4
     * @see #setAwaitTerminationSeconds
     */
    public void setAwaitTerminationMillis(long awaitTerminationMillis) {
        this.awaitTerminationMillis = awaitTerminationMillis;
    }

//    /**
//     * 线程池立即关闭时，统计线程池情况
//     */
//    @Override
//    public List<Runnable> shutdownNow() {
//        // 统计已执行任务、正在执行任务、未执行任务数量
//        return super.shutdownNow();
//    }

    /**
     * 任务执行之前，记录任务开始时间
     */
    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        startTimeThreadLocal.set(System.currentTimeMillis());
    }

    /**
     * 任务执行之后，计算任务结束时间
     */
    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        long costTime = System.currentTimeMillis() - startTimeThreadLocal.get();
        startTimeThreadLocal.remove();
        maxCostTime = maxCostTime > costTime ? maxCostTime : costTime;
        if (getCompletedTaskCount() == 0) {
            minCostTime = costTime;
        }
        minCostTime = minCostTime < costTime ? minCostTime : costTime;
        totalCostTime.addAndGet(costTime);
        logger.debug("{}-pool-monitor: " +
                        "任务耗时: {} ms, 初始线程数: {}, 核心线程数: {}, 执行的任务数量: {}, " +
                        "已完成任务数量: {}, 任务总数: {}, 队列里缓存的任务数量: {}, 池中存在的最大线程数: {}, " +
                        "最大允许的线程数: {},  线程空闲时间: {}, 线程池是否关闭: {}, 线程池是否终止: {}",
                this.threadPoolName,
                costTime, this.getPoolSize(), this.getCorePoolSize(), this.getActiveCount(),
                this.getCompletedTaskCount(), this.getTaskCount(), this.getQueue().size(), this.getLargestPoolSize(),
                this.getMaximumPoolSize(), this.getKeepAliveTime(TimeUnit.MILLISECONDS), this.isShutdown(), this.isTerminated());
    }


    public long getMinCostTime() {
        return minCostTime;
    }

    public long getMaxCostTime() {
        return maxCostTime;
    }

    public String getThreadPoolName() {
        return threadPoolName;
    }

    public long getAverageCostTime(){
        if(getCompletedTaskCount()==0||totalCostTime.get()==0){
            return 0;
        }
        return totalCostTime.get()/getCompletedTaskCount();
    }

    public void setRejectedExecutionHandler(@Nullable RejectedExecutionHandler rejectedExecutionHandler) {
        this.rejectedExecutionHandler =
                (rejectedExecutionHandler != null ? rejectedExecutionHandler : new ThreadPoolExecutor.AbortPolicy());
    }

    /**
     * 生成线程池所用的线程，改写了线程池默认的线程工厂，传入线程池名称，便于问题追踪
     */
    static class MonitorThreadFactory implements ThreadFactory {

        private static final AtomicInteger poolNumber = new AtomicInteger(1);

        private final ThreadGroup group;

        private final AtomicInteger threadNumber = new AtomicInteger(1);

        private final String namePrefix;

        /**
         * 初始化线程工厂
         *
         * @param threadNamePrefix 线程池名称
         */
        MonitorThreadFactory(String threadNamePrefix) {

            SecurityManager s = System.getSecurityManager();

            group = Objects.nonNull(s) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();

            namePrefix = threadNamePrefix + "-pool-" + poolNumber.getAndIncrement() + "-thread-";
        }

        @Override
        public Thread newThread(Runnable r) {

            Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);

            if (t.isDaemon()) {

                t.setDaemon(false);
            }

            if (t.getPriority() != Thread.NORM_PRIORITY) {

                t.setPriority(Thread.NORM_PRIORITY);
            }

            return t;

        }
    }

}
