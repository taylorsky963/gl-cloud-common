package com.gl.myparadise.common.objectpool.annotation;

/**
 * @author Vincent
 * @description:
 * @className: ThrowableFunction
 * @projectName: gl-cloud-base
 * @date 2025-03-26 - 03:58
 */
@FunctionalInterface
public interface ThrowableFunction<T, R> {

    R apply(T t) throws Exception;

}
