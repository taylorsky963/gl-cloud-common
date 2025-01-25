package com.gl.myparadise.common.redis.client.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: CacheableOp
 * @ProjectName: ly-common
 * @Date 2023-04-18 - 18:50
 * @apiNote 添加对应的数据至缓存
 */

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface CacheableMapPutOp {


    String[] value() default {};

    String baseKey() default "";

    String HashKey();

    String HashValue();


}
