package com.gl.myparadise.common.database.dynamic.masterslave.aspect;

import com.gl.myparadise.common.database.constants.DataSourceConfigrceConstants;
import com.gl.myparadise.common.database.dynamic.masterslave.loadbalance.MasterSlaveLoadBalanceRule;
import com.gl.myparadise.common.database.dynamic.masterslave.util.DynamicDataSourceBeanMapUtils;
import com.gl.myparadise.common.database.hold.DbContextHolder;
import com.gl.myparadise.common.database.model.DataSourceModel;
import com.gl.myparadise.common.database.model.DynamicDataSourceStorageModel;
import com.gl.myparadise.common.tools.util.ListUtil;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: DynamicDataSourceAdvice
 * @ProjectName: myparadise-common
 * @Date 2023-05-16 - 11:34
 */
public class DynamicDataSourceAdvice  implements MethodInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(DynamicDataSourceAdvice.class);

    private DynamicDataSourceStorageModel dynamicDataSourceStorageModel;

    private String dataSourceName;

    public DynamicDataSourceAdvice(String dataSourceName){

        this.dataSourceName = dataSourceName;

    }

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {

        DataSourceModel dataSourceModel = DbContextHolder.getDataSourceAnnInfo();

        if (dataSourceModel != null) {

            String methodName = methodInvocation.getMethod().getName();

            logger.debug("DynamicDataSourceAdvice methodName : " + methodName);

            dynamicDataSourceStorageModel = DynamicDataSourceBeanMapUtils
                    .getDynamicDataSourceStorageModel(dataSourceName);

            if(dynamicDataSourceStorageModel != null) {

                if (DataSourceConfigrceConstants.MASTER_WRITE.equals(dataSourceModel.getDataSourceOpType())) {

                    setMasterDataSource();

                }

                if (DataSourceConfigrceConstants.SLAVE_READ.equals(dataSourceModel.getDataSourceOpType())) {

                    List<String> slaveDataSourceList = dynamicDataSourceStorageModel.getSlaveDataSourceList();

                    if(ListUtil.isNotEmpty(slaveDataSourceList)) {

                        String dataSourceSlaveName = slaveDataSourceList.
                                get(MasterSlaveLoadBalanceRule.getInstance().getMasterSlaveLoadBalance(slaveDataSourceList.size(),
                                        dynamicDataSourceStorageModel.getLoadBalanceAlgorithmType()));

                        logger.info("SLAVE_READ DataSource" + dataSourceSlaveName);

                        DbContextHolder.setDataSourceOp(dataSourceSlaveName, DataSourceConfigrceConstants.SLAVE_READ);

                    }else {

                        logger.error("slave数据源配置不正确，slaveDataSourceNum is 0 ");

                        logger.error("重新设定主库数据源");

                        setMasterDataSource();

                    }

                }

            }
        }

        Object result = methodInvocation.proceed();

        return result;

    }


    private void setMasterDataSource() {


        int masterSourceNum = dynamicDataSourceStorageModel.getMasterDataSourceList().size();

        if (masterSourceNum == 1) {

            String dataSourceMasterName = dynamicDataSourceStorageModel.getMasterDataSourceList().get(0);

            logger.info("MASTER_WRITE DataSource" + dataSourceMasterName);

            DbContextHolder.setDataSourceOp(dataSourceMasterName, DataSourceConfigrceConstants.MASTER_WRITE);

        } else {

            logger.error("MASTER数据源配置不正确，masterSourceNum : " + masterSourceNum);

        }

    }
}
