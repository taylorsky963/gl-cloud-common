package com.gl.myparadise.common.database.model;

import com.gl.myparadise.common.tools.util.ListUtil;
import org.apache.commons.lang3.StringUtils;

import javax.sql.DataSource;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: DataSourceModel
 * @ProjectName: myparadise-common
 * @Date 2023-05-14 - 23:17
 */
public class MasterSlaveDataSourceModel implements Serializable {

    private static final long serialVersionUID = -7089433105927912998L;

    //默认数据源名称
    private DataSourceModel masterDataSource;

    //读库数据源
    private List<DataSourceModel> slaveDataSource;

    public DataSourceModel getMasterDataSource() {
        return masterDataSource;
    }

    public void setMasterDataSource(DataSourceModel masterDataSource) {
        this.masterDataSource = masterDataSource;
    }

    public List<DataSourceModel> getSlaveDataSource() {
        return slaveDataSource;
    }

    public void setSlaveDataSource(List<DataSourceModel> slaveDataSource) {
        this.slaveDataSource = slaveDataSource;
    }

    public Map<String, DataSource> getSlaveDataSourceMap(){

        if(ListUtil.isNotEmpty(slaveDataSource)){

            Map<String, DataSource> dataMap = new HashMap<String, DataSource>();

            for(int i = 0 ; i < slaveDataSource.size(); i++){

                dataMap.put(slaveDataSource.get(i).getDataSourceName(), slaveDataSource.get(i).getDataSource());
            }

            return dataMap;

        }

        return null;

    }
}
