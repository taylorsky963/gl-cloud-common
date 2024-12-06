package com.gl.myparadise.common.database.constants;

import com.gl.myparadise.common.database.model.DynamicDataSourceSpringBootRegisterModel;
import com.gl.myparadise.common.database.model.DynamicDataSourceStorageModel;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: DataSourceConstants
 * @ProjectName: myparadise-common
 * @Date 2023-05-11 - 19:58
 */
public class DataSourceConfigrceConstants {

    public static final String PROPERTIES_SPLIT_TAG = ".";

    public static final String PACKAGE_SPLIT_TAG = "\\.";

    public static final String PACKAGE_WILDCARD_SYMBOL = "*";

    public static final String PACKAGE_WILDCARD_SYMBOL_DOUBLE = PACKAGE_WILDCARD_SYMBOL + PACKAGE_WILDCARD_SYMBOL;

    public static final String SRPINGBOOT_DATASOURCE_TAG = "spring.datasource";

    public static final String SHARDING_PROPERTIES_TAG = "sharding.jdbc.datasource";

    public final static String PROPERTIES_TRANSACTION_TAG = "transaction";

    public static final String SHARDING_MASTERSLAVE_TAG = "sharding.jdbc.config.masterslave";

    public static final String SRPINGBOOT_MASTERSLAVE_TAG = "masterslave";

    public final static String DATASOURCE_TYPE_DEFAULT = "org.apache.tomcat.jdbc.pool.DataSource";

    public static final String MYBATISC_CONFIG_CONFIGLOCATIONS_TAG = "configLocations";

    public static final String MYBATISC_CONFIG_TYPEALIASESPACKAGE_TAG = "typeAliasesPackage";

    public static final String MYBATISC_CONFIG_MAPPERLOCATIONS_TAG = "mapperLocations";

    public static final String MYBATISC_CONFIG_BASEPACKAGE_TAG = "basePackage";

    public static final String SHARDING_DATASOURCE_BEAN_NAME = "shardingDataSource";

    public static final String DYNAMIC_DATASOURCE_BEAN_NAME = "DynamicDataSource";

    public static final String DYNAMIC_SQLFACTORY_BEAN_NAME = "DynamicSqlSessionFactory";

    public static final String DYNAMIC_SQLSESSIONTEMPLATE_BEAN_NAME = "SqlSessionTemplate";

    public static final String DYNAMIC_MAPPERSCANNER_BEAN_NAME = "MapperScanner";

    public static final String SHARDING_TRANSACTION_BEAN_NAME = "shardingTransactionManager";

    public static final String DYNAMIC_TRANSACTION_BEAN_NAME = "TransactionManager";

    public static final String DYNAMIC_ADVISOR_BEAN_NAME = "Advisor";

    public static final String MYBATISC_CONFIG_TAG = "gl.mybatis";

    public static final String MYBATISC_CONFIG_SHARDING_TAG = MYBATISC_CONFIG_TAG
            + PROPERTIES_SPLIT_TAG + SHARDING_DATASOURCE_BEAN_NAME;

    public final static String MASTER_WRITE = "WRITE";

    public final static String SLAVE_READ = "READONLY";

    public final static String SLAVE_LOADBALANCE_TYPE_RANDOM = "RANDOM";

    public final static String SLAVE_LOADBALANCE_TYPE_ROBIN = "ROBIN";

    public final static Boolean MASTERSLAVE_SOURCE_TAG = false;

    public static Map<String, DynamicDataSourceSpringBootRegisterModel> dyDataSourceBeanMap =
            new HashMap<String, DynamicDataSourceSpringBootRegisterModel>();
}
