package com.gl.myparadise.common.objectpool.util;

import com.gl.myparadise.common.objectpool.annotation.ThrowableConsumer;
import com.gl.myparadise.common.objectpool.annotation.ThrowableFunction;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.pool2.impl.GenericObjectPool;
/**
 * @author Vincent
 * @description:
 * @className: PooledObjectExecutor
 * @projectName: gl-cloud-base
 * @date 2025-03-26 - 03:36
 */
@Slf4j
public class PooledObjectExecutor {

    public static <T> void execute(GenericObjectPool<T> pool, ThrowableConsumer<T> action) {

        T obj = null;

        try {

            obj = pool.borrowObject();

            action.accept(obj);

        } catch (Exception e) {

            log.error(e.getMessage(), e);

            throw new RuntimeException("连接池操作失败", e);

        } finally {

            if (obj != null) {

                pool.returnObject(obj);

            }
        }
    }

    public static <T, R> R execute(GenericObjectPool<T> pool, ThrowableFunction<T, R> action) {

        T obj = null;

        try {

            obj = pool.borrowObject();

            return action.apply(obj);

        } catch (Exception e) {

            log.error(e.getMessage(), e);

            throw new RuntimeException("连接池操作失败", e);

        } finally {

            if (obj != null) {

                pool.returnObject(obj);

            }
        }
    }
}
