package com.gl.myparadise.common.threadpool.constants;


import com.gl.myparadise.common.threadpool.model.ThreadPoolModel;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: ThreadPoolConfigrceConstants
 * @ProjectName: myparadise-common
 * @Date 2023-05-22 - 19:58
 */
public class ThreadPoolConfigrceConstants {

    public static final String THREADPOOL_CONFIG_TAG = "gl.thread.pool";

    public static final String PROPERTIES_SPLIT_TAG = ".";

    public static final String THREADPOOL_MONITOR_PROPERTIES_TAG = "monitor";

    public static final String THREADPOOL_MONITOR_NAME = "monitorThreadPool";

    public static final String THREADPOOL_MONITOR_BEANNAME = "monitorThreadPoolBean";

    public static final String THREADPOOL_MONITOR_SCHEDULE_TAG = THREADPOOL_CONFIG_TAG
            + PROPERTIES_SPLIT_TAG + THREADPOOL_MONITOR_PROPERTIES_TAG + PROPERTIES_SPLIT_TAG + "schedule";

    public static final String THREADPOOL_MONITOR_TAG = THREADPOOL_CONFIG_TAG
            + PROPERTIES_SPLIT_TAG + THREADPOOL_MONITOR_PROPERTIES_TAG;

    public static final String THREADPOOL_MONITOR_MQ_BUSINESSCODE = "threadpool101";

    //什么也不做，直接抛弃
    public static final String THREADPOOL_REJECTED_EXECUTION_DISCARD = "discardPolicy";
    //如果执行程序尚未关闭，则位于工作队列头部的任务将被删除
    public static final String THREADPOOL_REJECTED_EXECUTION_DISCARDOLDEST = "discardOldestPolicy";
    //线程调用运行该任务的 execute 本身
    public static final String THREADPOOL_REJECTED_EXECUTION_CALLERRUNS = "callerRunsPolicy";
    // 抛出错误RejectedExecutionException
    public static final String THREADPOOL_REJECTED_EXECUTION_ABORT = "abortPolicy";

    public static Map<String, ThreadPoolModel> threadPoolBeanMap =
            new HashMap<String, ThreadPoolModel>();

}
