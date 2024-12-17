package com.gl.myparadise.common.mongo.file;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MongoExport {
    String name() default "";

    String dateFormat() default "";

    String dataFormat() default "";

    String convert() default "";

    String header() default "";
}