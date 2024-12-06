package com.gl.myparadise.common.mybatis.register.dynamic;

import com.gl.myparadise.common.database.constants.DataSourceConfigrceConstants;
import com.gl.myparadise.common.database.dynamic.masterslave.util.DynamicDataSourceBeanMapUtils;
import com.gl.myparadise.common.database.model.DynamicDataSourceSpringBootRegisterModel;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
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
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Iterator;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: DynamicMybatisSqlSessionTemplateRegister
 * @ProjectName: myparadise-common
 * @Date 2023-05-15 - 19:43
 */
@Component
@Order(-2)
public class DynamicMybatisSqlSessionTemplateRegister implements BeanDefinitionRegistryPostProcessor, ApplicationContextAware {

    private static final Logger logger = LoggerFactory.getLogger(DynamicMybatisSqlSessionFactoryRegister.class);

    private ApplicationContext applicationContext;

    private DynamicDataSourceSpringBootRegisterModel dataSourceSpringBootRegisterModel;

    public SqlSessionTemplate dynamicSqlSessionTemplate(SqlSessionFactory sqlSessionFactory){

        logger.debug("DynamicMybatisSqlSessionTemplateRegister dynamicSqlSessionTemplate");

        return new SqlSessionTemplate(sqlSessionFactory);

    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {

        Iterator<String> iterator = DataSourceConfigrceConstants.dyDataSourceBeanMap.keySet().iterator();

        while (iterator.hasNext()) {

            String dataSourceName = iterator.next();

            logger.debug("DynamicMybatisSqlSessionTemplateRegister " +
                    "postProcessBeanFactory register SqlSessionTemplate dataSourceName : " + dataSourceName);

            dataSourceSpringBootRegisterModel
                    = DynamicDataSourceBeanMapUtils.getDynamicDataSourceSpringBootRegisterModel(dataSourceName);

            String sqlSessionFactoryBeanName = dataSourceSpringBootRegisterModel.getSqlSessionFactoryBeanName();

            logger.debug("DynamicMybatisSqlSessionTemplateRegister " +
                    "postProcessBeanFactory register SqlSessionTemplate sqlSessionFactoryBeanName : " + sqlSessionFactoryBeanName);

            bulidSqlSessionTemplate(sqlSessionFactoryBeanName, beanDefinitionRegistry);

        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {



    }

    private void bulidSqlSessionTemplate(String sqlSessionFactoryBeanName, BeanDefinitionRegistry beanDefinitionRegistry){

        String sqlSessionTemplateBeanName = dataSourceSpringBootRegisterModel.getSqlSessionTemplateBeanName();

        logger.debug("DynamicMybatisSqlSessionTemplateRegister " +
                "bulidSqlSessionTemplate register sqlSessionTemplateBeanName : " + sqlSessionTemplateBeanName);

        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(SqlSessionTemplate.class, () -> {

            return dynamicSqlSessionTemplate(applicationContext.getBean(sqlSessionFactoryBeanName, SqlSessionFactory.class));

        });

        if(dataSourceSpringBootRegisterModel.getPrimary()){

            builder.setPrimary(true);

        }

        builder.addDependsOn(sqlSessionFactoryBeanName);

        BeanDefinition beanSqlSessionTemplate = builder.getRawBeanDefinition();

        beanSqlSessionTemplate.setRole(BeanDefinition.ROLE_APPLICATION);

        beanDefinitionRegistry.registerBeanDefinition(sqlSessionTemplateBeanName, beanSqlSessionTemplate);

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        this.applicationContext = applicationContext;

    }
}
