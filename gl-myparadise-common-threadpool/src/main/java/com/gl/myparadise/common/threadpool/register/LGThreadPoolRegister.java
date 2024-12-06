package com.gl.myparadise.common.threadpool.register;

import com.gl.myparadise.common.threadpool.config.ThreadPoolConfigurationProperties;
import com.gl.myparadise.common.threadpool.config.ThreadPoolInfoConfigurationProperties;
import com.gl.myparadise.common.threadpool.constants.ThreadPoolConfigrceConstants;
import com.gl.myparadise.common.threadpool.executor.ThreadPoolMonitorExecutor;
import com.gl.myparadise.common.threadpool.rejected.LGRejectedExecutionHandler;
import com.gl.myparadise.common.threadpool.util.LGThreadPoolTaskExecutorUtils;
import com.gl.myparadise.common.tools.util.ListUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: LGThreadPoolRegister
 * @ProjectName: gl-myparadise-common
 * @Date 2023-05-22 - 14:02
 */
@Component
@ConditionalOnProperty(prefix = ThreadPoolConfigrceConstants.THREADPOOL_CONFIG_TAG, name = "names")
public class LGThreadPoolRegister implements BeanDefinitionRegistryPostProcessor, EnvironmentAware {

    private static final Logger logger = LoggerFactory.getLogger(LGThreadPoolRegister.class);

    private Binder binder;

    private Environment environment;

    private Map<String, ThreadPoolInfoConfigurationProperties> threadPoolConfigPropsMap
            = new HashMap<String, ThreadPoolInfoConfigurationProperties>();

    private ThreadPoolInfoConfigurationProperties threadPoolMonitorConfigProps;

    @Override
    public void setEnvironment(Environment environment) {

        this.environment = environment;

        binder = Binder.get(environment); //绑定配置器

        bulidThreadPoolConfigPropsMap();

        bulidThreadPoolMonitorInfoConfigurationProperties();

    }

    private void bulidThreadPoolMonitorInfoConfigurationProperties(){

        if(StringUtils.isNotEmpty(environment.getProperty(ThreadPoolConfigrceConstants.THREADPOOL_MONITOR_TAG
                + ThreadPoolConfigrceConstants.PROPERTIES_SPLIT_TAG + "corePoolSize"))){

            this.threadPoolMonitorConfigProps
                    = binder.bind((ThreadPoolConfigrceConstants.THREADPOOL_MONITOR_TAG).trim(), ThreadPoolInfoConfigurationProperties.class).get();


        }

    }



    private void bulidThreadPoolConfigPropsMap(){

        ThreadPoolConfigurationProperties threadPoolConfigModel = binder.bind((ThreadPoolConfigrceConstants.THREADPOOL_CONFIG_TAG).trim(), ThreadPoolConfigurationProperties.class).get();

        if(threadPoolConfigModel != null){

            List<String> threadPoolNamesList = threadPoolConfigModel.getNames();

            if(ListUtil.isNotEmpty(threadPoolNamesList)){

                for(int i = 0 ; i < threadPoolNamesList.size(); i ++){

                    String threadPoolName = threadPoolNamesList.get(i);

                    logger.debug("LGThreadPoolRegister bulidThreadPoolConfigPropsMap threadPoolName : " + threadPoolName);

                    threadPoolConfigPropsMap.put(threadPoolName, bulidThreadPoolInfoConfigurationProperties(threadPoolName));

                }
            }

        }

    }

    private ThreadPoolInfoConfigurationProperties bulidThreadPoolInfoConfigurationProperties(String threadPoolName){

        return binder.bind((ThreadPoolConfigrceConstants.THREADPOOL_CONFIG_TAG + ThreadPoolConfigrceConstants.PROPERTIES_SPLIT_TAG + threadPoolName).trim(), ThreadPoolInfoConfigurationProperties.class).get();

    }

    public ThreadPoolExecutor threadPoolTaskExecutor(String threadPoolName, ThreadPoolInfoConfigurationProperties threadPoolInfoConfigProps) {

        RejectedExecutionHandler rejectedExecutionHandler = new LGRejectedExecutionHandler.AbortPolicy();

        String rejectedhandlerName = threadPoolInfoConfigProps.getRejectedHandler();

        if(StringUtils.isNotEmpty(rejectedhandlerName)){

            if(ThreadPoolConfigrceConstants.THREADPOOL_REJECTED_EXECUTION_DISCARD.equals(rejectedhandlerName)){

                rejectedExecutionHandler = new LGRejectedExecutionHandler.DiscardPolicy();

            }else if (ThreadPoolConfigrceConstants.THREADPOOL_REJECTED_EXECUTION_DISCARDOLDEST.equals(rejectedhandlerName)){

                rejectedExecutionHandler = new LGRejectedExecutionHandler.DiscardOldestPolicy();

            }else if (ThreadPoolConfigrceConstants.THREADPOOL_REJECTED_EXECUTION_CALLERRUNS.equals(rejectedhandlerName)){

                rejectedExecutionHandler = new LGRejectedExecutionHandler.CallerRunsPolicy();

            }else{

                try {

                    Class rejectedHandlerClass = Class.forName(threadPoolInfoConfigProps.getRejectedHandler());

                    rejectedExecutionHandler = (RejectedExecutionHandler) rejectedHandlerClass.newInstance();

                } catch (Exception e) {

                    logger.error("LGThreadPoolRegister.threadPoolTaskExecutor rejectedHandlerClass is error" , e);

                }

            }

        }

        ThreadPoolMonitorExecutor executor = new ThreadPoolMonitorExecutor(
                threadPoolInfoConfigProps.getCorePoolSize(),
                threadPoolInfoConfigProps.getMaxPoolSize(),
                threadPoolInfoConfigProps.getKeepAliveSeconds(),
                threadPoolInfoConfigProps.getQueueCapacity(),
                rejectedExecutionHandler,
                threadPoolInfoConfigProps.getThreadNamePrefix(),
                threadPoolName
        );

        executor.setAllowCoreThreadTimeOut(threadPoolInfoConfigProps.isAllowCoreThreadTimeOut());

        executor.setWaitForTasksToCompleteOnShutdown(threadPoolInfoConfigProps.isWaitForTasksToCompleteOnShutdown());

        executor.setAwaitTerminationMillis(threadPoolInfoConfigProps.getAwaitTerminationMillis());

        return executor;

    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {

        threadPoolRegistry(beanDefinitionRegistry);

        threadPoolMonitorRegistry(beanDefinitionRegistry);
    }

    private void threadPoolRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {

        if(!threadPoolConfigPropsMap.isEmpty()){

            Iterator<String> iterator = threadPoolConfigPropsMap.keySet().iterator();

            while (iterator.hasNext()) {

                String threadPoolName = iterator.next();

                ThreadPoolInfoConfigurationProperties threadPoolInfoConfigProps = threadPoolConfigPropsMap.get(threadPoolName);

                logger.debug("LGThreadPoolRegister threadPoolRegistry threadPoolName : " + threadPoolName);

                String threadPoolBeanName = threadPoolInfoConfigProps.getBeanName();

                logger.debug("LGThreadPoolRegister threadPoolRegistry beanName : " + threadPoolBeanName);

                BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(ThreadPoolExecutor.class, () -> {
                    // 这个方法可以定义这个bean的实例创建方式，如构造函数之后还想调用其他的方法，也可以在这里做
                    return threadPoolTaskExecutor(threadPoolName, threadPoolInfoConfigProps);

                });

                BeanDefinition beanDefinition = beanDefinitionBuilder.getRawBeanDefinition();
                //注册bean定义
                beanDefinitionRegistry.registerBeanDefinition(threadPoolBeanName, beanDefinition);

                LGThreadPoolTaskExecutorUtils.buildThreadPoolBeanMap(threadPoolName, threadPoolBeanName);

            }

        }else{

            logger.error("LGThreadPoolRegister " +
                    "threadPoolRegistry register threadPool is error ");

        }

    }

    private void threadPoolMonitorRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {

        if (threadPoolMonitorConfigProps != null) {

            String threadPoolName = ThreadPoolConfigrceConstants.THREADPOOL_MONITOR_NAME;

            String threadPoolMonitorBeanName = ThreadPoolConfigrceConstants.THREADPOOL_MONITOR_BEANNAME;

            BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(ThreadPoolExecutor.class, () -> {
                // 这个方法可以定义这个bean的实例创建方式，如构造函数之后还想调用其他的方法，也可以在这里做
                return threadPoolTaskExecutor(threadPoolName, threadPoolMonitorConfigProps);

            });

            BeanDefinition beanDefinition = beanDefinitionBuilder.getRawBeanDefinition();
            //注册bean定义
            beanDefinitionRegistry.registerBeanDefinition(threadPoolMonitorBeanName, beanDefinition);

            LGThreadPoolTaskExecutorUtils.buildThreadPoolBeanMap(threadPoolName, threadPoolMonitorBeanName);

        }

    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

    }

}
