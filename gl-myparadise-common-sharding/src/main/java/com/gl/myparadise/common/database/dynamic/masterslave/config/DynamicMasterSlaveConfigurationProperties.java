package com.gl.myparadise.common.database.dynamic.masterslave.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


public class DynamicMasterSlaveConfigurationProperties {

    private String masterDataSourceName;

    private List<String> slaveDataSourceNames = new ArrayList<>();

    private String loadBalanceAlgorithmType;

    private Properties props = new Properties();


    public List<String> getSlaveDataSourceNames() {
        return slaveDataSourceNames;
    }

    public void setSlaveDataSourceNames(List<String> slaveDataSourceNames) {
        this.slaveDataSourceNames = slaveDataSourceNames;
    }

    public String getLoadBalanceAlgorithmType() {
        return loadBalanceAlgorithmType;
    }

    public void setLoadBalanceAlgorithmType(String loadBalanceAlgorithmType) {
        this.loadBalanceAlgorithmType = loadBalanceAlgorithmType;
    }

    public Properties getProps() {
        return props;
    }

    public void setProps(Properties props) {
        this.props = props;
    }

    public String getMasterDataSourceName() {
        return masterDataSourceName;
    }

    public void setMasterDataSourceName(String masterDataSourceName) {
        this.masterDataSourceName = masterDataSourceName;
    }
}
