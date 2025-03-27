package com.gl.myparadise.common.objectpool.annotation;

import java.lang.annotation.*;

/**
 * @author Vincent
 * @description:
 * @className: ObjectPool
 * @projectName: gl-cloud-base
 * @date 2025-03-26 - 02:39
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GLObjectPool {

    /**
     * 指定连接池名称（可选，优先级最高）
     */
    String pool() default "";

    /**
     * 指定注入的参数名（可选，优先级高于默认第一个参数）
     */
    String param() default "";

    /**
     * 异常时是否归还对象（默认 true）
     */
    boolean returnOnError() default true;

}
