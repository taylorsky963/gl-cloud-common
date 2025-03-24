package com.gl.myparadise.common.mvc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Vincent
 * @description:
 * @className: WebSiteRequestLogRequired
 * @projectName: gl-background-member
 * @date 2025-01-16 - 22:43
 */
@Target({ElementType.METHOD, ElementType.TYPE})// 可用在方法名上
@Retention(RetentionPolicy.RUNTIME)// 运行时有效
public @interface GLFeignRequestAspectRequired {

    String value() default "";

}

