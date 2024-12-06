package com.gl.myparadise.common.database.sharding.masterslave.register;

import com.gl.myparadise.common.database.sharding.masterslave.config.ShardingMasterSlaveConfigurationProperties;
import com.gl.myparadise.common.database.utils.DataSourceUtls;
import com.gl.myparadise.common.database.sharding.masterslave.jdbc.ShardingDataSourceStorage;
import io.shardingsphere.api.config.MasterSlaveRuleConfiguration;
import io.shardingsphere.shardingjdbc.api.MasterSlaveDataSourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.ConfigurationPropertyNameAliases;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;
import com.gl.myparadise.common.database.constants.DataSourceConfigrceConstants;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author Vincent
 * @Description: sharding 数据源注册类
 * @ClassName: JpaConfiguration
 * @ProjectName: myparadise-common
 * @Date 2023-05-12 - 1:02
 */
@Configuration
@EnableConfigurationProperties({ShardingMasterSlaveConfigurationProperties.class})
@ConditionalOnProperty(prefix = DataSourceConfigrceConstants.SHARDING_PROPERTIES_TAG, name = "names")
@Component
public class ShardingMasterSlaveRegister implements EnvironmentAware {

    private Binder binder;

    private Environment environment;

    // 默认数据源
    private DataSource mastertDataSource;

    private static final Logger logger = LoggerFactory.getLogger(ShardingMasterSlaveRegister.class);

    private String masterDataSourceName = null;

    private final static String prefixConstant = "sharding.jdbc.datasource.";

    private Map<String, DataSource> slaveDataSourceMap = new HashMap<>();

    private final static ConfigurationPropertyNameAliases aliases = new ConfigurationPropertyNameAliases(); //别名

    @Autowired
    private ShardingMasterSlaveConfigurationProperties shardingMasterSlaveRule;

    @Override
    public void setEnvironment(Environment environment) {

        this.environment = environment;

        binder = Binder.get(environment); //绑定配置器

        String loadBalanceAlgorithmType = shardingMasterSlaveRule.getLoadBalanceAlgorithmType();

        if(loadBalanceAlgorithmType != null){

            ShardingDataSourceStorage.setLoadBalanceAlgorithmType(loadBalanceAlgorithmType);

        }

        initMasterDatasource();

        initSlaveDatasource();

    }

    @ConditionalOnProperty(prefix = DataSourceConfigrceConstants.SHARDING_PROPERTIES_TAG, name = "names")
    @Bean(name = DataSourceConfigrceConstants.SHARDING_DATASOURCE_BEAN_NAME)
    @Primary
    public DataSource getShardingDataSource() throws SQLException {

        logger.debug(" SourceName : " + shardingMasterSlaveRule.getSourceName());

        logger.debug(" masterDataSourceName : " + masterDataSourceName);

        logger.debug(" DataSlaveSource number : " + ShardingDataSourceStorage.getDataSlaveSourceList().size());

        MasterSlaveRuleConfiguration masterSlaveRuleConfig = new MasterSlaveRuleConfiguration(shardingMasterSlaveRule.getSourceName(), masterDataSourceName, ShardingDataSourceStorage.getDataSlaveSourceList());
        // 获取数据源对象
        return MasterSlaveDataSourceFactory.createDataSource(createDataSourceMap(), masterSlaveRuleConfig, new HashMap<String, Object>(), new Properties());

    }

    @Bean(name = DataSourceConfigrceConstants.SHARDING_TRANSACTION_BEAN_NAME)
    @Primary
    public PlatformTransactionManager shardingTransactionManager(@Qualifier(DataSourceConfigrceConstants.SHARDING_DATASOURCE_BEAN_NAME) DataSource shardingDataSource) {
        return new DataSourceTransactionManager(shardingDataSource);
    }

    private void initMasterDatasource() {

        logger.debug("SpringBootConfiguration.setEnvironment masterSlaveRule: " + shardingMasterSlaveRule);

        if (shardingMasterSlaveRule != null) {

            masterDataSourceName = shardingMasterSlaveRule.getMasterDataSourceName();

            if (masterDataSourceName != null) {

                logger.debug("SpringBootConfiguration.setEnvironment masterDataSourceName: " + masterDataSourceName);

                Map<String, Object> masterDataSourceProps = binder.bind((prefixConstant + masterDataSourceName).trim(), Map.class).get();

                logger.debug("SpringBootConfiguration.setEnvironment masterDataSourceProps: " + masterDataSourceProps);

                mastertDataSource = DataSourceUtls.buildDataSource(masterDataSourceProps);

                DataSourceUtls.dataBinder(mastertDataSource,masterDataSourceProps);

                ShardingDataSourceStorage.getDataMasterSourceList().add(masterDataSourceName);
            }
        }

    }

    private void initSlaveDatasource() {

        logger.debug("SpringBootConfiguration.setEnvironment masterSlaveRule: " + shardingMasterSlaveRule);

        if (shardingMasterSlaveRule != null) {

            List<String> slaveResourceList = shardingMasterSlaveRule.getSlaveDataSourceNames();

            if (slaveResourceList != null) {

                logger.debug("SpringBootConfiguration.setEnvironment slaveResourceList: " + slaveResourceList.size());

                for (int i = 0; i < slaveResourceList.size(); i++) {

                    String slavedataSourceName = slaveResourceList.get(i);

                    logger.debug("SpringBootConfiguration.setEnvironment slavedataSourceName: " + slavedataSourceName);

                    Map<String, Object> salveDataSourceProps = binder.bind((prefixConstant + slavedataSourceName).trim(), Map.class).get();

                    logger.debug("SpringBootConfiguration.setEnvironment slaveDataSourceProps: " + salveDataSourceProps);

                    DataSource dsSlave = DataSourceUtls.buildDataSource(salveDataSourceProps);

                    DataSourceUtls.dataBinder(dsSlave,salveDataSourceProps);

                    slaveDataSourceMap.put(slavedataSourceName, dsSlave);

                    ShardingDataSourceStorage.getDataSlaveSourceList().add(slavedataSourceName);

                }

            }

        }

    }

    private Map<String, DataSource> createDataSourceMap() {

        Map<String, DataSource> dsMap = new HashMap();

        dsMap.put(masterDataSourceName, mastertDataSource);

        dsMap.putAll(slaveDataSourceMap);

        return dsMap;

    }


}
