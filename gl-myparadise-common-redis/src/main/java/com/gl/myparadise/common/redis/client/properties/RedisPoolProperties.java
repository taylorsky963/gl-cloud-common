package com.gl.myparadise.common.redis.client.properties;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: RedisPoolProperties
 * @Date 2023-05-09 - 3:22
 */
public class RedisPoolProperties {

    //最大连接数（使用负值表示没有限制）
    private int maxTotal=8;
    // 连接池中的最大空闲连接
    private int maxIdle=30;
    //最小空闲连接数
    private int minIdle=10;
    //获取连接时的最大等待毫秒数,小于零:阻塞不确定的时间,默认-1
    private Long maxWaitMillis= 3000L;
    //每次释放连接的最大数目
    private int numTestsPerEvictionRun=10;
    //释放连接的扫描间隔（毫秒）
    private Long timeBetweenEvictionRunsMillis = 3000L;
    //连接最小空闲时间
    private Long minEvictableIdleTimeMillis = 1800000L;
    //连接空闲多久后释放, 当空闲时间>该值 且 空闲连接>最大空闲连接数 时直接释放
    private Long softMinEvictableIdleTimeMillis=10000L;
    //在获取连接的时候检查有效性, 默认false
    private boolean testOnBorrow=Boolean.TRUE;
    //在空闲时检查有效性, 默认false
    private boolean testWhileIdle=Boolean.TRUE;
    //在归还给pool时，是否提前进行validate操作
    private  boolean testOnReturn=Boolean.TRUE;
    //连接耗尽时是否阻塞, false报异常,ture阻塞直到超时, 默认true
    private boolean blockWhenExhausted=Boolean.FALSE;
    // 连接超时时间（毫秒）
    private int timeout=5000;

    private boolean jmxEnabled=Boolean.FALSE;

    private String jmxNamePrefix = "pool";

    private boolean lifo=Boolean.TRUE;

    //设置的逐出策略类名, 默认DefaultEvictionPolicy(当连接超过最大空闲时间,或连接数超过最大空闲连接数)
    private String evictionPolicyClassName = "org.apache.commons.pool2.impl.DefaultEvictionPolicy";

    public int getMaxTotal() {
        return maxTotal;
    }

    public void setMaxTotal(int maxTotal) {
        this.maxTotal = maxTotal;
    }

    public int getMaxIdle() {
        return maxIdle;
    }

    public void setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
    }

    public int getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(int minIdle) {
        this.minIdle = minIdle;
    }

    public int getNumTestsPerEvictionRun() {
        return numTestsPerEvictionRun;
    }

    public void setNumTestsPerEvictionRun(int numTestsPerEvictionRun) {
        this.numTestsPerEvictionRun = numTestsPerEvictionRun;
    }



    public boolean isTestOnBorrow() {
        return testOnBorrow;
    }

    public void setTestOnBorrow(boolean testOnBorrow) {
        this.testOnBorrow = testOnBorrow;
    }

    public boolean isTestWhileIdle() {
        return testWhileIdle;
    }

    public void setTestWhileIdle(boolean testWhileIdle) {
        this.testWhileIdle = testWhileIdle;
    }

    public boolean isTestOnReturn() {
        return testOnReturn;
    }

    public void setTestOnReturn(boolean testOnReturn) {
        this.testOnReturn = testOnReturn;
    }

    public boolean isBlockWhenExhausted() {
        return blockWhenExhausted;
    }

    public void setBlockWhenExhausted(boolean blockWhenExhausted) {
        this.blockWhenExhausted = blockWhenExhausted;
    }

    public Long getMaxWaitMillis() {
        return maxWaitMillis;
    }

    public void setMaxWaitMillis(Long maxWaitMillis) {
        this.maxWaitMillis = maxWaitMillis;
    }

    public String getEvictionPolicyClassName() {
        return evictionPolicyClassName;
    }

    public void setEvictionPolicyClassName(String evictionPolicyClassName) {
        this.evictionPolicyClassName = evictionPolicyClassName;
    }

    public boolean isJmxEnabled() {
        return jmxEnabled;
    }

    public void setJmxEnabled(boolean jmxEnabled) {
        this.jmxEnabled = jmxEnabled;
    }

    public String getJmxNamePrefix() {
        return jmxNamePrefix;
    }

    public void setJmxNamePrefix(String jmxNamePrefix) {
        this.jmxNamePrefix = jmxNamePrefix;
    }

    public Long getTimeBetweenEvictionRunsMillis() {
        return timeBetweenEvictionRunsMillis;
    }

    public void setTimeBetweenEvictionRunsMillis(Long timeBetweenEvictionRunsMillis) {
        this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
    }

    public Long getMinEvictableIdleTimeMillis() {
        return minEvictableIdleTimeMillis;
    }

    public void setMinEvictableIdleTimeMillis(Long minEvictableIdleTimeMillis) {
        this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
    }

    public Long getSoftMinEvictableIdleTimeMillis() {
        return softMinEvictableIdleTimeMillis;
    }

    public void setSoftMinEvictableIdleTimeMillis(Long softMinEvictableIdleTimeMillis) {
        this.softMinEvictableIdleTimeMillis = softMinEvictableIdleTimeMillis;
    }

    public boolean isLifo() {
        return lifo;
    }

    public void setLifo(boolean lifo) {
        this.lifo = lifo;
    }
}
