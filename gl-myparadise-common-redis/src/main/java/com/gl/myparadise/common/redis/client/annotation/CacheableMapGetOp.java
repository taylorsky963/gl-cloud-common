package com.gl.myparadise.common.redis.client.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Vincent
 * @description:
 * @className: CacheableMapGetOp
 * @projectName: gl-new-cloud-base
 * @date 2025-01-22 - 03:14
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface CacheableMapGetOp {

    String[] value() default {};

    String baseKey() default "";

    String HashKey();

    /*设置方法参数值拼接到KEY里 写法样例 argKey= "${id}##Status=1##${name}"*/
    String argKey() default "";

    long timeUnit() default  24 * 60 * 60L;

}
