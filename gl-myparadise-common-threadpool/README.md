# 概述
此为线程池的封装。

## 配置说明

定义线程池的名字
    
    gl.thread.pool.names=prod,user
 
配置线程池的BEANNAME，用@Async标签调用的时候要指定BEANNAME    
    
    gl.thread.pool.prod.beanName=prodThreadPool
    
    @Async(value="prodThreadPool")
    
    #定义核心线程的大小
    gl.thread.pool.prod.corePoolSize=5
    #定义最大线程数
    gl.thread.pool.prod.maxPoolSize=10
    #定义空闲回收时间
    gl.thread.pool.prod.keepAliveSeconds=600
    #定义等待队列大小
    gl.thread.pool.prod.queueCapacity=5
    #定义线程的前缀
    gl.thread.pool.prod.threadNamePrefix=prod_pool
    #是否允许核心线程空闲回收
    gl.thread.pool.prod.allowCoreThreadTimeOut=false
    #关闭线程池是否等待线程结束
    gl.thread.pool.prod.waitForTasksToCompleteOnShutdown=false
    #关闭线程的时间，需要和waitForTasksToCompleteOnShutdown同时设置
    gl.thread.pool.prod.awaitTerminationMillis=60
    #abortPolicy策略：默认策略，如果线程池队列满了丢掉这个任务并且抛出RejectedExecutionException异常。
    #discardPolicy策略：如果线程池队列满了，会直接丢掉这个任务并且不会有任何异常。
    #discardOldestPolicy策略：如果队列满了，会将最早进入队列的任务删掉腾出空间，再尝试加入队列。
    #callerRunsPolicy策略：如果添加到线程池失败，那么主线程会自己去执行该任务，不会等待线程池中的线程去执行。
    gl.thread.pool.prod.rejectedHandler=discardPolicy

设置线程池监控类的时间戳

    gl.thread.pool.monitor.schedule.crons=0 0/5 * * * ?
    
配置监控信息发送MQ的线程池

    gl.thread.pool.monitor.corePoolSize=5
    gl.thread.pool.monitor.maxPoolSize=10
    gl.thread.pool.monitor.keepAliveSeconds=600
    gl.thread.pool.monitor.queueCapacity=5
    gl.thread.pool.monitor.threadNamePrefix=monitor_pool
    gl.thread.pool.monitor.allowCoreThreadTimeOut=false
    gl.thread.pool.monitor.waitForTasksToCompleteOnShutdown=false
    gl.thread.pool.monitor.awaitTerminationMillis=60
    gl.thread.pool.monitor.rejectedHandler=discardPolicy

