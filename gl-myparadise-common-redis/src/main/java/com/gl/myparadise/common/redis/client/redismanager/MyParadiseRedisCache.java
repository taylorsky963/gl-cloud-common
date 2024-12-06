package com.gl.myparadise.common.redis.client.redismanager;

import com.gl.myparadise.common.bean.model.ServiceRsModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.support.NullValue;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheWriter;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: MyParadiseRedisCache
 * @Date 2023-05-09 - 0:52
 */
public class MyParadiseRedisCache extends RedisCache {

    private static final Logger logger = LoggerFactory.getLogger(MyParadiseRedisCache.class);

    private RedisCacheWriter redisCacheWriter;

    private RedisCacheConfiguration configuration;
    //校验规则：获取时间
    String REGEX_STR = ".*\\#\\d+$";

    private Long nullFlushTime = 0L;

    private static final String Splitter="#";
    /**
     * Create new {@link RedisCache}.
     *
     * @param name        must not be {@literal null}.
     * @param cacheWriter must not be {@literal null}.
     * @param cacheConfig must not be {@literal null}.
     */
    protected MyParadiseRedisCache(String name, RedisCacheWriter cacheWriter, RedisCacheConfiguration cacheConfig) {
        super(name, cacheWriter, cacheConfig);
        redisCacheWriter = cacheWriter;
        configuration =cacheConfig;
    }

    /**
     * 重写cache put 逻辑，引入自定义TTL 实现
     * 实现逻辑:
     * 1.通过获取@Cacheable 中的value ,然后根据约定好的特殊字符进行分割
     * 2.从分割结果集中获取设置的TTL 时间并将value 中的，然后给当前缓存设置TTL
     *
     * @param key
     * @param value
     */
    @Override
    public void put(Object key, Object value) {

        String name = super.getName();

        logger.debug("MyParadiseRedisCache put basekey : " + name);

        //获取插入的值
        Object cacheValue = preProcessCacheValue(value);

        boolean isLogicNull = getRsObjModelSuccess(cacheValue);

        logger.debug("MyParadiseRedisCache put isLogicNull : " + isLogicNull);

        logger.debug("MyParadiseRedisCache put isAllowNullValues : " + isAllowNullValues());

        logger.debug("MyParadiseRedisCache put isCacheValueNull : " + isCacheValueNull(cacheValue));

        logger.debug("MyParadiseRedisCache put nullFlushTime : " + nullFlushTime);

        //如果允许插入NULL值，并且值为NULL 或者 值为失败的值 即 封装类里的 success 为FALSE
        if(isAllowNullValues() && (isCacheValueNull(cacheValue) || !isLogicNull) && nullFlushTime > 0){

            redisCacheWriter.put(name, serializeCacheKey(super.createCacheKey(key)), serializeCacheValue(cacheValue), Duration.ofSeconds(nullFlushTime));

        }else{

            if (Pattern.matches(REGEX_STR, name)) {

                List<String> keyList = Arrays.asList(name.split(Splitter));
                //获取键值
                String finalName = keyList.get(0);

                logger.debug("MyParadiseRedisCache put finalName : " + finalName);

                //获取TTL 执行时间
                Long ttl = Long.valueOf(keyList.get(1));

                //获取缓存value

                //获取value 为null 时，抛出异常
                if (!isAllowNullValues() && isCacheValueNull(cacheValue)) {
                    throw new IllegalArgumentException(String.format(
                            "Cache '%s' does not allow 'null' values. Avoid storing null via '@Cacheable(unless=\"#result == null\")' or configure RedisCache to allow 'null' via RedisCacheConfiguration.",
                            name));

                }

                //插入时添加时间
                logger.debug("MyParadiseRedisCache put finalName : " + finalName);

                logger.debug("MyParadiseRedisCache put key : " + key);

                logger.debug("MyParadiseRedisCache put ttl : " + ttl);

                redisCacheWriter.put(finalName, serializeCacheKey(createCacheKey(key)), serializeCacheValue(cacheValue), Duration.ofSeconds(ttl));

            }else{

                super.put(key, value);

            }

        }

    }

    private boolean isCacheValueNull(Object cacheValue){

        if(cacheValue == null || cacheValue == NullValue.INSTANCE){

            return true;

        }

        return false;
    }


    private boolean getRsObjModelSuccess(Object cacheValue){

       if(cacheValue != null){

            if(cacheValue.getClass().equals(ServiceRsModel.class)){

                return ((ServiceRsModel) cacheValue).isSuccess();
            }

       }

        return true;

    }


    private Long getCacheTimeOut(Object cacheValue, Long ttl){

        Long ttlDefault = 60L;

        if(nullFlushTime > 0){

            ttlDefault = nullFlushTime;

        }

        if(cacheValue != null){

            if(cacheValue.getClass().equals(ServiceRsModel.class)){

                if(!((ServiceRsModel) cacheValue).isSuccess()){

                    ttl = ttlDefault;

                }
            }


        }

        return ttl;
    }


    protected String createMyParadiseCacheKey(Object key) {

        String convertedKey = convertKey(key);

        if (!configuration.usePrefix()) {

            return convertedKey;

        }
        return prefixCacheKey(convertedKey);
    }


    private String prefixCacheKey(String key) {
        String name = super.getName();
        if (Pattern.matches(REGEX_STR, name)) {
            List<String> keyList = Arrays.asList(name.split(Splitter));
            String finalName = keyList.get(0);
            return configuration.getKeyPrefixFor(finalName) + key;
        }
        // allow contextual cache names by computing the key prefix on every call.
        return configuration.getKeyPrefixFor(name) + key;
    }

    //配置文件里配置，如果没有配置默认60L
    public Long getNullFlushTime() {
        return nullFlushTime;
    }

    public void setNullFlushTime(Long nullFlushTime) {
        this.nullFlushTime = nullFlushTime;
    }
}
