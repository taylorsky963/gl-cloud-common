package com.gl.myparadise.common.redis.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.gl.myparadise.common.redis.client.constants.LGRedisConstants;
import com.gl.myparadise.common.redis.client.properties.RedisClusterProperties;
import com.gl.myparadise.common.redis.client.properties.RedisProperties;
import com.gl.myparadise.common.redis.client.redismanager.GenericFastJson2JsonRedisSerializer;
import com.gl.myparadise.common.redis.client.redismanager.MyRedisCacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.*;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.StringUtils;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;
import java.util.List;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: RedisClusterConfig
 * @Date 2023-05-09 - 2:18
 */
@Configuration
@ConditionalOnClass({JedisCluster.class})
@EnableConfigurationProperties({RedisClusterProperties.class})
@EnableCaching
@ConditionalOnProperty(prefix = LGRedisConstants.REDIS_PROPERTIES_PREFX, name = "password")
public class RedisClusterConfig extends CachingConfigurerSupport {

    private static final Logger logger = LoggerFactory.getLogger(RedisClusterConfig.class);
    @Autowired
    RedisClusterProperties redisClusterProperties;

    @Autowired
    private JedisPoolConfig jedisPoolConfig;

    @Autowired
    private JedisConnectionFactory jedisConnectionFactory;

    @Bean
    public CacheErrorHandler errorHandler() {
        // 异常处理，当Redis发生异常时，打印日志，但是程序正常走
        logger.info("初始化 -> [{}]", "Redis CacheErrorHandler");
        CacheErrorHandler cacheErrorHandler = new CacheErrorHandler() {
            @Override
            public void handleCacheGetError(RuntimeException e, Cache cache, Object key) {
                logger.error("Redis occur handleCacheGetError：key -> [{}]", key, e);
            }

            @Override
            public void handleCachePutError(RuntimeException e, Cache cache, Object key, Object value) {
                logger.error("Redis occur handleCachePutError：key -> [{}]；value -> [{}]", key, value, e);
            }

            @Override
            public void handleCacheEvictError(RuntimeException e, Cache cache, Object key)    {
                logger.error("Redis occur handleCacheEvictError：key -> [{}]", key, e);
            }

            @Override
            public void handleCacheClearError(RuntimeException e, Cache cache) {
                logger.error("Redis occur handleCacheClearError：", e);
            }

        };

        return cacheErrorHandler;

    }

    @Bean(name="cacheMyParadiseManager")
    @Primary
    public CacheManager cacheManager(JedisConnectionFactory jedisConnectionFactory) {

        RedisCacheWriter redisCacheWriter = RedisCacheWriter.lockingRedisCacheWriter(jedisConnectionFactory);

        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofSeconds(redisClusterProperties.getFlushTime()));

        MyRedisCacheManager myRedisCacheManager = new MyRedisCacheManager(redisCacheWriter,redisCacheConfiguration);

        myRedisCacheManager.setNullFlushTime(redisClusterProperties.getNullFlushTime());

        return myRedisCacheManager;

    }

    @Bean(name="cacheManager")
    public RedisCacheManager cacheManager() {

        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .disableCachingNullValues()
                .entryTtl(Duration.ofSeconds(redisClusterProperties.getFlushTime()));

        RedisCacheManager cacheManager = RedisCacheManager.builder(jedisConnectionFactory)
                .cacheDefaults(redisCacheConfiguration)
                .build();

        return cacheManager;

    }



    @Bean
    public RedisTemplate<String, Object> redisTemplate(JedisConnectionFactory jedisConnectionFactory) {

        RedisTemplate<String, Object> template = new RedisTemplate<>();

        template.setConnectionFactory(jedisConnectionFactory);

        GenericFastJson2JsonRedisSerializer serializer = new GenericFastJson2JsonRedisSerializer(Object.class);

        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        mapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance ,
                ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        serializer.setObjectMapper(mapper);

        template.setValueSerializer(serializer);
        //使用StringRedisSerializer来序列化和反序列化redis的key值
        template.setKeySerializer(new StringRedisSerializer());

        template.afterPropertiesSet();

        return template;

    }

    @Bean
    public JedisConnectionFactory jedisConnectionFactory(){

        JedisConnectionFactory factory = null;

        List<RedisProperties> nodeList = redisClusterProperties.getCluster();

        //获得默认的连接池构造器(怎么设计的，为什么不抽象出单独类，供用户使用呢) 有毒
//        JedisClientConfiguration.JedisPoolingClientConfigurationBuilder jpcb =
//                (JedisClientConfiguration.JedisPoolingClientConfigurationBuilder) JedisClientConfiguration.builder();

        JedisClientConfiguration.JedisPoolingClientConfigurationBuilder jpcb = JedisClientConfiguration.builder()
                .usePooling()
                .poolConfig(jedisPoolConfig);

        //指定jedisPoolConifig来修改默认的连接池构造器（真麻烦，滥用设计模式！） ！！！！
        //通过构造器来构造jedis客户端配置
        JedisClientConfiguration jedisClientConfiguration = jpcb.build();

        //如果是哨兵的模式
        if(redisClusterProperties.getRunmodel() == 1){

            logger.info("Redis use SentinelConfiguration");

            RedisSentinelConfiguration redisSentinelConfiguration = new RedisSentinelConfiguration();

            try {

                for (int i = 0; i < nodeList.size(); i++) {

                    RedisProperties properties = nodeList.get(i);

                    redisSentinelConfiguration.addSentinel(new RedisNode(properties.getHost(), properties.getPort()));

                }

            } catch (Exception e) {

                logger.error("出现配置错误!请确认node是否正确",e);

            }

            factory = new JedisConnectionFactory(redisSentinelConfiguration, jedisClientConfiguration);

        }else if (redisClusterProperties.getRunmodel() == 0) {
            //如果是单个节点 用Standalone模式
            logger.info("Redis use RedisStandaloneConfiguration");

            RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();

            try {

                if (!StringUtils.isEmpty(redisClusterProperties.getPassword())) {

                    redisStandaloneConfiguration.setPassword(RedisPassword.of(redisClusterProperties.getPassword()));

                }

                redisStandaloneConfiguration.setPort(nodeList.get(0).getPort());

                redisStandaloneConfiguration.setHostName(nodeList.get(0).getHost());

            }catch (Exception e) {

                    logger.error("出现配置错误!请确认node是否正确",e);

             }

            factory = new JedisConnectionFactory(redisStandaloneConfiguration, jedisClientConfiguration);

        }else if (redisClusterProperties.getRunmodel() == 2) {
            //集群配置信息实现
            logger.info("Redis use RedisStandaloneConfiguration");

            RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration();

            for (int i = 0; i < nodeList.size(); i++) {

                RedisProperties properties = nodeList.get(i);

               redisClusterConfiguration.addClusterNode(new RedisNode(properties.getHost(), properties.getPort()));

            }

            if (!StringUtils.isEmpty(redisClusterProperties.getPassword())) {

                redisClusterConfiguration.setPassword(RedisPassword.of(redisClusterProperties.getPassword()));

            }

            redisClusterConfiguration.setMaxRedirects(redisClusterProperties.getRedirects());

            factory = new JedisConnectionFactory(redisClusterConfiguration, jedisClientConfiguration);

        }

        return factory;

    }


    @Bean
    public JedisPoolConfig getJedisPoolConfig(){

        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();

        jedisPoolConfig.setMaxTotal(redisClusterProperties.getPool().getMaxTotal());

        jedisPoolConfig.setMaxIdle(redisClusterProperties.getPool().getMaxIdle());

        jedisPoolConfig.setMinIdle(redisClusterProperties.getPool().getMinIdle());

        jedisPoolConfig.setMaxWaitMillis(redisClusterProperties.getPool().getMaxWaitMillis());

        jedisPoolConfig.setTestOnBorrow(redisClusterProperties.getPool().isTestOnBorrow());

        jedisPoolConfig.setTestOnReturn(redisClusterProperties.getPool().isTestOnReturn());

        jedisPoolConfig.setTestWhileIdle(redisClusterProperties.getPool().isTestWhileIdle());

        jedisPoolConfig.setBlockWhenExhausted(redisClusterProperties.getPool().isBlockWhenExhausted());

        jedisPoolConfig.setMinEvictableIdleTimeMillis(redisClusterProperties.getPool().getMinEvictableIdleTimeMillis());

        jedisPoolConfig.setNumTestsPerEvictionRun(redisClusterProperties.getPool().getNumTestsPerEvictionRun());

        jedisPoolConfig.setTimeBetweenEvictionRunsMillis(redisClusterProperties.getPool().getTimeBetweenEvictionRunsMillis());

        jedisPoolConfig.setSoftMinEvictableIdleTimeMillis(redisClusterProperties.getPool().getSoftMinEvictableIdleTimeMillis());

        //设置的逐出策略类名, 默认DefaultEvictionPolicy(当连接超过最大空闲时间,或连接数超过最大空闲连接数)
        jedisPoolConfig.setEvictionPolicyClassName(redisClusterProperties.getPool().getEvictionPolicyClassName());

        jedisPoolConfig.setJmxEnabled(redisClusterProperties.getPool().isJmxEnabled());

        jedisPoolConfig.setJmxNamePrefix(redisClusterProperties.getPool().getJmxNamePrefix());

        jedisPoolConfig.setLifo(redisClusterProperties.getPool().isLifo());

        return jedisPoolConfig;
    }

}
