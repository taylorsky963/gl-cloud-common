package com.gl.myparadise.common.database.dynamic.masterslave.config;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: DynamicDataSourceConfigurationProperties
 * @ProjectName: myparadise-common
 * @Date 2023-05-14 - 22:03
 */
public class DynamicDataSourceConfigurationProperties {

    private List<String> names = new ArrayList<>();

    private String primary;

    public String getPrimary() {
        return primary;
    }

    public void setPrimary(String primary) {
        this.primary = primary;
    }

    public List<String> getNames() {
        return names;
    }

    public void setNames(List<String> names) {
        this.names = names;
    }
}
