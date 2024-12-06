package com.gl.myparadise.common.database.annotation;

import com.gl.myparadise.common.database.constants.DataSourceConfigrceConstants;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface DLDSAnn {

    String dataBase() default "";

    String opType() default DataSourceConfigrceConstants.MASTER_WRITE;
}
