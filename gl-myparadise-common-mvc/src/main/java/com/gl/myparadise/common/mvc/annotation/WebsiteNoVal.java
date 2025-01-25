package com.gl.myparadise.common.mvc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Vincent
 * @description: 这个标签 前台不校验
 * @className: WebsiteNoVal
 * @projectName: gl-new-cloud-base
 * @date 2025-01-22 - 22:51
 */
@Target(ElementType.PARAMETER) // 参数级注解
@Retention(RetentionPolicy.RUNTIME)
public @interface WebsiteNoVal {

    String value() default "";
}
