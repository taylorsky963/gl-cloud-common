package com.gl.myparadise.common.pagehelper.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PageQuery {

    //页号的参数名
    String pageNumParaName() default "pageNum";

    //每页行数的参数名
    String pageSizeParaName() default "pageSize";

    //是否关闭COUNT(0)
    String isSqlCount() default "isSqlCount";

}
