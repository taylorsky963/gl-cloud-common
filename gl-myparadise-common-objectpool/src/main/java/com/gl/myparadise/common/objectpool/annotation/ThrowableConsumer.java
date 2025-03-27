package com.gl.myparadise.common.objectpool.annotation;

/**
 * @author Vincent
 * @description:
 * @className: ThrowableConsumer
 * @projectName: gl-cloud-base
 * @date 2025-03-26 - 03:58
 */
@FunctionalInterface
public interface ThrowableConsumer<T> {

    void accept(T t) throws Exception;

}
