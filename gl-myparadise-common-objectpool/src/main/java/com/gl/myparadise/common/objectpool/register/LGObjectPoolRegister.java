package com.gl.myparadise.common.objectpool.register;

import com.gl.myparadise.common.objectpool.config.MultipleObjectPoolProperties;
import com.gl.myparadise.common.objectpool.config.ObjectPoolConfigurationProperties;
import com.gl.myparadise.common.objectpool.constants.ObjectPoolConfigConstants;
import com.gl.myparadise.common.objectpool.service.PoolObjectProviderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.apache.commons.pool2.*;
import org.apache.commons.pool2.impl.*;

import javax.annotation.PreDestroy;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Vincent
 * @description:
 * @className: LGObjectPoolRegister
 * @projectName: gl-cloud-base
 * @date 2025-03-25 - 02:46
 */
@Component
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(prefix = ObjectPoolConfigConstants.OBJECT_POOL_CONFIG_TAG, name = "names")
public class LGObjectPoolRegister implements SmartInitializingSingleton, EnvironmentAware, ApplicationContextAware {

    private Environment environment;

    private Binder binder;

    private ApplicationContext applicationContext;

    private final Map<String, GenericObjectPool<Object>> registeredPools = new ConcurrentHashMap<>();

    private final Map<String, Class<?>> poolTypeMap = new ConcurrentHashMap<>();


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        this.applicationContext = applicationContext;
    }

    @Override
    public void setEnvironment(Environment environment) {

        this.environment = environment;

        binder = Binder.get(environment); //绑定配置器

        bulidThreadPoolConfigPropsMap();
    }

    private void bulidThreadPoolConfigPropsMap() {

        MultipleObjectPoolProperties objectPoolConfigModel = binder.bind((ObjectPoolConfigConstants.OBJECT_POOL_CONFIG_TAG).trim(), MultipleObjectPoolProperties.class).get();

        if (objectPoolConfigModel != null) {

            List<String> objectPoolNamesList = objectPoolConfigModel.getNames();

            if (objectPoolNamesList != null && !objectPoolNamesList.isEmpty()) {

                for (int i = 0; i < objectPoolNamesList.size(); i++) {

                    String objectPoolName = objectPoolNamesList.get(i);

                    log.debug("LGThreadPoolRegister bulidThreadPoolConfigPropsMap threadPoolName : " + objectPoolName);

                    ObjectPoolConfigConstants.objectPoolMap.put(objectPoolName, bulidObjectPoolConfigurationProperties(objectPoolName));

                }
            }

        }

    }

    private ObjectPoolConfigurationProperties bulidObjectPoolConfigurationProperties(String objectPoolName) {

        return binder.bind((ObjectPoolConfigConstants.OBJECT_POOL_CONFIG_TAG + ObjectPoolConfigConstants.PROPERTIES_SPLIT_TAG + objectPoolName).trim(), ObjectPoolConfigurationProperties.class).get();

    }

    @Override
    public void afterSingletonsInstantiated() {

        if (!ObjectPoolConfigConstants.objectPoolMap.isEmpty()) {

            Iterator<String> iterator = ObjectPoolConfigConstants.objectPoolMap.keySet().iterator();

            while (iterator.hasNext()) {

                String objectPoolMapName = iterator.next();

                ObjectPoolConfigurationProperties objectPoolConfigurationProperties = ObjectPoolConfigConstants.objectPoolMap.get(objectPoolMapName);

                String objectPoolName = objectPoolConfigurationProperties.getName();

                PoolObjectProviderService provider = (PoolObjectProviderService) applicationContext.getBean(objectPoolConfigurationProperties.getType());

                Object target = provider.provide();

                GenericObjectPool<Object> pool = new GenericObjectPool<>(new FixedInstanceFactory(target), buildGenericObjectPoolConfig(objectPoolConfigurationProperties));

                ((ConfigurableApplicationContext) applicationContext)
                        .getBeanFactory()
                        .registerSingleton(objectPoolName, pool);

                registeredPools.put(objectPoolName, pool);

                // ✅ 记录类型（targetBean 是实际对象）
                poolTypeMap.put(objectPoolName, target.getClass());
            };

        }
    }

    private GenericObjectPoolConfig buildGenericObjectPoolConfig(ObjectPoolConfigurationProperties objectPoolConfigurationProperties) {

        GenericObjectPoolConfig<Object> genericObjectPoolConfig = new GenericObjectPoolConfig<>();

        genericObjectPoolConfig.setLifo(objectPoolConfigurationProperties.isLifo());

        genericObjectPoolConfig.setFairness(objectPoolConfigurationProperties.isFairness());

        genericObjectPoolConfig.setMaxWaitMillis(objectPoolConfigurationProperties.getMaxWaitMillis());

        genericObjectPoolConfig.setMaxTotal(objectPoolConfigurationProperties.getMaxTotal());

        genericObjectPoolConfig.setMaxIdle(objectPoolConfigurationProperties.getMaxIdle());

        genericObjectPoolConfig.setMinIdle(objectPoolConfigurationProperties.getMinIdle());

        genericObjectPoolConfig.setTimeBetweenEvictionRunsMillis(objectPoolConfigurationProperties.getTimeBetweenEvictionRunsMillis());

        genericObjectPoolConfig.setNumTestsPerEvictionRun(objectPoolConfigurationProperties.getNumTestsPerEvictionRun());

        genericObjectPoolConfig.setSoftMinEvictableIdleTimeMillis(objectPoolConfigurationProperties.getSoftMinEvictableIdleTimeMillis());

        genericObjectPoolConfig.setMinEvictableIdleTimeMillis(objectPoolConfigurationProperties.getMinEvictableIdleTimeMillis());

        genericObjectPoolConfig.setTestOnCreate(objectPoolConfigurationProperties.isTestOnCreate());

        genericObjectPoolConfig.setTestOnBorrow(objectPoolConfigurationProperties.isTestOnBorrow());

        genericObjectPoolConfig.setTestOnReturn(objectPoolConfigurationProperties.isTestOnReturn());

        genericObjectPoolConfig.setTestWhileIdle(objectPoolConfigurationProperties.isTestWhileIdle());

        return genericObjectPoolConfig;
    }

    @PreDestroy
    public void destroyAllPools() {

        registeredPools.forEach((name, pool) -> {

            try {

                pool.close();

                log.info("✅ 已销毁连接池: " + name);

            } catch (Exception e) {

                log.error("❌ 关闭连接池失败: " + name, e);

            }
        });
    }

    static class FixedInstanceFactory extends BasePooledObjectFactory<Object> {

        private final Object delegate;

        FixedInstanceFactory(Object delegate) {
            this.delegate = delegate;
        }

        @Override
        public Object create() {
            return delegate;
        }

        @Override
        public PooledObject<Object> wrap(Object obj) {

            return new DefaultPooledObject<>(obj);

        }
    }

    /**
     * 根据对象类型查找对应的连接池（支持类型兼容，例如父类或接口）
     *
     * @param targetType 想要借出的对象类型
     * @return 匹配的连接池（GenericObjectPool），找不到则返回 null
     */
    public GenericObjectPool<?> findPoolByType(Class<?> targetType) {

        for (Map.Entry<String, Class<?>> entry : poolTypeMap.entrySet()) {

            String poolName = entry.getKey();

            Class<?> actualType = entry.getValue();

            // ✅ 判断是否兼容类型（支持接口/父类匹配）
            if (targetType.isAssignableFrom(actualType)) {

                return registeredPools.get(poolName);
            }
        }

        return null;
    }

    /**
     * 根据传入的连接池名列表，销毁并重建对应的连接池
     *
     * @param objectPoolList 要重建的连接池名列表
     */
    public synchronized void rebuildPools(List<String> objectPoolList) {

        if (objectPoolList == null || objectPoolList.isEmpty()) {

            log.error("⚠️ rebuildPools 调用时传入的 pool 名为空，跳过重建");

            return;
        }

        for (String poolKey : objectPoolList) {

            ObjectPoolConfigurationProperties config = ObjectPoolConfigConstants.objectPoolMap.get(poolKey);

            if (config == null) {

                log.error("⚠️ 未找到配置，跳过连接池重建: {}", poolKey);

                continue;

            }

            String poolName = config.getName();

            // ✅ 1. 销毁旧连接池
            GenericObjectPool<Object> oldPool = registeredPools.get(poolName);

            if (oldPool != null) {

                try {

                    oldPool.close();

                    registeredPools.remove(poolName);

                    poolTypeMap.remove(poolName);

                    log.info("♻️ 已销毁连接池: {}", poolName);

                } catch (Exception e) {

                    log.error("❌ 销毁连接池失败: {}", poolName, e);

                    continue;

                }

            }

            // ✅ 2. 初始化新对象
            PoolObjectProviderService provider = (PoolObjectProviderService) applicationContext.getBean(config.getType());

            Object target = provider.provide();

            // ✅ 3. 创建新连接池
            GenericObjectPool<Object> newPool = new GenericObjectPool<>(
                    new FixedInstanceFactory(target),
                    buildGenericObjectPoolConfig(config)
            );

            ((ConfigurableApplicationContext) applicationContext)
                    .getBeanFactory()
                    .registerSingleton(poolName, newPool);

            registeredPools.put(poolName, newPool);

            poolTypeMap.put(poolName, target.getClass());

            log.info("✅ 连接池已重建并注册: {}", poolName);

        }
    }

}
