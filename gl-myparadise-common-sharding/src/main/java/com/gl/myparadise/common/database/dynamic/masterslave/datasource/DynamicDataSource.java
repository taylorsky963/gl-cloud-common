package com.gl.myparadise.common.database.dynamic.masterslave.datasource;

import com.gl.myparadise.common.database.hold.DbContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.Map;


public class DynamicDataSource extends AbstractRoutingDataSource {

    private static final Logger logger = LoggerFactory.getLogger(DynamicDataSource.class);

    public DynamicDataSource DynamicDataSource(DataSource mastertDataSource, Map<Object, Object> dsMap){

        setDefaultTargetDataSource(mastertDataSource);

        setTargetDataSources(dsMap);

        return this;
    }

    @Override
    protected Object determineCurrentLookupKey() {

        String dataSource = DbContextHolder.getDataSource();

        logger.info("get CurrentLookUpdataSource is : " + dataSource);

        return dataSource;

    }

}
