package com.gl.myparadise.common.redis.client.service;

import java.util.List;
import java.util.Map;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: Redisclient
 * @Date 2023-05-09 - 16:34
 */
public interface RedisclientService {

    boolean setCacheExpireTime(String key, long time);

    boolean putCache(String key, Object value);

    boolean putCacheTimeOut(String key, Object value, long timeout);

    Object getCache(String key);

    void removeCache(String key);

    void removeCacheArray(List<String> keyList);

    boolean checkCacheIsExist(String key);

    long getCacheExpireTime(String key);

    Boolean persistKey(String key);

    String setKeyAsValue(String key, String value);

    void addValueToKeyValue(String key, String value);

    boolean multiSetIfAbsent(Map valueMap);

    void multiSet(Map valueMap);

    long addListTimeOut(String key, List valueList, long timeout);

    long addList(String key, List valueList);

    List getList(String key, long star, long end);

    void removeList(String key);

    void deleteByPrex(String prex);

    boolean cacheListisExist(String key, long maxIndex);

    boolean putCacheTimeOutIfAbsent(String key, Object value, long timeout);

    boolean putCacheIfAbsent(String key, Object value);

    public Object multiGet(List<String> keyList);

    public void removeListByValue(String key, String baseValue, List<String> values);

    public void removeAllListByValue(String key, String baseValue, List<String> values);

    boolean putHashMap(String key, String hashKey, Object hashValue);

    boolean putHashAllMap(String key, Map valueMap, Long timeout);

    boolean removeHashMap(String key, String hashKey);

    Object getHashMapValue(String key, String hashKey);

    boolean checkHashMapKeyisExist(String key, String hashKey);

    List getHashMapMultiGet(String key, List<String> hashKey);

    Map getHashMapAll(String key);

    Long getHashMapSize(String key);

}
