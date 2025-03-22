package com.gl.myparadise.common.mvc.Interceptor;

import com.gl.myparadise.common.bean.model.ExceptionEnum;
import com.gl.myparadise.common.bean.model.ServiceRsModel;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

/**
 * @author Vincent
 * @description:
 * @className: FeignClientAspect
 * @projectName: gl-cloud-base
 * @date 2025-03-22 - 03:52
 */
@Aspect
@Component
public class FeignClientAspect {

    private static final Logger logger = LoggerFactory.getLogger(FeignClientAspect.class);


    @Around("execution(* com.gl.myparadise..api..*.*(..)) && @within(org.springframework.cloud.openfeign.FeignClient)")
    public Object handleFeignClientException(ProceedingJoinPoint joinPoint){

        try {

            return joinPoint.proceed();

        }catch (Throwable e) {

            logger.error("FeignClient 调用异常: {}", e.getMessage(), e);

            return new ServiceRsModel<>().error(
                    ExceptionEnum.SERVER_BUSY.getResponseCode(), // 错误码
                    ExceptionEnum.SERVER_BUSY.getResponseMsg()   // 错误消息
            );

        }

    }
}
