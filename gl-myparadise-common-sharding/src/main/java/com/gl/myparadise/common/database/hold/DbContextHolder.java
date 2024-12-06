package com.gl.myparadise.common.database.hold;

import com.gl.myparadise.common.database.model.DataSourceModel;

import java.util.ArrayList;
import java.util.List;


public class DbContextHolder {

    //线程本地环境
    private static final ThreadLocal<DataSourceModel> contextHolders = new ThreadLocal<DataSourceModel>();

    private static final ThreadLocal<List<DataSourceModel>> dataSourceIndexList = new ThreadLocal<List<DataSourceModel>>();
//    //数据源列表
//    public static List<String> dataSourceIds = new ArrayList<>();

    //设置数据源
    public static void setDataSourceOp(String dataSourceName, String dataSourceOpType) {

        DataSourceModel dataSourceModel = new DataSourceModel();

        dataSourceModel.setDataSourceOpType(dataSourceOpType);

        dataSourceModel.setDataSourceName(dataSourceName);

        setDataSourceOp(dataSourceModel);
    }

    //设置数据源
    public static void setDataSourceOp(DataSourceModel dataSourceModel) {

        contextHolders.set(dataSourceModel);

    }

    //获取数据源
    public static String getDataSource() {

        DataSourceModel dataSourceModel = contextHolders.get();

        if(dataSourceModel != null){

            return (String) contextHolders.get().getDataSourceName();

        }else {

            return null;

        }

    }

    //获取数据源
    public static String getDataSourceOpType() {

        DataSourceModel dataSourceModel = contextHolders.get();

        if(dataSourceModel != null){

            return (String) contextHolders.get().getDataSourceOpType();

        }else {

            return null;

        }

    }

    //获取标签信息
    public static DataSourceModel getDataSourceAnnInfo() {

        return contextHolders.get();
    }

    //设置数据源注解的INDEX
    public static void setDataSourceIndexList(String dataSourceName, String dataSourceOpType) {

        List<DataSourceModel>  dataSourceList = dataSourceIndexList.get();

        if(dataSourceList == null ){

            dataSourceList = new ArrayList<DataSourceModel>();

        }
        DataSourceModel dataSourceModel = new DataSourceModel();

        dataSourceModel.setDataSourceOpType(dataSourceOpType);

        dataSourceModel.setDataSourceName(dataSourceName);

        setDataSourceIndexList(dataSourceModel);

    }

    //设置数据源注解的INDEX
    public static void setDataSourceIndexList(DataSourceModel dataSourceModel) {

        List<DataSourceModel>  dataSourceList = dataSourceIndexList.get();

        if(dataSourceList == null ){

            dataSourceList = new ArrayList<DataSourceModel>();

        }

        dataSourceList.add(dataSourceModel);

        dataSourceIndexList.set(dataSourceList);

    }

    //获取数据源注解的CLASS信息
    public static List<DataSourceModel> getDataSourceIndexList() {

        return dataSourceIndexList.get();

    }

    //清除数据源注解的CLASS信息
    public static void clearDataSourceIndexList() {

        int number = dataSourceIndexList.get().size();

        if(number > 0){

            dataSourceIndexList.get().remove(number-1);

        }

    }
    //清除数据源
    public static void clearDataSource() {

        dataSourceIndexList.remove();

        contextHolders.remove();

    }
//
//    public static boolean containsDataSource(String dataSourceId){
//        return dataSourceIds.contains(dataSourceId);
//    }


}
