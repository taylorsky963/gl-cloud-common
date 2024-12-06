package com.gl.myparadise.common.redis.client.service.impl;

import com.gl.myparadise.common.redis.client.service.RedisLockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: RedisLockServiceImpl
 * @Date 2023-05-08 - 22:24
 */
@Service
public class RedisLockServiceImpl implements RedisLockService {


    private static final Long RELEASE_SUCCESS = 1L;

    private static final String LOCK_SUCCESS = "OK";

    private RedisSerializer<String> argsSerializer  = new StringRedisSerializer();
    private RedisSerializer<String> resultSerializer = new StringRedisSerializer();

    @Autowired
    private RedisTemplate redisTemplate;

    //上锁脚本
    private static final String LOCK_LUA = "if redis.call('setnx', KEYS[1], ARGV[1]) == 1 then redis.call('expire', KEYS[1], ARGV[2]) return 'true' else return 'false' end";
    //解锁脚本
    private static final String UNLOCK_LUA = "if redis.call('get', KEYS[1]) == ARGV[1] then redis.call('del', KEYS[1]) end return 'true' ";

    /**
     * [lockKey 锁住的key, lockExpireMils 锁住的时长。如果超时未解锁，视为加锁线程死亡，其他线程可夺取锁]
     * @param lockKey
     * @param lockExpireMils
     * @return
     */
//    public boolean lock(String lockKey, long lockExpireMils) {
//
//        return (Boolean) redisTemplate.execute((RedisCallback) connection -> {
//
//            long nowTime = System.currentTimeMillis();
//
//            Boolean acquire = connection.setNX(lockKey.getBytes(), String.valueOf(nowTime + lockExpireMils + 1).getBytes());
//
//            if (acquire) {
//
//                return Boolean.TRUE;
//
//            } else {
//
//                byte[] value = connection.get(lockKey.getBytes());
//
//                if (Objects.nonNull(value) && value.length > 0) {
//
//                    long oldTime = Long.parseLong(new String(value));
//
//                    if (oldTime < nowTime) {
//                        //connection.getSet：返回这个key的旧值并设置新值。
//                        byte[] oldValue = connection.getSet(lockKey.getBytes(), String.valueOf(nowTime + lockExpireMils + 1).getBytes());
//                        //当key不存时会返回空，表示key不存在或者已在管道中使用
//                        return oldValue == null ? false : Long.parseLong(new String(oldValue)) < nowTime;
//                    }
//                }
//            }
//
//            return Boolean.FALSE;
//
//        });
//    }

    /**
     * 通过key和val值解锁，只有满足key值和val值相等才能解锁，防止被其他线程并发解锁
     * @param lockKey
     * @param requestId
     */
    public void releaseDistributedLock(String lockKey, String requestId) {

        RedisScript unLockRedisScript = RedisScript.of(UNLOCK_LUA, String.class);

        List<String> keys = Collections.singletonList(lockKey);

        redisTemplate.execute(unLockRedisScript, argsSerializer, resultSerializer, keys, requestId);

    }


    /**
     * [lockKey锁住的key, val key值，确保唯一, second 过期时间，防止死锁]
     * @param lockKey
     * @param requestId
     * @param expireTime
     * @return
     */
    public boolean tryGetDistributedLock(String lockKey, String requestId, int expireTime) {

        RedisScript lockRedisScript = RedisScript.of(LOCK_LUA, String.class);

        List<String> keys = Collections.singletonList(lockKey);

        String flag = (String)redisTemplate.execute(lockRedisScript, argsSerializer, resultSerializer, keys, requestId, String.valueOf(expireTime));

        return Boolean.valueOf(flag);

    }

}
