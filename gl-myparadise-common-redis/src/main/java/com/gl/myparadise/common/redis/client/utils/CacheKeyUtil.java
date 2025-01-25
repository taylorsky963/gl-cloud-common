package com.gl.myparadise.common.redis.client.utils;

import com.gl.myparadise.common.redis.client.constants.LGRedisConstants;
import com.gl.myparadise.common.bean.model.ArgsModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: CacheKeyUtil
 * @ProjectName: ly-common
 * @Date 2023-04-19 - 0:09
 */
@Slf4j
public class CacheKeyUtil {

    private CacheKeyUtil() {
    }

    /**
     * 将KEY中的${参数名}替换为对应的值
     * @param argsKey
     * @param methodArgsMap
     * @return
     */
    public static String getCacheArgsKey(String argsKey, Map<String, Object> methodArgsMap) {

        log.debug("getCacheArgsKey argsKey value : " + argsKey);

        log.debug("getCacheArgsKey methodArgsMap value : " + methodArgsMap);

        if(methodArgsMap != null){

            Pattern pattern = Pattern.compile(LGRedisConstants.CacheKeyPattern);

            Matcher matcher = pattern.matcher(argsKey);

            StringBuffer cacheKey = new StringBuffer();

            while (matcher.find())
            {
                String mapkey = matcher.group(1);

                if(!StringUtils.isEmpty(mapkey)){

                    ArgsModel argsModel = getMethodArgsValue(mapkey, methodArgsMap);

                    if(argsModel != null && !StringUtils.isEmpty(argsModel.getArgValue()) && !StringUtils.isEmpty(argsModel.getArgName())){

                        log.debug("getCacheArgsKey methodArgsMap value : " + argsModel.getArgValue());

                        log.debug("getCacheArgsKey methodArgsMap name : " + argsModel.getArgName());

                        matcher.appendReplacement(cacheKey, mapkey + LGRedisConstants.BASE_ARG_VALUE_EQUALS + argsModel.getArgValue() + LGRedisConstants.BASE_CACHE_SEPARATOR);

                    }else{

                        matcher.appendReplacement(cacheKey, "");

                    }

                }else{

                    log.error("getCacheArgsKey  cacheKey format is error");

                }

            }

            matcher.appendTail(cacheKey);

            log.debug("getCacheKey return cacheKey is : " + cacheKey);

            return cacheKey.toString();

        }else{

           log.debug("getCacheKey return argsKey is : " + argsKey);

            return argsKey;

        }

    }

    /**
     * 将KEY中的${参数名}替换为对应的值
     * @param argsKey
     * @param methodArgsMap
     * @return
     */
    public static String getCacheArgsAppendKey(String argsKey, Map<String, Object> methodArgsMap) {

       log.debug("getCacheArgsAppendKey argsKey value : " + argsKey);

        if(methodArgsMap != null){

            Pattern pattern = Pattern.compile(LGRedisConstants.CacheKeyPattern);

            Matcher matcher = pattern.matcher(argsKey);

            StringBuffer cacheKey = new StringBuffer();

            while (matcher.find())
            {
                String mapkey = matcher.group(1);

                if(!StringUtils.isEmpty(mapkey)){

                    ArgsModel argsModel = getMethodArgsValue(mapkey, methodArgsMap);

                    if(argsModel != null && !StringUtils.isEmpty(argsModel.getArgValue()) && !StringUtils.isEmpty(argsModel.getArgName())){

                        log.debug("getCacheArgsAppendKey methodArgsMap value : " + argsModel.getArgValue());

                        log.debug("getCacheArgsAppendKey methodArgsMap name : " + argsModel.getArgName());

                        cacheKey.append(argsModel.getArgName() + LGRedisConstants.BASE_ARG_VALUE_EQUALS + argsModel.getArgValue() + LGRedisConstants.BASE_CACHE_SEPARATOR);

                    }

                }else{

                    log.error("getCacheArgsAppendKey  cacheKey format is error");

                }

            }

            log.debug("getCacheArgsAppendKey return cacheKey is : " + cacheKey);

            return cacheKey.toString();

        }else{

            log.debug("getCacheArgsAppendKey return argsKey is : " + argsKey);

            return argsKey;

        }

    }

    public static ArgsModel getMethodArgsValue(String mapkey, Map<String, Object> methodArgsMap){

        String[] argSplitArray = mapkey.split("\\.");

        ArgsModel argsModel = new ArgsModel();

        if(argSplitArray != null && argSplitArray.length > 1){

            return getArgsBeanValue(argSplitArray, methodArgsMap.get(argSplitArray[0]), 1);

        }else{

            argsModel.setArgName(mapkey);

            argsModel.setArgValue(objToString(methodArgsMap.get(mapkey)));

            return argsModel;

        }

    }

    private static ArgsModel getArgsBeanValue(String[] argSplitArray, Object methodArgsBean, int index){

        if(index < argSplitArray.length - 1) {

            return getArgsBeanValue(argSplitArray, getValueByPropertie(methodArgsBean, argSplitArray[index]), index + 1);

        }else {

            ArgsModel argsModel = new ArgsModel();

            argsModel.setArgName(argSplitArray[index]);

            argsModel.setArgValue(objToString(getValueByPropertie(methodArgsBean, argSplitArray[index])));

            return argsModel;

        }

    }

    /**
     * 将基础数据类型转换为String
     * @param obj
     * @return
     */
    public static String objToString(Object obj){

        if(obj != null){

            if (obj.getClass().equals(Integer.class)) {

                return Integer.toString((Integer) obj);

            }else if (obj.getClass().equals(Number.class)) {

                return Integer.toString((Integer) obj);

            }else  if (obj.getClass().equals(Long.class)) {

                return  Long.toString((Long) obj);

            }else  if (obj.getClass().equals(long.class)) {

                return  Long.toString((Long) obj);

            }else if (obj.getClass().equals(Float.class)) {

                return String.valueOf((float)obj);

            }else if (obj.getClass().equals(Double.class)) {

                return String.valueOf((Double)obj);

            }else if (obj.getClass().equals(Boolean.class)) {

                return String.valueOf((Boolean)obj);

            }else if (obj.getClass().equals(boolean.class)) {

                return String.valueOf((boolean)obj);

            }else if (obj.getClass().equals(byte.class)) {

                return new String((byte[])obj);

            }else if (obj.getClass().equals(char.class)) {

                return ((Character) obj).toString();

            }else if (obj.getClass().equals(int.class)) {

                return String.valueOf((int) obj);

            }else if (obj.getClass().equals(String.class)) {

                return (String)obj;

            }else if (obj.getClass().equals(Date.class)) {

                return DateFormatUtils.format((Date)obj, "yyyyMMddHHmmss");

            }else{

                log.error("objToString is not base type obj type is : " + obj.getClass().getName());

                return null;

            }

        }

        return null;
    }

    public static Map<String, Object> getValueByPropertie(Object obj, List<String> propertieNameList ) {

        Map<String, Object> mapValue = new HashMap<String, Object> ();

        if(propertieNameList != null && propertieNameList.size() > 0){

            for(int i = 0 ; i < propertieNameList.size(); i++){

                Object value = getValueByPropertie(obj, propertieNameList.get(i));

                if(value != null){

                    mapValue.put(propertieNameList.get(i), value);

                }

            }

        }

        return mapValue;
    }

    /**
     * 根据设置的字段名从返回值中取值
     * @param obj
     * @param propertieName
     * @return
     */
    private static Object getValueByPropertie(Object obj, String propertieName) {

        log.debug("getValueByPropertie propertieName : " + propertieName);

        Object propertieValue = null;

        try {

            Class userCla = (Class) obj.getClass();

           log.debug("getValueByPropertie obj name : " + userCla.getName());

            Field[] fs = userCla.getDeclaredFields();

            for (int i = 0; i < fs.length; i++) {

                Field f = fs[i];

                f.setAccessible(true);

                if (f.getName().endsWith(propertieName)) {

                    log.debug("getValueByPropertie propertieName value = " + f.get(obj));

                    propertieValue = f.get(obj);

                    break;

                }

            }

        } catch (Exception e) {

            log.error("getValueByPropertie is error :", e);

        }

        return propertieValue;
    }

    /**
     * MAP拼接KEY值
     * @param mapValue
     * @return
     */
    public static String getKeyByMap(Map<String, Object> mapValue){

        StringBuffer cacheKey = new StringBuffer();

        if(mapValue != null){

            for(String key : mapValue.keySet()){

                cacheKey.append(mapValue.get(key) == null ? "" : key + LGRedisConstants.BASE_ARG_VALUE_EQUALS + mapValue.get(key) + LGRedisConstants.BASE_CACHE_SEPARATOR);

            }

        }

        log.debug("getKeyByMap cacheKey : " + cacheKey.toString());

        return cacheKey.toString();


    }

    /**
     * 根据设置的字段名从objMap中取值
     * @param objMap
     * @param propertieName
     * @return
     */
    private static Object getValueByMap(Map<String, Object> objMap, String propertieName) {

        log.debug("getValueByMap propertieName : " + propertieName);

        return objMap.get(propertieName);

    }



    /**
     * 拼接BASE KEY
     * @param baseKey
     * @return
     */
    public static String getCacheBaseKey(String baseKey) {

        return LGRedisConstants.BASE_CACHE_SPRINGBOOT_REDIS_SERVICE + baseKey + LGRedisConstants.BASE_CACHE_SEPARATOR;

    }

    /**
     * 返回值的LIST泛型的KEY
     * @param className
     * @param methodName
     * @return
     */
    public static String getReturnListTKey(String className, String methodName) {

        return LGRedisConstants.BASE_CACHE_SPRINGBOOT_REDIS_SERVICE +
                LGRedisConstants.BASE_CACHE_RETURN_LIST_T + className + LGRedisConstants.BASE_CACHE_SEPARATOR
                + methodName + LGRedisConstants.BASE_CACHE_SEPARATOR;

    }

    public static List<String> getStringListBySplit(String str) {

        return getStringListBySplit(str, LGRedisConstants.BASE_RETURN_VALUE_SPLIT);

    }


    /**
     * 通过分割符获取LIST
     * @param str
     * @param splitPx
     * @return
     */
    private static List<String> getStringListBySplit(String str, String splitPx) {

        if(!StringUtils.isEmpty(str)){

            return Arrays.asList(str.split(splitPx));

        }

        return null;

    }

    /**
     * 判断是否是集合类 list 数组
     * @param obj
     * @return
     */
    public static boolean isCollection(Object obj) {

        Class userCla = (Class) obj.getClass();

        return isCollection(userCla);

    }

    /**
     * 断是否是集合类 list 数组
     * @param userCla
     * @return
     */
    public static boolean isCollection(Class userCla) {

        //判断返回类型是否是集合类型
        boolean isCollection = Collection.class.isAssignableFrom(userCla);

        //判断返回类型是否是数组类型
        boolean isArray = userCla.isArray();

        return isCollection || isArray;
    }

    public static void main(String[] args) {

        List a = new ArrayList<>();

        System.out.println(isCollection(a));
    }


    public static Long getNullValueTimeUnit(){

        return LGRedisConstants.CACHE_MINUTE;
    }

    public static String getNullValue(){

        return LGRedisConstants.BASE_CACHE_SAFE_NULL_VALUE;
    }

}
