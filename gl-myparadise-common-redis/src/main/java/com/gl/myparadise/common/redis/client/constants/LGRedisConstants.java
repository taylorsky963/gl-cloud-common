package com.gl.myparadise.common.redis.client.constants;

import com.gl.myparadise.common.bean.constant.CacheConstants;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: LGRedisConstants
 * @ProjectName: myparadise-common
 * @Date 2023-05-13 - 13:51
 */
public class LGRedisConstants extends CacheConstants {

    public static final String REDIS_PROPERTIES_PREFX = "spring.redis";

    public final static String BASE_CACHE_UPDATE_MATCH = "MATCH" + BASE_CACHE_SEPARATOR;

    public final static String BASE_CACHE_RETURN_LIST_T= "LISTRETURN" + BASE_CACHE_SEPARATOR;

    public final static String CacheKeyPattern = "\\$\\{(.+?)\\}";
}
