package com.gl.myparadise.common.redis.client.utils;

import com.gl.myparadise.common.redis.client.constants.LGRedisConstants;

import java.util.List;
import java.util.Map;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: CacheEvictUtil
 * @ProjectName: ly-common
 * @Date 2023-04-19 - 17:09
 */
public class CacheEvictUtil {

    private CacheEvictUtil() {
    }

    /**
     * 拼接存放的更新数据VALUE的 REDIS数据的KEY
     * @param baseKey
     * @param resultMappingKeyValue
     * @return
     */
    public static String getCacheEvictKey(String baseKey, String resultMappingKeyValue) {

        return CacheKeyUtil.getCacheBaseKey(baseKey) + LGRedisConstants.BASE_CACHE_UPDATE_MATCH
                + resultMappingKeyValue;

    }

    /**
     * 获取返回值的拼接KEY
     * @param obj
     * @param propertieNameList
     * @return
     */
    public static String getCacheEvictArgKey(Object obj, List<String> propertieNameList ) {

        Map<String, Object> mapValue = CacheKeyUtil.getValueByPropertie(obj, propertieNameList);

        return CacheKeyUtil.getKeyByMap(mapValue);
    }
}
