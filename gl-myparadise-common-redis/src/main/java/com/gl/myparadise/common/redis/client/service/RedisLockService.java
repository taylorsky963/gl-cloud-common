package com.gl.myparadise.common.redis.client.service;

public interface RedisLockService {

    void releaseDistributedLock(String lockKey, String requestId);

    boolean tryGetDistributedLock(String lockKey, String requestId, int expireTime);
}
