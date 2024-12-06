package com.gl.myparadise.common.threadpool.util;

import com.gl.myparadise.common.threadpool.constants.ThreadPoolConfigrceConstants;
import com.gl.myparadise.common.threadpool.model.ThreadPoolModel;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author Vincent
 * @Description: 获取线程池的运行信息
 * @ClassName: LGThreadPoolTaskExecutorUtils
 * @ProjectName: gl-myparadise-common
 * @Date 2023-05-22 - 19:27
 */
public class LGThreadPoolTaskExecutorUtils {

    //线程池提交的总任务数
    public static long getTaskCount(ThreadPoolExecutor threadPoolExecutor){

        return threadPoolExecutor.getTaskCount();

    }

    //线程池完成的任务数
    public static long getCompletedTaskCount(ThreadPoolExecutor threadPoolExecutor){

        return threadPoolExecutor.getCompletedTaskCount();

    }

    //线程池正在处理任务的线程数
    public static int getActiveCount(ThreadPoolExecutor threadPoolExecutor){

        return threadPoolExecutor.getActiveCount();

    }

    //线程池正在等待的队列数
    public static int getQueueCount(ThreadPoolExecutor threadPoolExecutor){

        return threadPoolExecutor.getQueue().size();

    }

    //线程池核心线程的数量
    public static int getCorePoolSize(ThreadPoolExecutor threadPoolExecutor){

        return threadPoolExecutor.getCorePoolSize();

    }

    //线程池曾经到达过的最大线程数量
    public static int getLargestPoolSize(ThreadPoolExecutor threadPoolExecutor){

        return threadPoolExecutor.getLargestPoolSize();

    }

    //线程池的最大线程数量
    public static int getMaximumPoolSize(ThreadPoolExecutor threadPoolExecutor){

        return threadPoolExecutor.getMaximumPoolSize();

    }

    //线程池当前的线程数量
    public static int getPoolSize(ThreadPoolExecutor threadPoolExecutor){

        return threadPoolExecutor.getPoolSize();

    }

    public static void buildThreadPoolBeanMap(String threadPoolName, String threadPoolBeanName){

        ThreadPoolModel threadPoolModel = new ThreadPoolModel();

        threadPoolModel.setThreadPoolName(threadPoolName);

        threadPoolModel.setThreadPoolBeanName(threadPoolBeanName);

        ThreadPoolConfigrceConstants.threadPoolBeanMap.put(threadPoolName, threadPoolModel);

    }


}
