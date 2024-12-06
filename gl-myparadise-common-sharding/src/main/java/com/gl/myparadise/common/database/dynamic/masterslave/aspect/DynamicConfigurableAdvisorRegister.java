package com.gl.myparadise.common.database.dynamic.masterslave.aspect;

import com.gl.myparadise.common.database.constants.DataSourceConfigrceConstants;
import com.gl.myparadise.common.database.dynamic.masterslave.register.DynamicBootDataSourceRegister;
import com.gl.myparadise.common.database.dynamic.masterslave.util.DynamicDataSourceBeanMapUtils;
import com.gl.myparadise.common.mybatis.cofig.LGMybatisConfigurationProperties;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: ConfigurableAdvisorConfig
 * @ProjectName: myparadise-common
 * @Date 2023-05-11 - 13:26
 */
@Configuration
@AutoConfigureAfter(DynamicBootDataSourceRegister.class)
public class DynamicConfigurableAdvisorRegister implements BeanDefinitionRegistryPostProcessor, EnvironmentAware {

    private static final Logger logger = LoggerFactory.getLogger(DynamicConfigurableAdvisorRegister.class);

    private Environment environment;

    private LGMybatisConfigurationProperties lgMybatisConfigurationProperties = new LGMybatisConfigurationProperties();

    public AspectJExpressionPointcutAdvisor configurabledvisor(String dataSourceName) {

        AspectJExpressionPointcutAdvisor advisor = new AspectJExpressionPointcutAdvisor();

        String pointcut = buildPointCut(dataSourceName);

        if (StringUtils.isNotEmpty(pointcut)) {

            advisor.setExpression(pointcut);

            advisor.setOrder(-1);

            advisor.setAdvice(new DynamicDataSourceAdvice(dataSourceName));

        }

        return advisor;

    }

    @Override
    public void setEnvironment(Environment environment) {

        this.environment = environment;

        lgMybatisConfigurationProperties.setEnvironment(environment);
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {

        Iterator<String> iterator = DataSourceConfigrceConstants.dyDataSourceBeanMap.keySet().iterator();

        while (iterator.hasNext()) {

            String dataSourceName = iterator.next();

            logger.debug("DynamicConfigurableAdvisorRegister " +
                    "postProcessBeanDefinitionRegistry register dataSourceName : " + dataSourceName);


            String dynamicAdvisorName = DynamicDataSourceBeanMapUtils.getDynamicAdvisorName(dataSourceName);

            logger.debug("DynamicConfigurableAdvisorRegister " +
                    "postProcessBeanDefinitionRegistry register dynamicAdvisorName : " + dynamicAdvisorName);

            BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(AspectJExpressionPointcutAdvisor.class, () -> {

                return configurabledvisor(dataSourceName);

            });

            BeanDefinition beanMapperScanner = builder.getBeanDefinition();

            beanMapperScanner.setRole(BeanDefinition.ROLE_APPLICATION);

            beanDefinitionRegistry.registerBeanDefinition(dynamicAdvisorName, beanMapperScanner);


        }

    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

    }

    private String buildPointCut(String dataSourceName){
//        execution (* com.gl.myparadise.demo..dao.sharding..*.*(..))
        StringBuffer  pointCutBuffer = new StringBuffer();

        List<String> pointCutList = Arrays.asList(lgMybatisConfigurationProperties.getBasePackage(dataSourceName).split(","));

        for(int i =0; i < pointCutList.size(); i++){

            if(i == pointCutList.size() - 1){

                pointCutBuffer.append("execution (* " + pointCutList.get(i) + "..*.*(..))");

            }else{

                pointCutBuffer.append("execution (* " + pointCutList.get(i) + "..*.*(..)) || ");

            }

            logger.debug("DynamicConfigurableAdvisorRegister buildPointCut pointCutList.get(i) : " + pointCutList.get(i));

        }

        logger.debug("DynamicConfigurableAdvisorRegister buildPointCut pointCut :" + pointCutBuffer.toString());

        return pointCutBuffer.toString();

    }
}
