package com.gl.myparadise.common.database.aspect;

import com.gl.myparadise.common.database.annotation.DLDSAnn;
import com.gl.myparadise.common.database.constants.DataSourceConfigrceConstants;
import com.gl.myparadise.common.database.hold.DbContextHolder;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: GLDataSourceAspect
 * @ProjectName: myparadise-common
 * @Date 2023-05-16 - 1:09
 */
@Aspect
@Order(-1)// 保证该AOP在@Transactional之前执行
@Component
public class GLDataSourceAspect {

    private static final Logger logger = LoggerFactory.getLogger(GLDataSourceAspect.class);

    @Before("@annotation(dldsAnn)")
    public void setMasterSlaveDataSourceTags(JoinPoint joinPoint, DLDSAnn dldsAnn) throws Throwable {

        String dataSourceOpType = dldsAnn.opType();

        String dataSourceName = dldsAnn.opType();

        logger.debug("setMasterSlaveDataSourceTags DataSource : {} {} > {}", dldsAnn.opType(), dldsAnn.dataBase(), joinPoint.getSignature());

        if (DataSourceConfigrceConstants.MASTER_WRITE.equals(dataSourceOpType)) {

            DbContextHolder.setDataSourceOp(dataSourceName, DataSourceConfigrceConstants.MASTER_WRITE);

            logger.debug(" setMasterSlaveDataSourceTags set masterDataSourceTag");


        }

        if (DataSourceConfigrceConstants.SLAVE_READ.equals(dataSourceOpType)) {

            DbContextHolder.setDataSourceOp(dataSourceName, DataSourceConfigrceConstants.SLAVE_READ);

            logger.debug("setMasterSlaveDataSourceTags set  slaveDataSourceTag");

        }

        DbContextHolder.setDataSourceIndexList(DbContextHolder.getDataSourceAnnInfo());

    }

    @After("@annotation(dldsAnn)")
    public void restoreDataSource(JoinPoint point, DLDSAnn dldsAnn) {

        try {

            DbContextHolder.clearDataSourceIndexList();

            int dataSouceIndexNum = DbContextHolder.getDataSourceIndexList().size();

            logger.debug("restoreDataSource dataSouceIndexNum is : " + dataSouceIndexNum);

            if (dataSouceIndexNum > 0) {

                DbContextHolder.setDataSourceOp(DbContextHolder.getDataSourceIndexList().get(dataSouceIndexNum - 1));

            } else {

                logger.debug("restoreDataSource clearDataSource ");

                DbContextHolder.clearDataSource();

            }

        } catch (Exception e) {

            logger.error("ShardingDataSourceTagAspect restoreDataSource is error", e);

            DbContextHolder.clearDataSource();

        }

    }

}
