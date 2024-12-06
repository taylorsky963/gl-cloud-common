package com.gl.myparadise.common.database.utils;

import com.gl.myparadise.common.database.constants.DataSourceConfigrceConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.ConfigurationPropertyName;
import org.springframework.boot.context.properties.source.ConfigurationPropertyNameAliases;
import org.springframework.boot.context.properties.source.ConfigurationPropertySource;
import org.springframework.boot.context.properties.source.MapConfigurationPropertySource;
import org.springframework.boot.jdbc.DataSourceBuilder;

import javax.sql.DataSource;
import java.util.Map;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: DataSourceUtls
 * @ProjectName: myparadise-common
 * @Date 2023-05-11 - 18:29
 */
public class DataSourceUtls {

    private static final Logger logger = LoggerFactory.getLogger(DataSourceUtls.class);

    private final static ConfigurationPropertyNameAliases aliases = new ConfigurationPropertyNameAliases(); //别名

    private DataSourceUtls(){};

    public static DataSource buildDataSource(Map<String, Object> dsMap) {

        try {

            String driverClassNameMapKey = "driver-class-name";

            Object classType = dsMap.get("type");

            if (classType == null){

                classType = DataSourceConfigrceConstants.DATASOURCE_TYPE_DEFAULT;// 默认DataSource

            }

            Class<? extends DataSource> dataSourceType;
            dataSourceType = (Class<? extends DataSource>) Class.forName((String) classType);

            if(dataSourceType.getTypeName().equals("com.alibaba.druid.pool.DruidDataSource")){

                driverClassNameMapKey = "driverClassName";

            }

            String driverClassName = dsMap.get(driverClassNameMapKey).toString();
            String url = dsMap.get("url").toString();
            String username = dsMap.get("username").toString();
            String password = dsMap.get("password").toString();

            DataSourceBuilder dataSourcefactory = DataSourceBuilder.create().driverClassName(driverClassName).url(url)
                    .username(username).password(password).type(dataSourceType);

            return dataSourcefactory.build();

        } catch (Exception ex) {

            logger.error("buildDataSource", ex);

        }

        return null;
    }


    public static void dataBinder(DataSource dataSource, Map<String, Object> dataSourceMap) {

        ConfigurationPropertySource source = new MapConfigurationPropertySource(dataSourceMap);

        Binder binderEx = new Binder(source.withAliases(aliases));

        binderEx.bind(ConfigurationPropertyName.EMPTY, Bindable.ofInstance(dataSource));  //将参数绑定到对象

    }
}
