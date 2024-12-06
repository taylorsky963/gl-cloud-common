package com.gl.myparadise.common.database.sharding.masterslave.jdbc;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.jdbc.metadata.DataSourcePoolMetadata;
import org.springframework.boot.jdbc.metadata.DataSourcePoolMetadataProvider;
import org.springframework.boot.jdbc.metadata.HikariDataSourcePoolMetadata;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Vincent
 * @Description: 解决新版Spring中,健康健康检查用到 sharding jdbc 时,该组件没有完全实现MySQL驱动导致的问题.
 * @ClassName: JpaConfiguration
 * @ProjectName: myparadise-common
 * @Date 2023-05-12 - 1:02
 */
@Configuration
public class JpaConfiguration {

    @Bean
    DataSourcePoolMetadataProvider dataSourcePoolMetadataProvider() {
        return dataSource -> dataSource instanceof HikariDataSource
                // 这里如果所使用的数据源没有对应的 DataSourcePoolMetadata 实现的话也可以全部使用 NotAvailableDataSourcePoolMetadata
                ? new HikariDataSourcePoolMetadata((HikariDataSource) dataSource)
                : new NotAvailableDataSourcePoolMetadata();
    }


    private static class NotAvailableDataSourcePoolMetadata implements DataSourcePoolMetadata {

        @Override
        public Float getUsage() {
            return null;
        }

        @Override
        public Integer getActive() {
            return null;
        }

        @Override
        public Integer getMax() {
            return null;
        }

        @Override
        public Integer getMin() {
            return null;
        }

        @Override
        public String getValidationQuery() {
            // 该字符串是适用于MySQL的简单查询语句,用于检查检查,其他数据库可能需要更换
            return "select 1";
        }

        @Override
        public Boolean getDefaultAutoCommit() {
            return null;
        }
    }
}
