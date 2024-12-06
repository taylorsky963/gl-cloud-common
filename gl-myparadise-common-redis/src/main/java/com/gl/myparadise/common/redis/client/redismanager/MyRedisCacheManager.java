package com.gl.myparadise.common.redis.client.redismanager;

import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: MyRedisCacheManager
 * @Date 2023-05-09 - 0:50
 */
public class MyRedisCacheManager extends RedisCacheManager {

    private final RedisCacheWriter cacheWriter;

    private final RedisCacheConfiguration defaultCacheConfig;

    private Long nullFlushTime = 0L;

    /** 用于返回自定义的redisCache **/
    @Override
    protected RedisCache createRedisCache(String name, RedisCacheConfiguration cacheConfig) {

        MyParadiseRedisCache myParadiseRedisCache = new MyParadiseRedisCache(name,
                cacheWriter, cacheConfig != null ? cacheConfig : defaultCacheConfig);

        myParadiseRedisCache.setNullFlushTime(nullFlushTime);

        return myParadiseRedisCache;

    }

    public MyRedisCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfiguration) {

        super(cacheWriter, defaultCacheConfiguration);

        this.cacheWriter = cacheWriter;

        this.defaultCacheConfig = defaultCacheConfiguration;

    }

    public Long getNullFlushTime() {
        return nullFlushTime;
    }

    public void setNullFlushTime(Long nullFlushTime) {
        this.nullFlushTime = nullFlushTime;
    }
}

