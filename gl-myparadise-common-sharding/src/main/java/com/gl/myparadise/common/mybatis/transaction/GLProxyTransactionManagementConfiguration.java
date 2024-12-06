package com.gl.myparadise.common.mybatis.transaction;

import com.gl.myparadise.common.database.constants.DataSourceConfigrceConstants;
import com.gl.myparadise.common.database.dynamic.masterslave.register.DynamicBootDataSourceRegister;
import com.gl.myparadise.common.database.dynamic.masterslave.util.DynamicDataSourceBeanMapUtils;
import com.gl.myparadise.common.database.sharding.masterslave.config.ShardingMasterSlaveConfigurationProperties;
import com.gl.myparadise.common.tools.util.ListUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.transaction.annotation.AnnotationTransactionAttributeSource;
import org.springframework.transaction.annotation.ProxyTransactionManagementConfiguration;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttributeSource;

import javax.annotation.Nullable;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author Vincent
 * @Description: 事务拦截器，用于多数据源的时候 配合 标签spring.datasource.productdb.transaction.base-packages
 * @ClassName: GLProxyTransactionManagementConfiguration
 * @ProjectName: myparadise-common
 * @Date 2023-05-16 - 2:07
 */
@Configuration
@EnableConfigurationProperties({ShardingMasterSlaveConfigurationProperties.class})
public class GLProxyTransactionManagementConfiguration  extends ProxyTransactionManagementConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(GLProxyTransactionManagementConfiguration.class);

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    @Override
    public TransactionAttributeSource transactionAttributeSource() {
        return new AnnotationTransactionAttributeSource() {
            @Override
            public TransactionAttribute getTransactionAttribute(Method method, @Nullable Class<?> targetClass) {

                DefaultTransactionAttribute attribute = (DefaultTransactionAttribute) super.getTransactionAttribute(method, targetClass);

                if (attribute != null && StringUtils.isEmpty(attribute.getQualifier())) {

                    String classFullName = method.getDeclaringClass().getName();

                    String transactionManagementName = getTransactionManagementName(classFullName);

                    logger.debug("GLProxyTransactionManagementConfiguration transactionAttributeSource transactionManagementName : " + transactionManagementName);

                    if(StringUtils.isNotEmpty(transactionManagementName)){

                        attribute.setQualifier(transactionManagementName);

                    }else{

                        attribute.setQualifier("");
                    }

                }

                return attribute;

            }
        };
    }

    private String getTransactionManagementName(String classFullName){

        logger.debug("GLProxyTransactionManagementConfiguration.getTransactionManagementName classFullName : " + classFullName);

        Map<String, List<String>> transactionManagerBasePackagesMap
                = DynamicDataSourceBeanMapUtils.getTransactionManagerBasePackagesMap();

        String transactionManagementName = null;

        Iterator<String> iterator = transactionManagerBasePackagesMap.keySet().iterator();

        while (iterator.hasNext()) {

            String dataSourceName = iterator.next();

            List<String> transactionManagerBasePackage = transactionManagerBasePackagesMap.get(dataSourceName);

            if(ListUtil.isNotEmpty(transactionManagerBasePackage)){

                if(isCheckTransactionManagerBasePackages(transactionManagerBasePackage, classFullName)){

                    transactionManagementName = DynamicDataSourceBeanMapUtils.getDynamicDataSourceSpringBootRegisterModel(dataSourceName).getTransactionManagerBeanName();

                    break;
                }
            }
        }

        return transactionManagementName;

    }


    private boolean isCheckTransactionManagerBasePackages(List<String> transactionManagerBasePackage, String classFullName){

        boolean rs = false;

        List<String> classPackageList = Arrays.asList(classFullName.split(DataSourceConfigrceConstants.PACKAGE_SPLIT_TAG));

        if(ListUtil.isNotEmpty(transactionManagerBasePackage)){

            for(int i = 0 ; i < transactionManagerBasePackage.size(); i++){

                String basePackage = transactionManagerBasePackage.get(i);

                if(StringUtils.isNotEmpty(basePackage)){

                    List<String> packageList = Arrays.asList(basePackage.split(DataSourceConfigrceConstants.PACKAGE_SPLIT_TAG));

                    if(equal(classPackageList, packageList)){

                        rs = true;

                        break;

                    }

                }

            }

        }

        return rs;
    }

    private static boolean equal(List<String> classPackageList, List<String> basePackageList){

        int basePackageIndex = 0;

        int classPackageIndex = 0;

        boolean rs = true;

        boolean notDoubleSymbol = true;

        while(basePackageIndex < basePackageList.size() && classPackageIndex < classPackageList.size()){

            String classPackage = classPackageList.get(classPackageIndex);

            String basePackage = basePackageList.get(basePackageIndex);

            classPackageIndex = classPackageIndex + 1;

            if(DataSourceConfigrceConstants.PACKAGE_WILDCARD_SYMBOL.equals(basePackage)){

                basePackageIndex = basePackageIndex + 1;

                continue;

            }else if (DataSourceConfigrceConstants.PACKAGE_WILDCARD_SYMBOL_DOUBLE
                    .equals(basePackage)){

                basePackageIndex = basePackageIndex + 1;

                notDoubleSymbol = false ;

                continue;

            }

            if(classPackage.equals(basePackage)){

                basePackageIndex = basePackageIndex + 1;

                notDoubleSymbol = true;

            }else{

                if(!notDoubleSymbol && (classPackageIndex < classPackageList.size())){

                    continue;

                }else{

                    rs = false;

                    break;

                }

            }

        }

        return rs;

    }

}
