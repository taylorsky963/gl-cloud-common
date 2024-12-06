package com.gl.myparadise.common.threadpool.monitor;

import com.gl.myparadise.common.threadpool.constants.ThreadPoolConfigrceConstants;
import com.gl.myparadise.common.threadpool.executor.ThreadPoolMonitorExecutor;
import com.gl.myparadise.common.threadpool.model.ThreadPoolModel;
import com.gl.myparadise.common.threadpool.register.LGThreadPoolRegister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: LGThreadPoolMonitorScheduled
 * @ProjectName: gl-myparadise-common
 * @Date 2023-05-22 - 17:29
 */
@Component
@ConditionalOnProperty(prefix = ThreadPoolConfigrceConstants.THREADPOOL_MONITOR_SCHEDULE_TAG, name = "crons")
@AutoConfigureAfter(LGThreadPoolRegister.class)
public class LGThreadPoolMonitorScheduled {

    private static final Logger logger = LoggerFactory.getLogger(LGThreadPoolMonitorScheduled.class);

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private ThreadPoolMonitor threadPoolMonitor;

    @Scheduled(cron="${gl.thread.pool.monitor.schedule.crons}")
    public void threadPoolMonitorScheduled(){

        Map<String, ThreadPoolModel> threadPoolBeanMap = ThreadPoolConfigrceConstants.threadPoolBeanMap;

        boolean isPrintToMQ = false;

        if(!threadPoolBeanMap.isEmpty()){

            isPrintToMQ = threadPoolBeanMap.containsKey(ThreadPoolConfigrceConstants.THREADPOOL_MONITOR_NAME);

            Iterator<String> iterator = threadPoolBeanMap.keySet().iterator();

            while (iterator.hasNext()) {

                String threadPoolName = iterator.next();

                ThreadPoolModel threadPoolModel = threadPoolBeanMap.get(threadPoolName);

                ThreadPoolMonitorExecutor threadPoolExecutor = applicationContext.getBean(threadPoolModel.getThreadPoolBeanName(), ThreadPoolMonitorExecutor.class);

                if(threadPoolExecutor != null){

                    if(isPrintToMQ){

                        threadPoolMonitor.printLogToMQ(threadPoolExecutor);

                    }else {

                        threadPoolMonitor.printLog(threadPoolExecutor);

                    }

                }else{

                    logger.error("LGThreadPoolMonitorScheduled " +
                            "threadPoolMonitorScheduled threadPoolExecutor " +
                            "is null threadPoolName name : " + threadPoolName);

                }

            }

        }
    }


}
