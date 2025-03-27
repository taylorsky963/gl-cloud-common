package com.gl.myparadise.common.objectpool.constants;

import com.gl.myparadise.common.objectpool.config.ObjectPoolConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: ThreadPoolConfigrceConstants
 * @ProjectName: myparadise-common
 * @Date 2023-05-22 - 19:58
 */
public class ObjectPoolConfigConstants {

    public static final String OBJECT_POOL_CONFIG_TAG = "gl.object.pool";

    public static final String PROPERTIES_SPLIT_TAG = ".";

    public static Map<String, ObjectPoolConfigurationProperties> objectPoolMap = new HashMap<String, ObjectPoolConfigurationProperties>();


}
