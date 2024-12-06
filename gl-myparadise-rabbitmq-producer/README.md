# 概述
此为RABBITMQ的生产端 就是发生数据端。

## 配置说明
 
配置生产者连接池

    #指定client连接到的server的地址，多个以逗号分隔(优先取addresses，然后再取host)
    spring.lg.rabbitmq.consumer.hostAddress=127.0.0.1:5672
    spring.lg.rabbitmq.consumer.username=mmc0630
    spring.lg.rabbitmq.consumer.password=1qaz2wsx
    spring.lg.rabbitmq.consumer.virtualhost=/
    #指定心跳超时，单位秒，0为不指定；默认60s
    spring.lg.rabbitmq.consumer.requested-heartbeat=10
    #连接超时，单位毫秒，0表示无穷大，不超时
    spring.lg.rabbitmq.consumer.connection-timeout=10000    
    #缓存中保持的channel数量
    spring.lg.rabbitmq.consumer.channel-cachesize=10
    spring.lg.rabbitmq.consumer.channel-checkout-timeout=5000
    #连接工厂缓存模式：CHANNEL 和 CONNECTION
    spring.lg.rabbitmq.product.cache-mode=CONNECTION
    
 配置 businesscode, 会根据businesscode 来匹配 管道信息
    
    spring.lg.rabbitmq.product.businesscode.names=demo101,demo102,threadpool101
    
 配置管道
 
    spring.lg.rabbitmq.product.demo101.businesses[0].businessCode=demo101
    spring.lg.rabbitmq.product.demo101.businesses[0].routingkey=demo101key
    spring.lg.rabbitmq.product.demo101.businesses[0].exchangeName=demo101exchange
    
    spring.lg.rabbitmq.product.demo102.businesses[0].businessCode=demo102
    spring.lg.rabbitmq.product.demo102.businesses[0].routingkey=demo102key
    spring.lg.rabbitmq.product.demo102.businesses[0].exchangeName=demo102exchange
    
    spring.lg.rabbitmq.product.demo101.exchanges[0].exchangeName=demo101exchange
    spring.lg.rabbitmq.product.demo101.exchanges[0].exchangeType=DIRECT
    
    spring.lg.rabbitmq.product.demo102.exchanges[0].exchangeName=demo102exchange
    spring.lg.rabbitmq.product.demo102.exchanges[0].exchangeType=DIRECT
    
    
    spring.lg.rabbitmq.product.demo101.queues[0].queuesName=demo101queues
    spring.lg.rabbitmq.product.demo101.queues[0].exchangeName=demo101exchange
    spring.lg.rabbitmq.product.demo101.queues[0].routingkey=demo101key
    
    spring.lg.rabbitmq.product.demo102.queues[0].queuesName=demo102queues
    spring.lg.rabbitmq.product.demo102.queues[0].exchangeName=demo102exchange
    spring.lg.rabbitmq.product.demo102.queues[0].routingkey=demo102key
    
## 代码使用说明

代码中引入接口类     

    @Autowired(required = false)
    private SendRabbitMessageService sendRabbitMessageService;

获取配置信息中的管道配置, 本项目不支持返回ACK的消息.

    RabbitBusinessProperties rabbitBusinessProperties = RabbitMqProductUtils.getRabbitBusinessPropertiesMap(DemoWebConstants.DEMO_MQ_BUSSICESS_BUSINESSCODE_101);

    void sendTopicMessageNoAck(String exchangeDirectName, String routingkey, Object content);
    
    void sendDirectMessageNoAck(String exchangeDirectName, String routingkey, Object content);
    
    void sendFanoutMessageNoAck(String exchangeDirectName, Object content);

  