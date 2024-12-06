package com.gl.myparadise.common.database.dynamic.masterslave.register;


import com.gl.myparadise.common.database.constants.DataSourceConfigrceConstants;
import com.gl.myparadise.common.database.dynamic.masterslave.config.DynamicDataSourceConfigurationProperties;
import com.gl.myparadise.common.database.dynamic.masterslave.config.DynamicMasterSlaveConfigurationProperties;
import com.gl.myparadise.common.database.dynamic.masterslave.config.TransactionManagerConfigurationProperties;
import com.gl.myparadise.common.database.dynamic.masterslave.datasource.DynamicDataSource;
import com.gl.myparadise.common.database.dynamic.masterslave.util.DynamicDataSourceBeanMapUtils;
import com.gl.myparadise.common.database.model.DataSourceModel;
import com.gl.myparadise.common.database.model.DynamicDataSourceSpringBootRegisterModel;
import com.gl.myparadise.common.database.model.MasterSlaveDataSourceModel;
import com.gl.myparadise.common.database.utils.DataSourceUtls;
import com.gl.myparadise.common.tools.util.ListUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.ConfigurationPropertyNameAliases;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.*;

@Configuration
@Order(-99)
@ConditionalOnProperty(prefix = DataSourceConfigrceConstants.SRPINGBOOT_DATASOURCE_TAG, name = "names")
@AutoConfigureBefore(DataSourceAutoConfiguration.class)
public class DynamicBootDataSourceRegister implements BeanDefinitionRegistryPostProcessor, EnvironmentAware {

    private static final Logger logger = LoggerFactory.getLogger(DynamicBootDataSourceRegister.class);

    private DynamicDataSourceConfigurationProperties dynamicDataSourceRule;

    private Map<String, MasterSlaveDataSourceModel> dynamicDataSourceMap
            = new HashMap<String, MasterSlaveDataSourceModel>();

    private Binder binder;

    private String primary;

    private Environment environment;

    private final static ConfigurationPropertyNameAliases aliases = new ConfigurationPropertyNameAliases(); //别名

    @Override
    public final void setEnvironment(final Environment environment) {

        this.environment = environment;

        binder = Binder.get(environment); //绑定配置器

        dynamicDataSourceRule = binder.bind((DataSourceConfigrceConstants.SRPINGBOOT_DATASOURCE_TAG).trim(), DynamicDataSourceConfigurationProperties.class).get();

        if(dynamicDataSourceRule != null){

            List<String> dataSourceNamesList = dynamicDataSourceRule.getNames();

            primary = dynamicDataSourceRule.getPrimary();

            if(ListUtil.isNotEmpty(dataSourceNamesList)){

                for(int i = 0 ; i < dataSourceNamesList.size(); i ++){

                    String dataSourceName = dataSourceNamesList.get(i);

                    TransactionManagerConfigurationProperties transactionManagerConfigurationProperties = null;

                    try{

                        transactionManagerConfigurationProperties = binder.bind(
                                (DataSourceConfigrceConstants.SRPINGBOOT_DATASOURCE_TAG
                                + DataSourceConfigrceConstants.PROPERTIES_SPLIT_TAG
                                + dataSourceName + DataSourceConfigrceConstants.PROPERTIES_SPLIT_TAG
                                + DataSourceConfigrceConstants.PROPERTIES_TRANSACTION_TAG).trim(), TransactionManagerConfigurationProperties.class).get();

                    }catch (NoSuchElementException e){

                        logger.error("spring.datasource transaction is not in pops", e);

                    }

                    if(dataSourceName.equals(primary)) {

                        DynamicDataSourceBeanMapUtils.bulidDynamicDataSourceSpringBootRegisterModel(dataSourceName, true, transactionManagerConfigurationProperties);

                    }else {

                        DynamicDataSourceBeanMapUtils.bulidDynamicDataSourceSpringBootRegisterModel(dataSourceName, false, transactionManagerConfigurationProperties);

                    }

                    logger.debug("DynamicBootDataSourceRegister dataSourceName : " + dataSourceName);

                    dynamicDataSourceMap.put(dataSourceName, init(dataSourceName));

                }

            }

        }

    }


    private MasterSlaveDataSourceModel init(String dataSourceName){

        DynamicMasterSlaveConfigurationProperties dynamicMasterSlaveRule = binder.bind((getDataSourceDetailTag(dataSourceName)).trim(), DynamicMasterSlaveConfigurationProperties.class).get();

        String loadBalanceAlgorithmType = dynamicMasterSlaveRule.getLoadBalanceAlgorithmType();

        if(loadBalanceAlgorithmType != null){

            DynamicDataSourceBeanMapUtils.setLoadBalanceAlgorithmType(dataSourceName, loadBalanceAlgorithmType);

        }

        return buildDataSourceModel(initMasterDatasource(dataSourceName, dynamicMasterSlaveRule), initSlaveDatasource(dataSourceName, dynamicMasterSlaveRule));

    }

    private MasterSlaveDataSourceModel buildDataSourceModel(DataSourceModel masterDataSource,
                                                 List<DataSourceModel> slaveDataSource){

        MasterSlaveDataSourceModel masterSlaveDataSourceModel = new MasterSlaveDataSourceModel();

        masterSlaveDataSourceModel.setMasterDataSource(masterDataSource);

        masterSlaveDataSourceModel.setSlaveDataSource(slaveDataSource);

        return masterSlaveDataSourceModel;

    }

    private String getDataSourceDetailTag(String dataSourceName){

        return DataSourceConfigrceConstants.SRPINGBOOT_DATASOURCE_TAG +
                DataSourceConfigrceConstants.PROPERTIES_SPLIT_TAG +
                dataSourceName ;

    }

    private DataSourceModel initMasterDatasource(String dataSourceName, DynamicMasterSlaveConfigurationProperties dynamicMasterSlaveRule) {

        logger.debug("SpringBootConfiguration.setEnvironment masterSlaveRule: " + dynamicMasterSlaveRule);

        if (dynamicMasterSlaveRule != null) {

            String masterDataSourceName = dynamicMasterSlaveRule.getMasterDataSourceName();

            if (masterDataSourceName != null) {

                DataSourceModel masterDataSourceModel = new DataSourceModel();

                logger.debug("SpringBootConfiguration.setEnvironment masterDataSourceName: " + masterDataSourceName);

                Map<String, Object> masterDataSourceProps = binder.bind((getDataSourceDetailTag(dataSourceName)
                        + DataSourceConfigrceConstants.PROPERTIES_SPLIT_TAG + masterDataSourceName).trim(), Map.class).get();

                logger.debug("SpringBootConfiguration.setEnvironment masterDataSourceProps: " + masterDataSourceProps);

                DataSource mastertDataSource = DataSourceUtls.buildDataSource(masterDataSourceProps);

                DataSourceUtls.dataBinder(mastertDataSource,masterDataSourceProps);

                masterDataSourceModel.setDataSourceName(masterDataSourceName);

                masterDataSourceModel.setDataSource(mastertDataSource);

                DynamicDataSourceBeanMapUtils.addMasterDataSourceList(dataSourceName, masterDataSourceName);

                return masterDataSourceModel;

            }

        }

        return null;

    }

    private List<DataSourceModel> initSlaveDatasource(String dataSourceName, DynamicMasterSlaveConfigurationProperties dynamicMasterSlaveRule) {

        logger.debug("SpringBootConfiguration.setEnvironment masterSlaveRule: " + dynamicMasterSlaveRule);

        if (dynamicMasterSlaveRule != null) {

            List<String> slaveResourceList = dynamicMasterSlaveRule.getSlaveDataSourceNames();

            if (slaveResourceList != null) {

                List<DataSourceModel> slaveDataSourceList = new ArrayList<DataSourceModel>();

                List<String> slaveDataSourceNameList = new ArrayList<String>();

                logger.debug("SpringBootConfiguration.setEnvironment slaveResourceList: " + slaveResourceList.size());

                for (int i = 0; i < slaveResourceList.size(); i++) {

                    DataSourceModel slaveDataSourceModel = new DataSourceModel();

                    String slavedataSourceName = slaveResourceList.get(i);

                    logger.debug("SpringBootConfiguration.setEnvironment slavedataSourceName: " + slavedataSourceName);

                    Map<String, Object> salveDataSourceProps = binder.bind((getDataSourceDetailTag(dataSourceName)
                            + DataSourceConfigrceConstants.PROPERTIES_SPLIT_TAG + slavedataSourceName).trim(), Map.class).get();

                    logger.debug("SpringBootConfiguration.setEnvironment slaveDataSourceProps: " + salveDataSourceProps);

                    DataSource dsSlave = DataSourceUtls.buildDataSource(salveDataSourceProps);

                    DataSourceUtls.dataBinder(dsSlave,salveDataSourceProps);

                    slaveDataSourceModel.setDataSourceName(slavedataSourceName);

                    slaveDataSourceModel.setDataSource(dsSlave);

                    slaveDataSourceList.add(slaveDataSourceModel);

                    slaveDataSourceNameList.add(slavedataSourceName);

                    slaveDataSourceModel = null;

                }

                DynamicDataSourceBeanMapUtils.addSlaveDataSourceList(dataSourceName, slaveDataSourceNameList);

                return slaveDataSourceList;

            }

        }

        return null;

    }

    /**
     * 创建数据源
     * @param dataSourceName 配置文件里的数据源名称
     * @return
     */
    public DynamicDataSource getDynamicDataSource(String dataSourceName) {

        DynamicDataSource dynamicDataSource = new DynamicDataSource();

        DataSourceModel masterDataSource = dynamicDataSourceMap.get(dataSourceName).getMasterDataSource();

        Map<String, DataSource> slaveDataSourceMap = dynamicDataSourceMap.get(dataSourceName).getSlaveDataSourceMap();
        // 默认数据源
        dynamicDataSource.setDefaultTargetDataSource(masterDataSource.getDataSource());

        // 配置多数据源
        Map<Object, Object> dsMap = new HashMap();

        dsMap.put(masterDataSource.getDataSourceName(), masterDataSource.getDataSource());

        if(slaveDataSourceMap != null){

            dsMap.putAll(slaveDataSourceMap);

        }

        dynamicDataSource.setTargetDataSources(dsMap);

        return dynamicDataSource;

    }

    /**
     * 创建事务管理器
     * @param dynamicDataSource
     * @return
     */
    public PlatformTransactionManager dynamicTransactionManager(DataSource dynamicDataSource) {

        return new DataSourceTransactionManager(dynamicDataSource);

    }


    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {

        if(!dynamicDataSourceMap.isEmpty()){

            Iterator<String> iterator = dynamicDataSourceMap.keySet().iterator();

            while (iterator.hasNext()) {

                String dataSourceName = iterator.next();

                logger.debug("DynamicBootDataSourceRegister " +
                        "postProcessBeanDefinitionRegistry register dataSource dataSourceName : " + dataSourceName);

                DynamicDataSourceSpringBootRegisterModel dataSourceSpringBootRegisterModel
                        = DynamicDataSourceBeanMapUtils.getDynamicDataSourceSpringBootRegisterModel(dataSourceName);

                String dataSourceBeanName = dataSourceSpringBootRegisterModel.getDataSourceBeanName();

                logger.debug("DynamicBootDataSourceRegister " +
                        "postProcessBeanDefinitionRegistry register dataSource dataSourceBeanName : " + dataSourceBeanName);

                BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(DynamicDataSource.class, () -> {
                    // 这个方法可以定义这个bean的实例创建方式，如构造函数之后还想调用其他的方法，也可以在这里做
                    return getDynamicDataSource(dataSourceName);
                });


                if(dataSourceSpringBootRegisterModel.getPrimary()){

                    beanDefinitionBuilder.setPrimary(true);

                }

                BeanDefinition beanDefinition = beanDefinitionBuilder.getRawBeanDefinition();
                //注册bean定义
                beanDefinitionRegistry.registerBeanDefinition(dataSourceBeanName, beanDefinition);

            }

        }else{

            logger.error("DynamicBootDataSourceRegister " +
                    "postProcessBeanDefinitionRegistry register dataSource is error ");

        }

    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

        if(DataSourceConfigrceConstants.dyDataSourceBeanMap != null){

            Iterator<String> iterator = DataSourceConfigrceConstants.dyDataSourceBeanMap.keySet().iterator();

            while (iterator.hasNext()) {

                String dataSourceName = iterator.next();

                logger.debug("DynamicBootDataSourceRegister " +
                        "postProcessBeanFactory register TransactionManager dataSourceName : " + dataSourceName);

                DynamicDataSourceSpringBootRegisterModel dataSourceSpringBootRegisterModel
                        = DynamicDataSourceBeanMapUtils.getDynamicDataSourceSpringBootRegisterModel(dataSourceName);


                String dataSourceBeanName = dataSourceSpringBootRegisterModel.getDataSourceBeanName();

                logger.debug("DynamicBootDataSourceRegister " +
                        "postProcessBeanFactory register TransactionManager dataSourceBeanName : " + dataSourceBeanName);

                String transactionManagerBeanName = dataSourceSpringBootRegisterModel.getTransactionManagerBeanName();

                logger.debug("DynamicBootDataSourceRegister " +
                        "postProcessBeanFactory register TransactionManager transactionManagerBeanName : " + transactionManagerBeanName);

                BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(PlatformTransactionManager.class, () -> {

                    return dynamicTransactionManager(configurableListableBeanFactory.getBean(dataSourceBeanName, DataSource.class));

                });


                if(dataSourceSpringBootRegisterModel.getPrimary()){

                    builder.setPrimary(true);

                }

                BeanDefinition beanDefinition = builder.getRawBeanDefinition();

                ((DefaultListableBeanFactory) configurableListableBeanFactory).registerBeanDefinition(transactionManagerBeanName, beanDefinition);

            }

        }

    }

}
