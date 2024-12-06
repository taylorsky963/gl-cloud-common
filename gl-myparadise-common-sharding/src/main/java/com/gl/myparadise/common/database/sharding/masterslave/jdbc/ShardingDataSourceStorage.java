package com.gl.myparadise.common.database.sharding.masterslave.jdbc;

import java.util.ArrayList;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: ShardingDataSourceStorage
 * @ProjectName: myparadise-common
 * @Date 2023-05-12 - 0:39
 */
public class ShardingDataSourceStorage {

    private static String loadBalanceAlgorithmType = "";

    private static ArrayList<String> dataMasterSourceList = new ArrayList<String> ();

    private static ArrayList<String> dataSlaveSourceList = new ArrayList<String> ();


    public static ArrayList<String> getDataSlaveSourceList() {

        return dataSlaveSourceList;

    }

    public static ArrayList<String> getDataMasterSourceList() {
        return dataMasterSourceList;
    }

    public static String getLoadBalanceAlgorithmType() {
        return loadBalanceAlgorithmType;
    }

    public static void setLoadBalanceAlgorithmType(String loadBalanceAlgorithmType) {
        ShardingDataSourceStorage.loadBalanceAlgorithmType = loadBalanceAlgorithmType;
    }
}
