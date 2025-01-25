package com.gl.myparadise.common.redis.client.utils;

import com.gl.myparadise.common.redis.client.constants.LGRedisConstants;

import java.util.List;
import java.util.Map;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: CacheableKeyUtil
 * @ProjectName: ly-common
 * @Date 2023-04-19 - 17:01
 */
public class CacheableKeyUtil {

//    private static final Logger logger = LoggerFactory.getLogger(CacheableKeyUtil.class);

    private CacheableKeyUtil() {
    }

    /**
     * 返回 KEY
     *
     * @param baseKey
     * @param argsKey
     * @param methodArgsMap
     * @return
     */
    public static String getCacheableKey(String baseKey, String argsKey, Map<String, Object> methodArgsMap) {

        return baseKey + CacheKeyUtil.getCacheArgsKey(argsKey, methodArgsMap);

    }

    public static Long getReturnTimeUnit(Long timeUnit) {

        return timeUnit + LGRedisConstants.CACHE_MINUTE * 30;

    }

    /**
     * 获取返回值的拼接KEY
     *
     * @param obj
     * @param propertieNameList
     * @return
     */
    public static String getReturnMappingKey(Object obj, List<String> propertieNameList) {

        Map<String, Object> mapValue = CacheKeyUtil.getValueByPropertie(obj, propertieNameList);

        return CacheKeyUtil.getKeyByMap(mapValue);
    }

//    public static Object getListBeanFromJason(String dataStr, Class<T> tClass) throws Exception {
//
////        logger.info("getBeanFromJason tClass name : " + tClass.getName());
//
//        CollectionType listType =
//                JsonUtil.objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, tClass);
//
//        return JsonUtil.objectMapper.readValue(dataStr, listType);
//
//    }
//
//
//    public static boolean checkResultValueIsEffective(Object object) {
//
//        boolean tag = true;
//
//        if(object == null){
//
//            tag = false;
//
//        }else if (object.getClass().equals(PageInfo.class)) {
//
//            tag = checkPageInfoValueIsEffective(object);
//
//        }else if (object.getClass().equals(ModelJson.class)) {
//
//            tag = checkModelJsonValueIsEffective(object);
//
//        }
//
//        return tag;
//    }
//
//    public static boolean checkPageInfoValueIsEffective(Object object) {
//
//        boolean tag = true;
//
//        Long pageInfoTotal = ((PageInfo) object).getTotal();
//
////        logger.info("pageInfoTotal PageInfo getTotal is : " + pageInfoTotal);
//
//        if (pageInfoTotal < 1) {
//
//            tag = false;
//
//        }
//
//        return tag;
//    }
//
//    public static boolean checkModelJsonValueIsEffective(Object object) {
//
//        boolean tag = true;
//
//        Object data = ((ModelJson) object).getData();
//
//        if (data == null) {
//
//            tag = false;
//
//        } else if (data.getClass().equals(PageInfo.class)) {
//
//            tag = checkPageInfoValueIsEffective(data);
//
//        }
//
//        return tag;
//    }

    public static Object getCacheDefaultNullValue(Object object) {

        if (object == null) {

            return CacheKeyUtil.getNullValue();

        } else {

            return object;

        }

    }


}
