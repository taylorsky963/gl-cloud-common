package com.gl.myparadise.common.redis.client.properties;

import com.gl.myparadise.common.redis.client.constants.LGRedisConstants;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: RedisClusterProperties
 * @Date 2023-05-09 - 2:40
 */
@ConfigurationProperties(prefix = LGRedisConstants.REDIS_PROPERTIES_PREFX)
public class RedisClusterProperties {

    //Redis数据库索引（默认为0）
    private String database;
    //Redis服务器连接密码（默认为空）
    private String password;

    private int connectionTimeout = 5000;

    private int maxAttempts = 5;

    private int soTimeout = 5000;

    private int runmodel = 0;

    private int flushTime = 300;

    private int redirects = 1;

    private Long nullFlushTime = 60L;

    private List<RedisProperties> cluster;

    private RedisPoolProperties pool;

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<RedisProperties> getCluster() {
        return cluster;
    }

    public void setCluster(List<RedisProperties> cluster) {
        this.cluster = cluster;
    }

    public RedisPoolProperties getPool() {
        return pool;
    }

    public void setPool(RedisPoolProperties pool) {
        this.pool = pool;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public int getMaxAttempts() {
        return maxAttempts;
    }

    public void setMaxAttempts(int maxAttempts) {
        this.maxAttempts = maxAttempts;
    }

    public int getSoTimeout() {
        return soTimeout;
    }

    public void setSoTimeout(int soTimeout) {
        this.soTimeout = soTimeout;
    }

    public int getRunmodel() {
        return runmodel;
    }

    public void setRunmodel(int runmodel) {
        this.runmodel = runmodel;
    }

    public int getFlushTime() {
        return flushTime;
    }

    public void setFlushTime(int flushTime) {
        this.flushTime = flushTime;
    }

    public Long getNullFlushTime() {
        return nullFlushTime;
    }

    public void setNullFlushTime(Long nullFlushTime) {
        this.nullFlushTime = nullFlushTime;
    }

    public int getRedirects() {
        return redirects;
    }

    public void setRedirects(int redirects) {
        this.redirects = redirects;
    }
}
