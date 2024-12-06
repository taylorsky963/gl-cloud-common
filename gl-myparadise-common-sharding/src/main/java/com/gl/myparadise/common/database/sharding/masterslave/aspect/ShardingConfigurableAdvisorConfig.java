package com.gl.myparadise.common.database.sharding.masterslave.aspect;

import com.gl.myparadise.common.database.constants.DataSourceConfigrceConstants;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.Arrays;
import java.util.List;

/**
 * @author Vincent
 * @Description: sharding 读写分离拦截器注册类
 * @ClassName: ConfigurableAdvisorConfig
 * @ProjectName: myparadise-common
 * @Date 2023-05-11 - 13:26
 */
@Configuration
@ConditionalOnProperty(prefix = DataSourceConfigrceConstants.MYBATISC_CONFIG_SHARDING_TAG, name = DataSourceConfigrceConstants.MYBATISC_CONFIG_BASEPACKAGE_TAG)
public class ShardingConfigurableAdvisorConfig implements EnvironmentAware {

    private static final Logger logger = LoggerFactory.getLogger(ShardingConfigurableAdvisorConfig.class);

    private Environment environment;

    @Bean
    @ConditionalOnProperty(prefix = DataSourceConfigrceConstants.MYBATISC_CONFIG_SHARDING_TAG, name = DataSourceConfigrceConstants.MYBATISC_CONFIG_BASEPACKAGE_TAG)
    public AspectJExpressionPointcutAdvisor configurabledvisor() {

        String pointcut = buildPointCut();

        AspectJExpressionPointcutAdvisor advisor = new AspectJExpressionPointcutAdvisor();

        if(StringUtils.isNotEmpty(pointcut)){

            advisor.setExpression(pointcut);

            advisor.setOrder(-1);

            advisor.setAdvice(new ShardingDataSourceAdvice());

        }

        return advisor;

    }

    @Override
    public void setEnvironment(Environment environment) {

        this.environment = environment;
    }

    private String buildPointCut(){
//        execution (* com.gl.myparadise.demo..dao.sharding..*.*(..))
        StringBuffer  pointCutBuffer = new StringBuffer();

        List<String> pointCutList = Arrays.asList(environment.getProperty(DataSourceConfigrceConstants.MYBATISC_CONFIG_SHARDING_TAG
                + DataSourceConfigrceConstants.PROPERTIES_SPLIT_TAG
                + DataSourceConfigrceConstants.MYBATISC_CONFIG_BASEPACKAGE_TAG).split(","));

        for(int i =0; i < pointCutList.size(); i++){

            if(i == pointCutList.size() - 1){

                pointCutBuffer.append("execution (* " + pointCutList.get(i) + "..*.*(..))");

            }else{

                pointCutBuffer.append("execution (* " + pointCutList.get(i) + "..*.*(..)) || ");

            }

            logger.debug("ShardingConfigurableAdvisorConfig buildPointCut pointCutList.get(i) : " + pointCutList.get(i));

        }

        logger.debug("ShardingConfigurableAdvisorConfig buildPointCut pointCut :" + pointCutBuffer.toString());

        return pointCutBuffer.toString();

    }
}
