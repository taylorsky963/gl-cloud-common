package com.gl.myparadise.common.redis.client.properties;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: RedisProperties
 * @Date 2023-05-09 - 3:31
 */
public class RedisProperties {

    //Redis服务器地址
    private String host;
    //Redis服务器连接端口
    private int port;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
