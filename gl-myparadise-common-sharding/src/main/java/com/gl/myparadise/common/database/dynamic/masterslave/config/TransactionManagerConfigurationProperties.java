package com.gl.myparadise.common.database.dynamic.masterslave.config;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: TransactionManagerProperties
 * @ProjectName: myparadise-common
 * @Date 2023-05-18 - 0:57
 */
public class TransactionManagerConfigurationProperties {

    private String name;

    private List<String> basePackages = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getBasePackages() {
        return basePackages;
    }

    public void setBasePackages(List<String> basePackages) {
        this.basePackages = basePackages;
    }
}
