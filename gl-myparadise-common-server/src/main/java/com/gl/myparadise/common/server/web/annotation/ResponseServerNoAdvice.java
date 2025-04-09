package com.gl.myparadise.common.server.web.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Vincent
 * @description:
 * @className: ResponseNoAdvice
 * @projectName: gl-cloud-base
 * @date 2025-04-08 - 03:34
 */
@Target({ElementType.METHOD, ElementType.TYPE})// 可用在方法名上
@Retention(RetentionPolicy.RUNTIME)//
public @interface ResponseServerNoAdvice {
}
