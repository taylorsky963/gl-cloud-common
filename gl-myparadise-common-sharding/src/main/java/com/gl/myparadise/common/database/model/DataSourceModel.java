package com.gl.myparadise.common.database.model;

import org.apache.commons.lang3.StringUtils;

import javax.sql.DataSource;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: DataSourceModel
 * @ProjectName: myparadise-common
 * @Date 2023-05-14 - 23:31
 */
public class DataSourceModel implements Serializable {

    private static final long serialVersionUID = 2095176283023224577L;

    private String dataSourceName;

    private DataSource dataSource;

    private String dataSourceOpType;

    public String getDataSourceName() {
        return dataSourceName;
    }

    public void setDataSourceName(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public String getDataSourceOpType() {
        return dataSourceOpType;
    }

    public void setDataSourceOpType(String dataSourceOpType) {
        this.dataSourceOpType = dataSourceOpType;
    }

    public Map<String, DataSource> getDataSourceMap() {

        if (StringUtils.isNotEmpty(dataSourceName) && dataSource != null) {

            Map<String, DataSource> dataMap = new HashMap<String, DataSource>();

            dataMap.put(dataSourceName, dataSource);

            return dataMap;

        }

        return null;

    }
}
