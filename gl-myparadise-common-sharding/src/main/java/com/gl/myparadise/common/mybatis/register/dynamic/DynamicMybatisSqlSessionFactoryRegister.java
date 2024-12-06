package com.gl.myparadise.common.mybatis.register.dynamic;

import com.gl.myparadise.common.database.constants.DataSourceConfigrceConstants;
import com.gl.myparadise.common.database.dynamic.masterslave.util.DynamicDataSourceBeanMapUtils;
import com.gl.myparadise.common.database.model.DynamicDataSourceSpringBootRegisterModel;
import com.gl.myparadise.common.mybatis.cofig.LGMybatisConfigurationProperties;
import com.gl.myparadise.common.mybatis.transaction.DynamicTransactionInterceptor;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Iterator;

/**
 * @author Vincent
 * @Description: spring datasource mybatis SqlSessionFactory 注册类
 * @ClassName: DynamicMybatisSqlSessionFactoryRegister
 * @ProjectName: myparadise-common
 * @Date 2023-05-12 - 18:44
 */
@Component
@Order(-3)
public class DynamicMybatisSqlSessionFactoryRegister implements BeanDefinitionRegistryPostProcessor, EnvironmentAware, ApplicationContextAware {

    private static final Logger logger = LoggerFactory.getLogger(DynamicMybatisSqlSessionFactoryRegister.class);

    private Environment environment;

    private LGMybatisConfigurationProperties lgMybatisConfigurationProperties = new LGMybatisConfigurationProperties();

    private ApplicationContext applicationContext;

    private DynamicDataSourceSpringBootRegisterModel dataSourceSpringBootRegisterModel;

    @Override
    public void setEnvironment(Environment environment) {

        this.environment = environment;

        lgMybatisConfigurationProperties.setEnvironment(environment);

    }

    public SqlSessionFactory dynamicSqlSessionFactory(String dataSourceName, DataSource dynamicDataSource){

        try{
            SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();

            factoryBean.setDataSource(dynamicDataSource);

            factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(lgMybatisConfigurationProperties.getMapperLocations(dataSourceName)));

            factoryBean.setTypeAliasesPackage(lgMybatisConfigurationProperties.getTypeAliasesPackage(dataSourceName));

            factoryBean.setConfigLocation(new PathMatchingResourcePatternResolver().getResource(lgMybatisConfigurationProperties.getConfigLocations(dataSourceName)));

            factoryBean.setPlugins(new Interceptor[]{new DynamicTransactionInterceptor(dataSourceName)});

            return factoryBean.getObject();

        }catch (Exception e){

            logger.error("DynamicMybatisRegister dynamicSqlSessionFactory is error", e );

        }

        return null;

    }


    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {

        Iterator<String> iterator = DataSourceConfigrceConstants.dyDataSourceBeanMap.keySet().iterator();

        while (iterator.hasNext()) {

            String dataSourceName = iterator.next();

            logger.debug("DynamicMybatisRegister " +
                    "postProcessBeanFactory register SqlSessionFactory dataSourceName : " + dataSourceName);

            dataSourceSpringBootRegisterModel
                    = DynamicDataSourceBeanMapUtils.getDynamicDataSourceSpringBootRegisterModel(dataSourceName);

            bulidSqlSessionFactory(dataSourceName, beanDefinitionRegistry);

        }

    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {



    }

    private void bulidSqlSessionFactory(String dataSourceName, BeanDefinitionRegistry beanDefinitionRegistry){

        String dataSourceBeanName = dataSourceSpringBootRegisterModel.getDataSourceBeanName();

        logger.debug("DynamicMybatisRegister " +
                "bulidSqlSessionFactory register bulidSqlSessionFactory dataSourceBeanName : " + dataSourceBeanName);

        String sqlSessionFactoryBeanName = dataSourceSpringBootRegisterModel.getSqlSessionFactoryBeanName();

        logger.debug("DynamicMybatisRegister " +
                "bulidSqlSessionFactory register bulidSqlSessionFactory sqlSessionFactoryBeanName : " + sqlSessionFactoryBeanName);

        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(SqlSessionFactory.class, () -> {

            return dynamicSqlSessionFactory(dataSourceName, applicationContext.getBean(dataSourceBeanName, DataSource.class));

        });

        if(dataSourceSpringBootRegisterModel.getPrimary()){

            builder.setPrimary(true);

        }

        builder.addDependsOn(dataSourceBeanName);

        BeanDefinition beanSqlSessionFactory = builder.getRawBeanDefinition();

        beanSqlSessionFactory.setRole(BeanDefinition.ROLE_APPLICATION);

        beanDefinitionRegistry.registerBeanDefinition(sqlSessionFactoryBeanName, beanSqlSessionFactory);


    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
