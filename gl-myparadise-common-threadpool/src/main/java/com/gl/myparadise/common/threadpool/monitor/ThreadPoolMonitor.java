package com.gl.myparadise.common.threadpool.monitor;

import com.gl.myparadise.common.bean.rabbitmq.SendRabbitMessageService;
import com.gl.myparadise.common.consumer.rabbitmq.producer.config.RabbitBusinessProperties;
import com.gl.myparadise.common.consumer.rabbitmq.producer.util.RabbitMqProductUtils;
import com.gl.myparadise.common.threadpool.constants.ThreadPoolConfigrceConstants;
import com.gl.myparadise.common.threadpool.executor.ThreadPoolMonitorExecutor;
import com.gl.myparadise.common.threadpool.model.ThreadPoolMonitorModel;
import com.gl.myparadise.common.threadpool.rejected.MonitorRejectedExecutionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: ThreadPoolMonitor
 * @ProjectName: gl-myparadise-common
 * @Date 2023-05-23 - 20:38
 */
@Service
public class ThreadPoolMonitor {

    private static final Logger logger = LoggerFactory.getLogger(ThreadPoolMonitor.class);

    @Autowired(required = false)
    private SendRabbitMessageService sendRabbitMessageService;

    public void printLog(ThreadPoolMonitorExecutor threadPoolMonitorExecutor){

        ThreadPoolMonitorModel threadPoolMonitorModel = buildThreadPoolMonitorModel(threadPoolMonitorExecutor);

        logger.info(threadPoolMonitorModel.toString());

    }

    @Async(value = ThreadPoolConfigrceConstants.THREADPOOL_MONITOR_BEANNAME)
    public void printLogToMQ(ThreadPoolMonitorExecutor threadPoolMonitorExecutor){

        ThreadPoolMonitorModel threadPoolMonitorModel = buildThreadPoolMonitorModel(threadPoolMonitorExecutor);

        RabbitBusinessProperties rabbitBusinessProperties = RabbitMqProductUtils.getRabbitBusinessPropertiesMap(ThreadPoolConfigrceConstants.THREADPOOL_MONITOR_MQ_BUSINESSCODE);

        if(sendRabbitMessageService != null){

            sendRabbitMessageService.sendDirectMessageNoAck(rabbitBusinessProperties.getExchangeName(),
                    rabbitBusinessProperties.getRoutingkey(), threadPoolMonitorModel.toString());

        }else{

            logger.error("ThreadPoolMonitor.printLogToMQ is error");

            printLog(threadPoolMonitorExecutor);

        }


    }



    private ThreadPoolMonitorModel buildThreadPoolMonitorModel(ThreadPoolMonitorExecutor threadPoolMonitorExecutor){

        ThreadPoolMonitorModel threadPoolMonitorModel = new ThreadPoolMonitorModel();

        threadPoolMonitorModel.setThreadPoolName(threadPoolMonitorExecutor.getThreadPoolName());

        threadPoolMonitorModel.setActiveCount(threadPoolMonitorExecutor.getActiveCount());

        threadPoolMonitorModel.setAverageCostTime(threadPoolMonitorExecutor.getAverageCostTime());

        threadPoolMonitorModel.setCompletedTaskCount(threadPoolMonitorExecutor.getCompletedTaskCount());

        threadPoolMonitorModel.setCorePoolSize(threadPoolMonitorExecutor.getCorePoolSize());

        threadPoolMonitorModel.setLargestPoolSize(threadPoolMonitorExecutor.getLargestPoolSize());

        threadPoolMonitorModel.setMaxCostTime(threadPoolMonitorExecutor.getMaxCostTime());

        threadPoolMonitorModel.setMinCostTime(threadPoolMonitorExecutor.getMinCostTime());

        threadPoolMonitorModel.setPoolSize(threadPoolMonitorExecutor.getPoolSize());

        threadPoolMonitorModel.setQueueSize(threadPoolMonitorExecutor.getQueue().size());

        threadPoolMonitorModel.setTaskCount(threadPoolMonitorExecutor.getTaskCount());

        threadPoolMonitorModel.setMaximumPoolSize(threadPoolMonitorExecutor.getMaximumPoolSize());

        threadPoolMonitorModel.setRejectedTaskCount(((MonitorRejectedExecutionHandler) threadPoolMonitorExecutor.getRejectedExecutionHandler()).getRejectedExecutionCount());

        return threadPoolMonitorModel;

    }
}
