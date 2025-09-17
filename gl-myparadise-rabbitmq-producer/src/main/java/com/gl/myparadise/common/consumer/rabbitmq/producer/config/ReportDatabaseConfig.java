package com.gl.myparadise.common.consumer.rabbitmq.producer.config;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * RabbitMQ Producer 模块专用的数据库配置
 * 使用 Apache Commons DBCP2 连接池，适配 Sharding-JDBC 配置
 * 只有在配置了 sharding.jdbc.datasource.master.url 时才会初始化
 */
@Configuration
@ConditionalOnProperty(name = "sharding.jdbc.datasource.master.url")
public class ReportDatabaseConfig {

    @Value("${sharding.jdbc.datasource.master.url}")
    private String url;

    @Value("${sharding.jdbc.datasource.master.username}")
    private String username;

    @Value("${sharding.jdbc.datasource.master.password}")
    private String password;

    @Value("${sharding.jdbc.datasource.master.driver-class-name:com.mysql.cj.jdbc.Driver}")
    private String driverClassName;

    /**
     * RabbitMQ Producer 专用的数据源
     * 使用 Apache Commons DBCP2 连接池
     * 配置：最大2连接，最小0连接，空闲超时60秒
     */
    @Bean(name = "rabbitmqProducerDataSource")
    public DataSource rabbitmqProducerDataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        
        // 基本连接配置
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setDriverClassName(driverClassName);
        
        // 连接池配置 - 轻量级连接池模式
        dataSource.setMaxTotal(2);                    // 最大连接数：2
        dataSource.setMinIdle(0);                     // 最小空闲连接：0
        dataSource.setMaxIdle(2);                     // 最大空闲连接：2
        dataSource.setInitialSize(0);                 // 初始连接数：0
        
        // 超时配置
        dataSource.setMaxWaitMillis(30000);           // 最大等待时间：30秒
        dataSource.setMinEvictableIdleTimeMillis(60000); // 空闲超时：60秒
        dataSource.setTimeBetweenEvictionRunsMillis(30000); // 空闲检查间隔：30秒
        
        // 连接验证
        dataSource.setValidationQuery("SELECT 1");    // 验证查询
        dataSource.setTestOnBorrow(true);             // 借用时验证
        dataSource.setTestWhileIdle(true);            // 空闲时验证
        dataSource.setTestOnReturn(false);            // 归还时不验证
        
        // 连接泄漏检测
        dataSource.setRemoveAbandonedOnBorrow(true);  // 借用时移除废弃连接
        dataSource.setRemoveAbandonedTimeout(60);     // 废弃超时：60秒
        dataSource.setLogAbandoned(true);             // 记录废弃连接日志
        
        return dataSource;
    }
}
