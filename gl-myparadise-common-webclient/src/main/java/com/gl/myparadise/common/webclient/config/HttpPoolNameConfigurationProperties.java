package com.gl.myparadise.common.webclient.config;

import java.util.List;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: GLHttpPoolConfigurationProperties
 * @ProjectName: gl-common
 * @Date 2023-06-22 - 21:36
 */
public class HttpPoolNameConfigurationProperties {

    private List<String> names;

    public List<String> getNames() {
        return names;
    }

    public void setNames(List<String> names) {
        this.names = names;
    }
}
