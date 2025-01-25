package com.gl.myparadise.common.redis.client.service.impl;

import com.gl.myparadise.common.redis.client.properties.RedisClusterProperties;
import com.gl.myparadise.common.redis.client.service.RedisclientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: RedisclientServiceImpl
 * @Date 2023-05-08 - 16:40
 */
@Service
public class RedisclientServiceImpl implements RedisclientService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private JedisConnectionFactory jedisConnectionFactory;

    private static final Logger logger = LoggerFactory.getLogger(RedisclientServiceImpl.class);

    @Autowired
    RedisClusterProperties redisClusterProperties;

    /**
     * 设置缓存数据
     * @param key
     * @param value
     * @return
     */
    @Override
    public boolean putCache(String key, Object value) {

        try {

            redisTemplate.opsForValue().set(key, value);

            return true;

        } catch (Exception e) {

            logger.error("", e);

            return false;

        }
    }

    @Override
    public boolean putCacheIfAbsent(String key, Object value) {

        try {

            redisTemplate.opsForValue().setIfAbsent(key, value);

            return true;

        } catch (Exception e) {

            logger.error("", e);

            return false;

        }
    }

    /**
     * 存放缓存数据，设置缓存失效时间
     * @param key
     * @param value
     * @param timeout 秒
     * @return
     */
    @Override
    public boolean putCacheTimeOutIfAbsent(String key, Object value, long timeout) {

        try {

            if(timeout>0){

                if(value == null){

                    timeout = redisClusterProperties.getNullFlushTime();

                }

                redisTemplate.opsForValue().setIfAbsent(key, value, timeout, TimeUnit.SECONDS);

            }else{

                putCacheIfAbsent(key, value);

            }

            return true;

        } catch (Exception e) {

            logger.error("", e);

            return false;
        }
    }

    /**
     * 存放缓存数据，设置缓存失效时间
     * @param key
     * @param value
     * @param timeout 秒
     * @return
     */
    @Override
    public boolean putCacheTimeOut(String key, Object value, long timeout) {

        try {

            if(timeout>0){

                if(value == null){

                    timeout = redisClusterProperties.getNullFlushTime();

                }

                redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);

            }else{

                putCache(key, value);

            }

            return true;

        } catch (Exception e) {

            logger.error("", e);

            return false;
        }
    }

    /**
     * 获取缓存数据
     * @param key
     * @return
     */
    @Override
    public Object getCache(String key) {

        return key == null ? null : redisTemplate.opsForValue().get(key);

    }

    /**
     * 单独删除缓存
     * @param key
     */
    @Override
    public void removeCache(String key) {

        redisTemplate.delete(key);

    }

    /**
     * 批量删除缓存
     * @param keyList
     */
    @Override
    public void removeCacheArray(List<String> keyList) {

        if(keyList!=null&&keyList.size()>0){

            if(keyList.size()==1){

                removeCache(keyList.get(0));

            }else{

                redisTemplate.delete(keyList);

            }
        }
    }

    /**
     * 判断key是否存在
     * @param key 键
     * @return true 存在 false不存在
     */
    public boolean checkCacheIsExist(String key){

        try {

            return redisTemplate.hasKey(key);

        } catch (Exception e) {

            logger.error("", e);

            return false;

        }
    }

    /**
     * 设置缓存失效时间
     * @param key 键
     * @param time 时间(秒)
     * @return
     */
    @Override
    public boolean setCacheExpireTime(String key,long time){

        try {

            if(time>0){

                redisTemplate.expire(key, time, TimeUnit.SECONDS);

            }

            return true;

        } catch (Exception e) {

            logger.error("", e);

            return false;

        }
    }

    /**
     * 根据key 获取过期时间
     * @param key 键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
    @Override
    public long getCacheExpireTime(String key){

        return redisTemplate.getExpire(key, TimeUnit.SECONDS);

    }


    public Boolean renameOldKeyIfAbsent(String oldKey, String newKey) {

        return redisTemplate.renameIfAbsent(oldKey, newKey);

    }

    /**
     * 讲Key持久化保存
     * @param key
     * @return
     */
    @Override
    public Boolean persistKey(String key) {

        return redisTemplate.persist(key);

    }

    /**
     * 将旧的key设置为value，并且返回旧的key
     * @param key
     * @param value
     * @return
     */
    @Override
    public String setKeyAsValue(String key, String value) {

        return (String) redisTemplate.opsForValue().getAndSet(key, value);

    }

    /**
     * 在原有的值基础上新增字符串到末尾
     * @param key
     * @param value
     */
    @Override
    public void addValueToKeyValue(String key, String value) {

        redisTemplate.opsForValue().append(key, value);

    }

    /**
     * 如果对应的map集合名称不存在，则添加否则不做修改
     * @param valueMap
     */
    @Override
    public boolean multiSetIfAbsent(Map valueMap){

        return redisTemplate.opsForValue().multiSetIfAbsent(valueMap);

    }

    /**
     * 获取MAP的值
     * @param keyList
     * @return
     */
    @Override
    public Object multiGet(List<String> keyList){

        return redisTemplate.opsForValue().multiGet(keyList);

    }

    /**
     * 设置map集合到redis
     * @param valueMap
     */
    @Override
    public void multiSet(Map valueMap){

        redisTemplate.opsForValue().multiSet(valueMap);

    }

    /**
     * 存储list
     * @param key
     * @param valueList
     * @param timeout
     * @return
     */
    @Override
    public long addListTimeOut(String key, List valueList, long timeout){

        if(valueList == null || valueList.size() < 1){

            return 0;

        }

        long putNum = redisTemplate.opsForList().rightPushAll(key, valueList);

        setCacheExpireTime(key, timeout);

        logger.info("cache list addListTimeOut key : " + key);

        logger.info("cache list addListTimeOut putNum : " + putNum);

        return putNum;

    }

    /**
     * 存储list
     * @param key
     * @param valueList
     * @return
     */
    @Override
    public long addList(String key, List valueList) {

        return redisTemplate.opsForList().rightPush(key, valueList);

    }

    /**
     * 获取list值
     * @param key
     * @return
     */
    @Override
    public List getList(String key, long star, long end) {

        long listLength = redisTemplate.opsForList().size(key);

        if(listLength <= 0 || listLength< star){

            return null;

        }

        if(listLength <= end){

            end = listLength;

        }

        return redisTemplate.opsForList().range(key, star, end);

    }

    @Override
    public void removeList(String key) {

        redisTemplate.opsForList().trim(key,0,-1);

    }

    @Override
    public void removeListByValue(String key, String baseValue, List<String> values) {

        for(int i = 0; i < values.size() ; i ++){

            if(values.get(i) != null){

                if(baseValue != null){

                    redisTemplate.opsForList().remove(key, 1L, baseValue + values.get(i));

                }else{

                    redisTemplate.opsForList().remove(key, 1L, baseValue + values.get(i));

                }

            }

        }

    }

    @Override
    public void removeAllListByValue(String key, String baseValue, List<String> values) {

        for(int i = 0; i < values.size() ; i ++){

            if(values.get(i) != null){

                if(baseValue != null){

                    redisTemplate.opsForList().remove(key, 0L, baseValue + values.get(i));

                }else{

                    redisTemplate.opsForList().remove(key, 0L, baseValue + values.get(i));

                }

            }

        }

    }

    @Override
    public void deleteByPrex(String prex) {

        String prexKey = prex +"*";

        Set<String> keys = redisTemplate.keys(prexKey);

        if (!CollectionUtils.isEmpty(keys)) {

            redisTemplate.delete(keys);

        }
    }

    @Override
    public boolean cacheListisExist(String key, long minIndex) {

        long listLength = redisTemplate.opsForList().size(key);

        logger.info("cache list isExist listLength : " + listLength);

        logger.info("cache list isExist key : " + key);


        if(listLength > minIndex){

            return Boolean.TRUE;

        }

        return Boolean.FALSE;
    }

    /**
     * //putAll方法
     * @param key
     * @param valueMap
     * @param timeout
     * @return
     */
    @Override
    public boolean putHashAllMap(String key, Map valueMap, Long timeout){

        if(valueMap != null){

            redisTemplate.opsForHash().putAll(key, valueMap);

            setCacheExpireTime(key, timeout);

            return Boolean.TRUE;

        }

        return Boolean.FALSE;

    }

    /**
     * //put one方法
     * @param key
     * @param key
     * @param hashKey
     * @param hashValue
     * @return
     */
    @Override
    public boolean putHashMap(String key, String hashKey, Object hashValue){

        if(hashValue != null && hashKey != null){

            redisTemplate.opsForHash().put(key, hashKey, hashValue);

            return Boolean.TRUE;

        }

        return Boolean.FALSE;

    }

    /**
     * //delete方法，删除key对应的hash的hashkey及其value
     * @param key
     * @param hashKey
     * @return
     */
    @Override
    public boolean removeHashMap(String key, String hashKey){

        if(hashKey != null){

            redisTemplate.opsForHash().delete(key, hashKey);

            return Boolean.TRUE;

        }

        return Boolean.FALSE;

    }

    /**
     * get方法，根据key和hashkey找出对应的值
     * @param key
     * @param hashKey
     * @return
     */
    @Override
    public Object getHashMapValue(String key, String hashKey){

        if(hashKey != null){

            return redisTemplate.opsForHash().get(key, hashKey);

        }

        return null;

    }

    /**
     * //hasKey方法，确定hashkey是否存在
     * @param key
     * @param hashKey
     * @return
     */
    @Override
    public boolean checkHashMapKeyisExist(String key, String hashKey){

        if(hashKey != null){

            return redisTemplate.opsForHash().hasKey(key, hashKey);

        }

        return Boolean.FALSE;

    }

    /**
     * //multiGet方法，根据key和多个hashkey找出对应的多个值
     * @param key
     * @param hashKey
     * @return
     */
    @Override
    public List getHashMapMultiGet(String key, List<String> hashKey){

        if(hashKey != null){

            return redisTemplate.opsForHash().multiGet(key, hashKey);

        }

        return null;

    }

    /**
     * keys方法，获取key对应的hash表的所有键值对
     * @param key
     * @return
     */
    @Override
    public Map getHashMapAll(String key){

       return redisTemplate.opsForHash().entries(key);

    }


    /**
     * //keys方法，获取key对应的hash表的大小
     * @param key
     * @return
     */
    @Override
    public Long getHashMapSize(String key){

        return redisTemplate.opsForHash().size(key);

    }

}
