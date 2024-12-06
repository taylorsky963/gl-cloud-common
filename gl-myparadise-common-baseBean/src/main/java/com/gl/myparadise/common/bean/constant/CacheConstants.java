package com.gl.myparadise.common.bean.constant;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: CacheConstants
 * @ProjectName: myparadise-common
 * @Date 2023-04-17 - 20:04
 */
public class CacheConstants {

    public final static String BASE_CACHE_SEPARATOR = "##";

    public final static String BASE_CACHE_SPRINGBOOT_REDIS= "redisCSer";

    public final static String BASE_CACHE_SPRINGBOOT_REDIS_SERVICE= BASE_CACHE_SPRINGBOOT_REDIS + "::";

    public static final String BASE_CACHE_CLIENT = ".CLIENT";

    public final static String BASE_RETURN_VALUE_SPLIT = ",";

    public final static String BASE_ARG_VALUE_EQUALS = ":";

    public final static String BASE_CACHE_SAFE_NULL_VALUE = "NULL";

    public final static long CACHE_MINUTE = 60L;

    public final static long CACHE_HOUR = 60L * CACHE_MINUTE;

    public final static long CACHE_DAY = 24 * CACHE_HOUR;

    public final static long CACHE_YEAR = 365 * CACHE_DAY;

}
