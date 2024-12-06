package com.gl.myparadise.common.database.dynamic.masterslave.util;

import com.gl.myparadise.common.database.constants.DataSourceConfigrceConstants;
import com.gl.myparadise.common.database.dynamic.masterslave.config.TransactionManagerConfigurationProperties;
import com.gl.myparadise.common.database.model.DynamicDataSourceSpringBootRegisterModel;
import com.gl.myparadise.common.database.model.DynamicDataSourceStorageModel;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: DynamicDataSourceBeanMapUtils
 * @ProjectName: myparadise-common
 * @Date 2023-05-15 - 15:04
 */
public class DynamicDataSourceBeanMapUtils {

    private DynamicDataSourceBeanMapUtils(){};

    public static void setLoadBalanceAlgorithmType(String dataSourceName, String loadBalanceAlgorithmType){

        DynamicDataSourceStorageModel dynamicDataSourceStorageModel = getDynamicDataSourceStorageModel(dataSourceName);

        dynamicDataSourceStorageModel.setLoadBalanceAlgorithmType(loadBalanceAlgorithmType);

        setDynamicDataSourceStorageModel(dataSourceName, dynamicDataSourceStorageModel);
    }


    public static void addMasterDataSourceList(String dataSourceName, String masterDataSourceName){

        DynamicDataSourceStorageModel dynamicDataSourceStorageModel = getDynamicDataSourceStorageModel(dataSourceName);

        dynamicDataSourceStorageModel.addMasterDataSourceList(masterDataSourceName);

        setDynamicDataSourceStorageModel(dataSourceName, dynamicDataSourceStorageModel);

    }

    public static void addSlaveDataSourceList(String dataSourceName, List<String> slaveDataSourceNameList){

        DynamicDataSourceStorageModel dynamicDataSourceStorageModel = getDynamicDataSourceStorageModel(dataSourceName);

        dynamicDataSourceStorageModel.addAllSlaveDataSourceList(slaveDataSourceNameList);

        setDynamicDataSourceStorageModel(dataSourceName, dynamicDataSourceStorageModel);

    }

    public static DynamicDataSourceSpringBootRegisterModel getDynamicDataSourceSpringBootRegisterModel(String dataSourceName){

        DynamicDataSourceSpringBootRegisterModel dataSourceSpringBootRegisterModel
                = DataSourceConfigrceConstants.dyDataSourceBeanMap.get(dataSourceName);

        if(dataSourceSpringBootRegisterModel == null){

            dataSourceSpringBootRegisterModel = new DynamicDataSourceSpringBootRegisterModel();

            dataSourceSpringBootRegisterModel.setDataSourceName(dataSourceName);

            DataSourceConfigrceConstants.dyDataSourceBeanMap.put(dataSourceName, dataSourceSpringBootRegisterModel);

        }

        return dataSourceSpringBootRegisterModel;

    }

    private static void setDynamicDataSourceSpringBootRegisterModel(String dataSourceName, DynamicDataSourceSpringBootRegisterModel dataSourceSpringBootRegisterModel){

        DataSourceConfigrceConstants.dyDataSourceBeanMap.put(dataSourceName, dataSourceSpringBootRegisterModel);

    }

    public static void bulidDynamicDataSourceSpringBootRegisterModel(String dataSourceName, boolean isPrimary, TransactionManagerConfigurationProperties transactionManagerConfigurationProperties){

        DynamicDataSourceSpringBootRegisterModel dynamicDataSourceSpringBootRegisterModel
                = getDynamicDataSourceSpringBootRegisterModel(dataSourceName);

        dynamicDataSourceSpringBootRegisterModel.setDataSourceBeanName(dataSourceName
                + DataSourceConfigrceConstants.DYNAMIC_DATASOURCE_BEAN_NAME);

        dynamicDataSourceSpringBootRegisterModel.setMapperScannerBeanName(dataSourceName
                + DataSourceConfigrceConstants.DYNAMIC_MAPPERSCANNER_BEAN_NAME);

        dynamicDataSourceSpringBootRegisterModel.setSqlSessionTemplateBeanName(dataSourceName
                + DataSourceConfigrceConstants.DYNAMIC_SQLSESSIONTEMPLATE_BEAN_NAME);

        dynamicDataSourceSpringBootRegisterModel.setSqlSessionFactoryBeanName(dataSourceName
                + DataSourceConfigrceConstants.DYNAMIC_SQLFACTORY_BEAN_NAME);

        if(transactionManagerConfigurationProperties != null){

            dynamicDataSourceSpringBootRegisterModel.setTransactionManagerBasePackages(transactionManagerConfigurationProperties.getBasePackages());

            if(StringUtils.isNotEmpty(transactionManagerConfigurationProperties.getName())){

                dynamicDataSourceSpringBootRegisterModel.setTransactionManagerBeanName(transactionManagerConfigurationProperties.getName());

            }else{

                dynamicDataSourceSpringBootRegisterModel.setTransactionManagerBeanName(dataSourceName
                        + DataSourceConfigrceConstants.DYNAMIC_TRANSACTION_BEAN_NAME);
            }

        }else{

            dynamicDataSourceSpringBootRegisterModel.setTransactionManagerBeanName(dataSourceName
                    + DataSourceConfigrceConstants.DYNAMIC_TRANSACTION_BEAN_NAME);

        }


        dynamicDataSourceSpringBootRegisterModel.setPrimary(isPrimary);

        setDynamicDataSourceSpringBootRegisterModel(dataSourceName, dynamicDataSourceSpringBootRegisterModel);
    }

    public static DynamicDataSourceStorageModel getDynamicDataSourceStorageModel(String dataSourceName){

        return getDynamicDataSourceSpringBootRegisterModel(dataSourceName).getDynamicDataSourceStorageModel();

    }

    public static void setDynamicDataSourceStorageModel(String dataSourceName, DynamicDataSourceStorageModel dynamicDataSourceStorageModel){

        DynamicDataSourceSpringBootRegisterModel dynamicDataSourceSpringBootRegisterModel = getDynamicDataSourceSpringBootRegisterModel(dataSourceName);

        dynamicDataSourceSpringBootRegisterModel.setDynamicDataSourceStorageModel(dynamicDataSourceStorageModel);

        setDynamicDataSourceSpringBootRegisterModel(dataSourceName, dynamicDataSourceSpringBootRegisterModel);

    }

    public static String getDynamicAdvisorName(String dataSourceName){

        return dataSourceName + DataSourceConfigrceConstants.DYNAMIC_ADVISOR_BEAN_NAME;

    }

    public static Map<String, List<String>> getTransactionManagerBasePackagesMap (){

        Iterator<String> iterator = DataSourceConfigrceConstants.dyDataSourceBeanMap.keySet().iterator();

        Map<String, List<String>> transactionManagerBasePackagesMap = new HashMap<String, List<String>>();

        while (iterator.hasNext()) {

            String dataSourceName = iterator.next();

            DynamicDataSourceSpringBootRegisterModel dynamicDataSourceSpringBootRegisterModel
                    = DataSourceConfigrceConstants.dyDataSourceBeanMap.get(dataSourceName);

            transactionManagerBasePackagesMap.put(dataSourceName, dynamicDataSourceSpringBootRegisterModel.getTransactionManagerBasePackages());

        }

        return transactionManagerBasePackagesMap;

    }

}
