package com.gl.myparadise.common.objectpool.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author Vincent
 * @description:
 * @className: ObjectPoolConfigurationProperties
 * @projectName: gl-cloud-base
 * @date 2025-03-24 - 20:22
 */
@Data
@NoArgsConstructor
@ToString(callSuper = true)
public class ObjectPoolConfigurationProperties implements Serializable {

    private static final long serialVersionUID = -2808072486052815617L;

    private String name;

    private String type;

    private boolean lifo = true;

    private boolean fairness = false;

    private long maxWaitMillis = -1L;

    private int maxTotal = 10;

    private int maxIdle = 5;

    private int minIdle = 0;

    private long timeBetweenEvictionRunsMillis = -1L;

    private int numTestsPerEvictionRun = 3;

    private long softMinEvictableIdleTimeMillis = -1L;

    private long minEvictableIdleTimeMillis = 1800000L;

    private boolean testOnCreate = false;

    private boolean testOnBorrow = false;

    private boolean testOnReturn = false;

    private boolean testWhileIdle = false;


}
