package com.gl.myparadise.common.mybatis.transaction;

import com.gl.myparadise.common.database.dynamic.masterslave.util.DynamicDataSourceBeanMapUtils;
import com.gl.myparadise.common.database.model.DynamicDataSourceSpringBootRegisterModel;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Properties;

/**
 * @author Vincent
 * @Description: 拦截SQL 暂时没有业务逻辑，后面再添加
 * @ClassName: DynamicTransactionInterceptor
 * @ProjectName: myparadise-common
 * @Date 2023-05-17 - 20:27
 */
@Intercepts(value = {
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})
//        ,
//        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})
}
)
public class DynamicTransactionInterceptor implements Interceptor {

    private static final Logger logger = LoggerFactory.getLogger(DynamicTransactionInterceptor.class);

    private String dataSourceName;

    public DynamicTransactionInterceptor(String dataSourceName) {

        this.dataSourceName = dataSourceName;
    }


    @Override
    public Object intercept(Invocation invocation) throws Throwable {

//        Object[] args = invocation.getArgs();
//
//        MappedStatement statement = (MappedStatement) args[0];
//
//        DynamicDataSourceSpringBootRegisterModel dynamicDataSourceSpringBootRegisterModel = DynamicDataSourceBeanMapUtils.getDynamicDataSourceSpringBootRegisterModel(dataSourceName);
//
//        if(TransactionSynchronizationManager.isActualTransactionActive() && StringUtils.isEmpty(TransactionSynchronizationManager.getCurrentTransactionName())){
//
//            TransactionSynchronizationManager.setCurrentTransactionName(dynamicDataSourceSpringBootRegisterModel.getTransactionManagerBeanName());
//
//        }
//
//        logger.debug("DynamicTransactionInterceptor DynamicDataSourceSpringBootRegisterModel : " + dynamicDataSourceSpringBootRegisterModel.toString());
//
//        logger.debug("DynamicTransactionInterceptor getDatabaseId : " + statement.getDatabaseId());

        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        logger.debug(properties.toString());
    }
}
