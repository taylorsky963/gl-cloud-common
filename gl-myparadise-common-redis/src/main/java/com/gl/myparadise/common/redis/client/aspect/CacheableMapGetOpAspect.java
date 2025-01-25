package com.gl.myparadise.common.redis.client.aspect;

import com.gl.myparadise.common.bean.model.ServiceRsModel;
import com.gl.myparadise.common.redis.client.annotation.CacheableMapGetOp;
import com.gl.myparadise.common.redis.client.constants.LGRedisConstants;
import com.gl.myparadise.common.redis.client.service.RedisclientService;
import com.gl.myparadise.common.redis.client.utils.AspertUtil;
import com.gl.myparadise.common.redis.client.utils.CacheKeyUtil;
import com.gl.myparadise.common.redis.client.utils.CacheableKeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


/**
 * @author Vincent
 * @description:
 * @className: CacheableMapGetOpAspect
 * @projectName: gl-new-cloud-base
 * @date 2025-01-22 - 03:19
 */
@Aspect
@Slf4j
@Component
public class CacheableMapGetOpAspect {

    @Autowired
    private RedisclientService redisclientService;

    @Around(value = "@annotation(cacheableMapGetOp)")
    public Object cacheableOp(ProceedingJoinPoint joinPoint, CacheableMapGetOp cacheableMapGetOp) throws Throwable {

        log.debug("cacheableMapGetOp key : " + cacheableMapGetOp.baseKey());

        Object result = null;

        String hashkeyValue = null;

        try {

            Object cacheValue = "";

            if (!StringUtils.isEmpty(cacheableMapGetOp.baseKey())) {

                String cacheKey = CacheKeyUtil.getCacheBaseKey(cacheableMapGetOp.baseKey());

                String HashKey = cacheableMapGetOp.HashKey();

                log.debug("cacheableMapGetOp cacheKey : " + cacheKey);

                log.debug("cacheableMapGetOp HashKey : " + HashKey);

                if (StringUtils.isEmpty(HashKey)) {

                    throw new Exception("cacheableMapGetOp HashKey is null");

                }

                Map<String, Object> methodMap = AspertUtil.getMethodNameAndValueMap(joinPoint);

                if (methodMap.containsKey(HashKey)) {

                    hashkeyValue = String.valueOf(methodMap.get(HashKey));

                    methodMap.remove(HashKey);

                } else {

                    throw new Exception("cacheableMapGetOp HashKey not match parameter");

                }

                if (StringUtils.isNotEmpty(cacheableMapGetOp.argKey())) {

                    if (!methodMap.isEmpty()) {

                        cacheKey = CacheableKeyUtil.getCacheableKey(cacheKey, cacheableMapGetOp.argKey(), methodMap);

                    }

                }

                log.debug("cacheableMapGetOp cacheKey : " + cacheKey);

                cacheValue = redisclientService.getHashMapValue(cacheKey, hashkeyValue);

                if (cacheValue != null) {

                    result = cacheValue;

                } else {

                    result = joinPoint.proceed();

                    if (result != null) {

                        if (result instanceof ServiceRsModel) {

                            ServiceRsModel serviceRsModel = (ServiceRsModel) result;

                            result = putMapToRedis((Map) serviceRsModel.getRsData(), cacheKey, hashkeyValue, cacheableMapGetOp.timeUnit());


                        } else if (result instanceof Map) {

                            result = putMapToRedis((Map) result, cacheKey, hashkeyValue, cacheableMapGetOp.timeUnit());

                        }

                    } else {

                        log.error("cacheableMapGetOp result class type error");

                    }

                }

                return result;

            } else {

                log.error("cacheableMapGetOp key is null");

            }

        } catch (Exception e) {

            log.error("cacheableMapGetOp is error", e);

        }

        return null;

    }

    private Object putMapToRedis(Map resultMap, String cacheKey, String hashkeyValue, Long timeUnit) {

        Object result = null;

        if (resultMap == null) {

            resultMap = new HashMap<String, Object>();

        }

        if (!resultMap.isEmpty()) {

            redisclientService.putHashAllMap(cacheKey, resultMap, timeUnit);

            result = resultMap.get(hashkeyValue);


        } else {

            resultMap.put(hashkeyValue, "");

            redisclientService.putHashAllMap(cacheKey, resultMap, LGRedisConstants.CACHE_MINUTE);

            result = null;

        }

        return result;
    }

}
