package com.gl.myparadise.common.webclient.constants;

import com.gl.myparadise.common.webclient.config.HttpPoolClientRetryProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: WebClientConstants
 * @ProjectName: gl-common
 * @Date 2023-06-22 - 19:58
 */
public class WebClientConstants {

    public static final String PROPERTIES_SPLIT_TAG = ".";

    public static final String HTTPPOOL_CONFIG_TAG = "spring.gl.httppool";

    public final static String RESPONSE_ERROR_TGA = "error";

    public final static String HTTP_POOL_RETRY_BASE_TAG = "spring.gl.httppool.retry";

    public final static Map<String, HttpPoolClientRetryProperties> httpPoolRetryPropertiesMap
            = new HashMap<String, HttpPoolClientRetryProperties>();
}
