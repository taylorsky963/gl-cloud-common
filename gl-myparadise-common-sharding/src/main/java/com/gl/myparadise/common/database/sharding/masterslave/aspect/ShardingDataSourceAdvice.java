package com.gl.myparadise.common.database.sharding.masterslave.aspect;

import com.gl.myparadise.common.database.constants.DataSourceConfigrceConstants;
import com.gl.myparadise.common.database.hold.DbContextHolder;
import io.shardingsphere.api.HintManager;
import io.shardingsphere.core.hint.HintManagerHolder;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Vincent
 * @Description: sharding 读写分离拦截器
 * @ClassName: DynamicDataSourceAdvice
 * @ProjectName: myparadise-common
 * @Date 2023-05-11 - 13:22
 */
public class ShardingDataSourceAdvice implements MethodInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(ShardingDataSourceAdvice.class);

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {

        String dataSourceOptype = DbContextHolder.getDataSourceOpType();

        if (StringUtils.isNotEmpty(dataSourceOptype)) {

            if (DataSourceConfigrceConstants.SLAVE_READ.equals(dataSourceOptype)) {

                if(HintManagerHolder.isMasterRouteOnly()){

                    HintManagerHolder.clear();

                }

                logger.debug(" DataSource is slaveSource ");

            }else{

                logger.debug(" DataSource is masterSource  ");

                HintManager.getInstance().setMasterRouteOnly();

            }

        }

        Object result = methodInvocation.proceed();

        return result;

    }
}
