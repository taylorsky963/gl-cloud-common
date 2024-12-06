# 概述
此为webclient连接池的封装工程。

## 配置说明
 
配置连接池名字 spring.lg.httppool.names ，多个连接池用逗号分隔

    spring.lg.httppool.names=matrix
    
根据连接池名字配置连接池信息

    #访问基础链接
    spring.lg.httppool.matrix.base-url=http://127.0.0.1:3010
    
    # 设置最大内存单位 M
    spring.lg.httppool.matrix.maxInMemorySize=2
    
    #springbean name
    spring.lg.httppool.matrix.bean-name=matrixHttpPool
    
    #连接池的最大连接数
    spring.lg.httppool.matrix.max-total-connect=2
    
    #等待队列大小，默认是最大连接数的2倍
    spring.lg.httppool.matrix.pending-acquire-max-count=2
    
    #任务等待超时时间，当队列中的任务等待超过pendingAcquireTimeout还获取不到连接，就会抛出异常,单位是秒
    spring.lg.httppool.matrix.pending-acquire-timeout=10
    
    #空闲时连接回收的时间,单位秒
    spring.lg.httppool.matrix.max-idle-time=20
    
    #连接的生命周期时间,单位秒
    spring.lg.httppool.matrix.max-life-time=60
    
    #SSL 证书存放路径，没有证书暂时无法测试
    #spring.lg.httppool.matrix.ssl-path=
    
    # 指客户端和服务器建立连接的超时时间,ms , 最大约21秒,因为内部tcp在进行三次握手建立连接时,默认tcp超时时间是20秒
    spring.lg.httppool.matrix.connect-timeout=20000
    
    # 指客户端从服务器读取数据包的间隔超时时间,不是总读取时间,也就是socket timeout 单位 S
    spring.lg.httppool.matrix.read-timeout=20
    
    #写入TIMEOUT 单位 s
    spring.lg.httppool.matrix.write-timeout=60

    
## 代码使用说明
  
代码中自动注入

        @Qualifier(value = "matrixHttpPool")
        @Autowired
        private WebClient webClient;
    

  