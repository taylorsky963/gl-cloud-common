package com.gl.myparadise.common.objectpool.aspect;


import com.gl.myparadise.common.objectpool.register.LGObjectPoolRegister;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.ApplicationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.stereotype.Component;
import com.gl.myparadise.common.objectpool.annotation.GLObjectPool;

import java.lang.reflect.Method;
import java.util.Map;
/**
 * @author Vincent
 * @description:
 * @className: ObjectPoolAspect
 * @projectName: gl-cloud-base
 * @date 2025-03-26 - 02:41
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class GLObjectPoolAspect {

    private final ApplicationContext context;

    private final LGObjectPoolRegister poolRegister;

    private final DefaultParameterNameDiscoverer nameDiscoverer = new DefaultParameterNameDiscoverer();

    @Around("execution(* com.gl.myparadise..*(..)) && (@annotation(com.gl.myparadise.common.objectpool.annotation.GLObjectPool) || @within(com.gl.myparadise.common.objectpool.annotation.GLObjectPool))")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {

        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();

        Class<?> targetClass = joinPoint.getTarget().getClass();

        GLObjectPool annotation = method.getAnnotation(GLObjectPool.class);

        if (annotation == null) {

            annotation = targetClass.getAnnotation(GLObjectPool.class);
        }

        if (annotation == null) {

            return joinPoint.proceed();
        }

        Object[] args = joinPoint.getArgs();

        Class<?>[] paramTypes = method.getParameterTypes();

        String[] paramNames = nameDiscoverer.getParameterNames(method);

        if (args.length == 0) {

            throw new IllegalArgumentException("使用 @UsePooledObject 的方法必须包含参数用于注入连接池对象");
        }

        int index = -1;

        if (!annotation.param().isEmpty() && paramNames != null) {

            for (int i = 0; i < paramNames.length; i++) {

                if (annotation.param().equals(paramNames[i])) {

                    index = i;

                    break;
                }
            }

            if (index == -1) {

                throw new IllegalArgumentException("找不到参数名为 '" + annotation.param() + "' 的方法参数");
            }

        } else {

            index = 0;
        }

        Class<?> injectType = paramTypes[index];

        String poolName = annotation.pool();

        GenericObjectPool<?> pool;

        if (!poolName.isEmpty()) {

            pool = context.getBean(poolName, GenericObjectPool.class);

        } else {

            pool = poolRegister.findPoolByType(injectType);

            if (pool == null) {

                throw new IllegalStateException("未找到类型为 " + injectType.getSimpleName() + " 的连接池");
            }
        }

        Object pooledObj = null;

        try {

            pooledObj = ((GenericObjectPool) pool).borrowObject();

            args[index] = pooledObj;

            return joinPoint.proceed(args);

        } catch (Throwable e) {

            if (annotation.returnOnError() && pooledObj != null) {

                ((GenericObjectPool) pool).returnObject(pooledObj);
            }

            throw e;

        } finally {

            if (pooledObj != null) {

                ((GenericObjectPool) pool).returnObject(pooledObj);

            }

        }

    }
}
