package com.gl.myparadise.common.mybatis.register.dynamic;

import com.gl.myparadise.common.database.constants.DataSourceConfigrceConstants;
import com.gl.myparadise.common.database.dynamic.masterslave.util.DynamicDataSourceBeanMapUtils;
import com.gl.myparadise.common.database.model.DynamicDataSourceSpringBootRegisterModel;
import com.gl.myparadise.common.mybatis.cofig.LGMybatisConfigurationProperties;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Iterator;

/**
 * @author Vincent
 * @Description: spring datasource mybatis MapperScanner 注册类
 * @ClassName: DynamicMybatisMapperScannerRegister
 * @ProjectName: myparadise-common
 * @Date 2023-05-15 - 19:57
 */
@Component
@Order(-1)
public class DynamicMybatisMapperScannerRegister implements BeanDefinitionRegistryPostProcessor, EnvironmentAware , ApplicationContextAware {

    private static final Logger logger = LoggerFactory.getLogger(DynamicMybatisMapperScannerRegister.class);

    private Environment environment;

    private LGMybatisConfigurationProperties lgMybatisConfigurationProperties = new LGMybatisConfigurationProperties();

    private DynamicDataSourceSpringBootRegisterModel dataSourceSpringBootRegisterModel;

    private int i = 0;

    @Override
    public void setEnvironment(Environment environment) {

        this.environment = environment;

        lgMybatisConfigurationProperties.setEnvironment(environment);

    }

    public MapperScannerConfigurer mapperScannerConfigurer(String dataSourceName, String sqlSessionFactoryBeanName, String sqlSessionTemplateBeanName) {

        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();

        mapperScannerConfigurer.setBasePackage(lgMybatisConfigurationProperties.getBasePackage(dataSourceName));

        mapperScannerConfigurer.setSqlSessionFactoryBeanName(sqlSessionFactoryBeanName);

        mapperScannerConfigurer.setSqlSessionTemplateBeanName(sqlSessionTemplateBeanName);

        return mapperScannerConfigurer;

    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {

        Iterator<String> iterator = DataSourceConfigrceConstants.dyDataSourceBeanMap.keySet().iterator();

        while (iterator.hasNext()) {

            String dataSourceName = iterator.next();

            logger.debug("DynamicMybatisMapperScannerRegister " +
                    "postProcessBeanDefinitionRegistry register MapperScannerConfigurer dataSourceName : " + dataSourceName);

            dataSourceSpringBootRegisterModel
                    = DynamicDataSourceBeanMapUtils.getDynamicDataSourceSpringBootRegisterModel(dataSourceName);

            String sqlSessionTemplateBeanName = dataSourceSpringBootRegisterModel.getSqlSessionTemplateBeanName();

            logger.debug("DynamicMybatisMapperScannerRegister " +
                    "postProcessBeanDefinitionRegistry register MapperScannerConfigurer sqlSessionTemplateBeanName : " + sqlSessionTemplateBeanName);

            String sqlSessionFactoryBeanName = dataSourceSpringBootRegisterModel.getSqlSessionFactoryBeanName();

            logger.debug("DynamicMybatisMapperScannerRegister " +
                    "postProcessBeanDefinitionRegistry register MapperScannerConfigurer sqlSessionFactoryBeanName : " + sqlSessionFactoryBeanName);

            bulidMapperScannerConfigurer(dataSourceName, sqlSessionFactoryBeanName, sqlSessionTemplateBeanName,
                    beanDefinitionRegistry);

        }

    }

    private void bulidMapperScannerConfigurer(String dataSourceName, String sqlSessionFactoryBeanName, String sqlSessionTemplateBeanName, BeanDefinitionRegistry beanDefinitionRegistry) {

        String sqlMapperScannerBeanName = dataSourceSpringBootRegisterModel.getMapperScannerBeanName();

        logger.debug("DynamicMybatisMapperScannerRegister " +
                " register bulidMapperScannerConfigurer sqlMapperScannerBeanName : " + sqlMapperScannerBeanName);

        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(MapperScannerConfigurer.class, () -> {

            return mapperScannerConfigurer(dataSourceName, sqlSessionFactoryBeanName, sqlSessionTemplateBeanName);

        });

        builder.addDependsOn(sqlSessionFactoryBeanName);

        builder.addDependsOn(sqlSessionTemplateBeanName);

        BeanDefinition beanMapperScanner = builder.getBeanDefinition();

        beanMapperScanner.setRole(BeanDefinition.ROLE_APPLICATION);

        beanDefinitionRegistry.registerBeanDefinition(sqlMapperScannerBeanName, beanMapperScanner);

    }



    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {


    }

}
