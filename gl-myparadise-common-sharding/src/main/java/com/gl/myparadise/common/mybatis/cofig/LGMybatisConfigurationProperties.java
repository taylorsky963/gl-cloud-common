package com.gl.myparadise.common.mybatis.cofig;

import com.gl.myparadise.common.database.constants.DataSourceConfigrceConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author Vincent
 * @Description: mybatis 配置文件属性类
 * @ClassName: LGMybatisConfigurationProperties
 * @ProjectName: myparadise-common
 * @Date 2023-05-12 - 18:39
 */
public class LGMybatisConfigurationProperties {

    private Environment environment;

    private List<String> dataSourceName = new ArrayList<>();

    public List<String> getDataSourceName() {
        return dataSourceName;
    }

    public void setDataSourceName(List<String> dataSourceName) {
        this.dataSourceName = dataSourceName;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public String getTypeAliasesPackage(String dataSourceName){

        return environment.getProperty(DataSourceConfigrceConstants.MYBATISC_CONFIG_TAG
                + DataSourceConfigrceConstants.PROPERTIES_SPLIT_TAG
                + dataSourceName + DataSourceConfigrceConstants.PROPERTIES_SPLIT_TAG
                + DataSourceConfigrceConstants.MYBATISC_CONFIG_TYPEALIASESPACKAGE_TAG);

    }

    public String getConfigLocations(String dataSourceName){

        return environment.getProperty(DataSourceConfigrceConstants.MYBATISC_CONFIG_TAG
                + DataSourceConfigrceConstants.PROPERTIES_SPLIT_TAG
                + dataSourceName + DataSourceConfigrceConstants.PROPERTIES_SPLIT_TAG
                + DataSourceConfigrceConstants.MYBATISC_CONFIG_CONFIGLOCATIONS_TAG);
    }

    public String getMapperLocations(String dataSourceName){

        return environment.getProperty(DataSourceConfigrceConstants.MYBATISC_CONFIG_TAG
                + DataSourceConfigrceConstants.PROPERTIES_SPLIT_TAG
                + dataSourceName + DataSourceConfigrceConstants.PROPERTIES_SPLIT_TAG
                + DataSourceConfigrceConstants.MYBATISC_CONFIG_MAPPERLOCATIONS_TAG);

    }

    public String getBasePackage(String dataSourceName){

        return environment.getProperty(DataSourceConfigrceConstants.MYBATISC_CONFIG_TAG +
                DataSourceConfigrceConstants.PROPERTIES_SPLIT_TAG
                + dataSourceName +  DataSourceConfigrceConstants.PROPERTIES_SPLIT_TAG +
                DataSourceConfigrceConstants.MYBATISC_CONFIG_BASEPACKAGE_TAG);

    }
}
