package com.gl.myparadise.common.database.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class DynamicDataSourceStorageModel  implements Serializable {

    private static final long serialVersionUID = -3404712983616951602L;

    private String loadBalanceAlgorithmType = "";

    private ArrayList<String> masterDataSourceList = new ArrayList<String> ();

    private ArrayList<String> slaveDataSourceList = new ArrayList<String> ();


    public ArrayList<String> getSlaveDataSourceList() {

        return this.slaveDataSourceList;

    }

    public ArrayList<String> getMasterDataSourceList() {

        return this.masterDataSourceList;

    }

    public void addSlaveDataSourceList(String slaveSourceName) {

        this.slaveDataSourceList.add(slaveSourceName);

    }

    public void addAllSlaveDataSourceList(List<String> slaveSourceNameList) {

        this.slaveDataSourceList.addAll(slaveSourceNameList);

    }

    public void addMasterDataSourceList(String masterSourceName) {

        this.masterDataSourceList.add(masterSourceName);

    }

    public String getLoadBalanceAlgorithmType() {
        return this.loadBalanceAlgorithmType;
    }

    public void setLoadBalanceAlgorithmType(String loadBalanceAlgorithmType) {

        this.loadBalanceAlgorithmType = loadBalanceAlgorithmType;

    }
}
