package com.gl.myparadise.common.database.model;

import com.gl.myparadise.common.tools.util.ListUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: DynamicDataSourceRegisterModel
 * @ProjectName: myparadise-common
 * @Date 2023-05-15 - 1:51
 */
public class DynamicDataSourceSpringBootRegisterModel  implements Serializable {

    private static final long serialVersionUID = 8807038123722655650L;

    private DynamicDataSourceStorageModel dynamicDataSourceStorageModel = new DynamicDataSourceStorageModel();

    private  String dataSourceBeanName;

    private  String dataSourceName;

    private  String sqlSessionFactoryBeanName;

    private  String sqlSessionTemplateBeanName;

    private  String transactionManagerBeanName;

    private List<String> transactionManagerBasePackages = new ArrayList<>();

    private  String mapperScannerBeanName;

    private Boolean isPrimary = false;

    public List<String> getTransactionManagerBasePackages() {
        return transactionManagerBasePackages;
    }

    public void setTransactionManagerBasePackages(List<String> transactionManagerBasePackages) {

        if(ListUtil.isNotEmpty(transactionManagerBasePackages)){

            this.transactionManagerBasePackages.addAll(transactionManagerBasePackages);

        }

    }

    public Boolean getPrimary() {
        return isPrimary;
    }

    public void setPrimary(Boolean primary) {
        isPrimary = primary;
    }

    public String getMapperScannerBeanName() {
        return mapperScannerBeanName;
    }

    public void setMapperScannerBeanName(String mapperScannerBeanName) {
        this.mapperScannerBeanName = mapperScannerBeanName;
    }

    public DynamicDataSourceStorageModel getDynamicDataSourceStorageModel() {
        return dynamicDataSourceStorageModel;
    }

    public void setDynamicDataSourceStorageModel(DynamicDataSourceStorageModel dynamicDataSourceStorageModel) {
        this.dynamicDataSourceStorageModel = dynamicDataSourceStorageModel;
    }

    public String getDataSourceBeanName() {
        return dataSourceBeanName;
    }

    public void setDataSourceBeanName(String dataSourceBeanName) {
        this.dataSourceBeanName = dataSourceBeanName;
    }

    public String getDataSourceName() {
        return dataSourceName;
    }

    public void setDataSourceName(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }

    public String getSqlSessionFactoryBeanName() {
        return sqlSessionFactoryBeanName;
    }

    public void setSqlSessionFactoryBeanName(String sqlSessionFactoryBeanName) {
        this.sqlSessionFactoryBeanName = sqlSessionFactoryBeanName;
    }

    public String getSqlSessionTemplateBeanName() {
        return sqlSessionTemplateBeanName;
    }

    public void setSqlSessionTemplateBeanName(String sqlSessionTemplateBeanName) {
        this.sqlSessionTemplateBeanName = sqlSessionTemplateBeanName;
    }

    public String getTransactionManagerBeanName() {
        return transactionManagerBeanName;
    }

    public void setTransactionManagerBeanName(String transactionManagerBeanName) {
        this.transactionManagerBeanName = transactionManagerBeanName;
    }

}
