package com.gl.myparadise.common.mybatis.register;

import com.gl.myparadise.common.database.constants.DataSourceConfigrceConstants;
import com.gl.myparadise.common.database.sharding.masterslave.register.ShardingMasterSlaveRegister;
import com.gl.myparadise.common.mybatis.cofig.LGMybatisConfigurationProperties;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: LGMybatisRegister
 * @ProjectName: myparadise-common
 * @Date 2023-05-12 - 18:44
 */
@Configuration
@Component
@AutoConfigureAfter(ShardingMasterSlaveRegister.class)
@ConditionalOnProperty(prefix = DataSourceConfigrceConstants.MYBATISC_CONFIG_SHARDING_TAG, name = "basePackage")
@ConditionalOnBean(name = DataSourceConfigrceConstants.SHARDING_DATASOURCE_BEAN_NAME)
@MapperScan(basePackages = {"${gl.mybatis.shardingDataSource.basePackage}"}, sqlSessionFactoryRef = "shardingSqlSessionFactory")
public class ShardingMybatisRegister implements EnvironmentAware {

    private LGMybatisConfigurationProperties configPops = new LGMybatisConfigurationProperties();

    private String dataSourceName = DataSourceConfigrceConstants
            .SHARDING_DATASOURCE_BEAN_NAME;

    @Autowired(required = false)
    @Qualifier(DataSourceConfigrceConstants.SHARDING_DATASOURCE_BEAN_NAME)
    private DataSource shardingDataSource;

    private Environment environment;

    @Override
    public void setEnvironment(Environment environment) {

        this.environment = environment;

        configPops.setEnvironment(this.environment);

    }

    @Bean(name = "shardingSqlSessionFactory")
    @Primary
    @ConditionalOnProperty(prefix = DataSourceConfigrceConstants.MYBATISC_CONFIG_TAG, name = "shardingDataSource.basePackage")
    public SqlSessionFactory shardingSqlSessionFactory(ApplicationContext applicationContext) throws Exception {

        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();

        factoryBean.setDataSource(shardingDataSource);

        factoryBean.setMapperLocations(applicationContext.getResources(configPops.getMapperLocations(dataSourceName)));

        factoryBean.setTypeAliasesPackage(configPops.getTypeAliasesPackage(dataSourceName));

        factoryBean.setConfigLocation(applicationContext.getResource(configPops.getConfigLocations(dataSourceName)));

//        factoryBean.setPlugins(new Interceptor[]{new CryptInterceptor()});

        return factoryBean.getObject();

    }

    @Bean
    @Primary
    public SqlSessionTemplate shardingSqlSessionTemplate(ApplicationContext applicationContext) throws Exception {
        return new SqlSessionTemplate(shardingSqlSessionFactory(applicationContext));
    }


}
