# 概述
此为RABBITMQ的消费端 就是接收数据端。

## 配置说明
 
配置接收端连接池

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
    
 配置 listenerName, 会根据listenerName 来匹配 listener信息

    #设置监听的beanName
    spring.lg.rabbitmq.consumer.listener.names=demo101,demo102,theadpoolmq

    
 配置管道 default-requeue-rejected 参数 不建议设置为TRUE，会造成MQ信息堆积，内存溢出
        
    #是否启动时自动启动容器
    #spring.lg.rabbitmq.consumer.listener.demo101.auto-startup=false
    #最小的消费者数量
    spring.lg.rabbitmq.consumer.listener.demo101.concurrency=10
    #最大的消费者数量
    spring.lg.rabbitmq.consumer.listener.demo101.max-concurrency=20
    #指定一个请求能处理多少个消息，如果有事务的话，必须大于等于transaction数量.
    spring.lg.rabbitmq.consumer.listener.demo101.prefetch=50
    #超时时间
    spring.lg.rabbitmq.consumer.listener.demo101.receive-timeout=1000
    #表示消息确认方式，其有三种配置方式，分别是NONE、MANUAL和AUTO；默认NONE
    spring.lg.rabbitmq.consumer.listener.demo101.acknowledge-mode=AUTO
    # 决定被拒绝的消息是否重新入队；默认是true（与参数acknowledge-mode有关系）
    #spring.lg.rabbitmq.consumer.listener.demo101.default-requeue-rejected=false
    #多少长时间发布空闲容器时间，单位毫秒
    #spring.lg.rabbitmq.consumer.listener.demo101.idle-event-interval=5000
    #接收超时时间
    spring.lg.rabbitmq.consumer.listener.demo101.receiveTimeout=15000

可自行配置错误处理类

    spring.lg.rabbitmq.consumer.listener.demo101.errorHandler=com.gl.myparadise.demo.server.controller.mq.DemoDefaultErrorHandler

错误处理类必须 实现ErrorHandler 接口
    
    public class DemoDefaultErrorHandler implements ErrorHandler {
    
        private static final Logger logger = LoggerFactory.getLogger(DemoDefaultErrorHandler.class);
    
        @Override
        public void handleError(Throwable throwable) {
    
            logger.error("DemoDefaultErrorHandler drop : ", throwable);
        }
    }    
    
配置重试机制，不建议设置，容易造成堵塞 如果开启RETRY模式 ACK必须是AUTO或者 MANUAL 并且 rejected 为TRUE
如果ACK 设置为MANUAL 请在异常处理类中对ACK进行处理，否者会造成 信息堆积在MQ中

    #监听重试是否可用
    spring.lg.rabbitmq.consumer.listener.demo101.retry.enabled=true
    #最大重试次数
    spring.lg.rabbitmq.consumer.listener.demo101.retry.max-attempts=3
    #第一次和第二次尝试发布或传递消息之间的间隔
    spring.lg.rabbitmq.consumer.listener.demo101.retry.initial-interval=5000
    
    #应用于上一重试间隔的乘数 ExponentialBackOffPolicy
    spring.lg.rabbitmq.consumer.listener.demo101.retry.multiplier=2
    #最大重试时间间隔 ExponentialBackOffPolicy
    spring.lg.rabbitmq.consumer.listener.demo101.retry.max-interval=30000
    #重试是有状态or无状态 ExponentialBackOffPolicy
    spring.lg.rabbitmq.consumer.listener.demo101.retry.stateless=true

自定义重试最大值后的处理类

    spring.lg.rabbitmq.consumer.listener.demo101.retry.recoverer=com.gl.myparadise.demo.server.controller.mq.DemoMethodInvocationRecoverer

代码必须实现MethodInvocationRecoverer 接口

    public class DemoMethodInvocationRecoverer implements MethodInvocationRecoverer {
    
        private static final Logger logger = LoggerFactory.getLogger(DemoDefaultErrorHandler.class);
    
        @Override
        public Object recover(Object[] args, Throwable cause) {
    
            this.logger.error("DemoMethodInvocationRecoverer Retries exhausted for message " + args, cause);
    
            return null;
        }
    }

配置监听的queques

    #设置监听器监听的队列
    spring.lg.rabbitmq.consumer.listener.queues.names=matrixsocketqueues
    #设置自动创建监听器监听队列
    spring.lg.rabbitmq.consumer.listener.queues.auto=false

设置队列绑定的excheange，只有当auto参数为true的时候生效

    spring.lg.rabbitmq.consumer.listener.queues.matrixsocketqueues.exchangeName=matrixsocketexchange
    spring.lg.rabbitmq.consumer.listener.queues.matrixsocketqueues.exchangeType=FANOUT
    spring.lg.rabbitmq.consumer.listener.queues.matrixsocketqueues.routingkey=matrixsocketkey
    
## 代码使用说明
  
代码中加入注解

containerFactory 为listenerName

errorHandler 可以是自定义的错误类BEAN ，注意如果在配置文件中配置了错误类，执行顺序为先执行
errorHandler 指定的BEAN，然后再执行配置文件配置的类，注意 对异常的处理会影响到ACK

queues 为定义的MQ里的队列名字
    
    @RabbitListener(containerFactory = "demo101", errorHandler = "rabbitListenerErrorHandler",
            queues = "demo101queues")
    @RabbitHandler     
    public void getDemo101Messages(@Payload String messages) throws Exception {
    
            if(messages != null){
    
                logger.info("getDemo101Messages  is : " + messages);
    
            }
    
    }
    
实现从配置文件监听queque

    @Value("${spring.lg.rabbitmq.consumer.listener.queues.names}")
    private String[] queuesNames;

    @Bean
    public String[] queuesNames(){

        return queuesNames;
    }

    @RabbitListener(containerFactory = MatrixWebSocketConstants.WEBSOCKET_MQ_BUSINESS_CODE, errorHandler = "rabbitListenerErrorHandler",
            queues = {"#{queuesNames}"})
    public void getDemo101Messages(@Payload String messages) throws Exception {

            if(messages != null){
    
                logger.info("getDemo101Messages  is : " + messages);
    
            }
    
    }