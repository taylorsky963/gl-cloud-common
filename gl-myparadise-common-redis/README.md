# 概述。
封装了redis连接池，封装了SPRNG @Cacheable 的操作

## redis 配置说明

redis设置的统一过期时间（秒） 默认5分钟

    spring.redis.flushTime=1800
    
redis空值的存储时间, 如果没有配置 默认是60S

    spring.redis.nullFlushTime=80

配置范例
    
    # Redis数据库索引（默认为0）
    spring.redis.runmodel=0 
    # Redis服务器地址
    spring.redis.cluster[0].host=xxx
    # Redis服务器连接端口
    spring.redis.cluster[0].port=xxx 
    #表示连接超时时间
    spring.redis.connectionTimeout=5000
    #出现异常最大重试次数
    spring.redis.maxAttempts=5
    #表示读取数据超时时间
    spring.redis.soTimeout=5000
    spring.redis.redirects=5
    # Redis服务器连接密码（默认为空）
    spring.redis.password=xxx
    # Redis设置的统一过期时间（秒） 默认5分钟
    spring.redis.flushTime=1800
    # Redis设置的null值统一过期时间（秒） -1为不缓存
    spring.redis.nullFlushTime=60
    #最大连接数（使用负值表示没有限制）
    spring.redis.pool.maxTotal=8 
    # 连接池中的最大空闲连接
    spring.redis.pool.maxIdle=30
    #最小空闲连接数
    spring.redis.pool.minIdle=10 
    #获取连接时的最大等待毫秒数,小于零:阻塞不确定的时间,默认-1
    spring.redis.pool.maxWaitMillis=3000 
    #每次释放连接的最大数目
    spring.redis.pool.numTestsPerEvictionRun=10
    #逐出扫描的时间间隔(毫秒) 如果为负数,则不运行逐出线程, 默认-1
    spring.redis.pool.timeBetweenEvictionRunsMillis=3000
    #连接最小空闲时间
    spring.redis.pool.minEvictableIdleTimeMillis=1800000
    #连接空闲多久后释放, 当空闲时间>该值 且 空闲连接>最大空闲连接数 时直接释放
    spring.redis.pool.softMinEvictableIdleTimeMillis=10000
    #在获取连接的时候检查有效性, 默认false
    spring.redis.pool.testOnBorrow=true
    #在空闲时检查有效性, 默认false
    spring.redis.pool.testWhileIdle=true
    #在归还给pool时，是否提前进行validate操作
    spring.redis.pool.testOnReturn=true
    #连接耗尽时是否阻塞, false报异常,ture阻塞直到超时, 默认true
    spring.redis.pool.blockWhenExhausted=false
    # 连接超时时间（毫秒）
    spring.redis.pool.evictionPolicyClassName=org.apache.commons.pool2.impl.DefaultEvictionPolicy
    #是否启用pool的jmx管理功能, 默认true
    spring.redis.pool.jmxEnabled=false
    #是否启用后进先出, 默认true
    spring.redis.pool.lifo=true
    
redis 标签自定义过期时间的使用

value = "分组常量" + # + 过期时间

    @Cacheable(value="redisclientService#600", key = "#userInfo.userInfoId")




