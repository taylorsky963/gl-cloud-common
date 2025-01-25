package com.gl.myparadise.common.redis.client.aspect;


import com.gl.myparadise.common.redis.client.annotation.CacheableMapPutOp;
import com.gl.myparadise.common.redis.client.service.RedisclientService;
import com.gl.myparadise.common.redis.client.utils.AspertUtil;
import com.gl.myparadise.common.redis.client.utils.CacheKeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;


/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: CacheableOpAspect
 * @ProjectName: ly-common
 * @Date 2023-04-18 - 18:58
 */
@Aspect
@Slf4j
@Component
public class CacheableMapDelOpAspect {

    @Autowired
    private RedisclientService redisclientService;

    @Around(value = "@annotation(cacheableMapPutOp)")
    public void cacheablePutOp(ProceedingJoinPoint joinPoint, CacheableMapPutOp cacheableMapPutOp) throws Throwable {

        if (!StringUtils.isEmpty(cacheableMapPutOp.baseKey())) {

            String cacheKey = CacheKeyUtil.getCacheBaseKey(cacheableMapPutOp.baseKey());

            log.debug("cacheablePutOp cacheKey : " + cacheKey);

            Map<String, Object> methodMap = AspertUtil.getMethodNameAndValueMap(joinPoint);

            if (methodMap != null && !methodMap.isEmpty()) {

                String hashKey = (String) methodMap.get(cacheableMapPutOp.HashKey());

                Object hashValue = methodMap.get(cacheableMapPutOp.HashValue());

                log.debug("cacheablePutOp hashKey : " + hashKey);

                if(StringUtils.isNotEmpty(hashKey)) {

                    redisclientService.putHashMap(cacheKey, hashKey, hashValue);

                }else{

                    redisclientService.removeCache(cacheKey);

                }

            } else {

                throw new Exception("cacheablePutOp not param");

            }

        }

    }

}
