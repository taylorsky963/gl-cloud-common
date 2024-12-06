package com.gl.myparadise.common.consumer.rabbitmq.producer.register;

import com.gl.myparadise.common.consumer.rabbitmq.producer.config.*;
import com.gl.myparadise.common.consumer.rabbitmq.producer.constants.RabbitMqProducerConstants;
import com.gl.myparadise.common.consumer.rabbitmq.producer.util.RabbitMqProductUtils;
import com.gl.myparadise.common.tools.bean.SpringBeanUtils;
import com.gl.myparadise.common.tools.util.ListUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: RabbitManagerRegister
 * @ProjectName: gl-myparadise-common
 * @Date 2023-05-26 - 1:04
 */
@Configuration
@EnableConfigurationProperties({RabbitMQProductConfigurationProperties.class})
@ConditionalOnProperty(prefix = RabbitMqProducerConstants.RABBITMQ_PRODUCT_BUSINESSCODE_CONFIG_TAG, name = "names")
public class RabbitManagerRegister  implements EnvironmentAware {

    private static final Logger logger = LoggerFactory.getLogger(RabbitManagerRegister.class);

    private RabbitAdmin rabbitAdmin;

    private Environment environment;

    private Binder binder;

    private List<RabbitExchangeProperties> exchangeList = new ArrayList<RabbitExchangeProperties>();

    private List<RabbitQueueProperties> queuesList = new ArrayList<RabbitQueueProperties>();


    @Override
    public void setEnvironment(Environment environment) {

        this.environment = environment;

        binder = Binder.get(environment);

        this.rabbitAdmin = SpringBeanUtils.getBean("rabbitAdmin");

        RabbitBusinessConfigurationProperties rabbitBusinessConfigurationProperties
                = binder.bind((RabbitMqProducerConstants.RABBITMQ_PRODUCT_BUSINESSCODE_CONFIG_TAG).trim(), RabbitBusinessConfigurationProperties.class).get();


        setRabbitBusinessPropertiesMap(rabbitBusinessConfigurationProperties.getNames());

    }

    private void setRabbitBusinessPropertiesMap(List<String> businessCodeNameList){

        if(ListUtil.isNotEmpty(businessCodeNameList)){

            for(int i = 0; i < businessCodeNameList.size(); i++){

                setRabbitBusinessProperties(businessCodeNameList.get(i));

            }

        }else{

            logger.error("RabbitManagerRegister setRabbitBusinessPropertiesMap mq properties is error");
        }
    }

    private void setRabbitBusinessProperties(String businessCodeName){

        logger.debug("businessCodeName is : " + businessCodeName);

        if(StringUtils.isNotEmpty(businessCodeName)){

            RabbitBusinessConfigurationProperties rabbitBusinessConfigurationProperties = binder.bind((RabbitMqProducerConstants.RABBITMQ_PRODUCT_CONFIG_TAG
                    + RabbitMqProducerConstants.PROPERTIES_SPLIT_TAG + businessCodeName).trim(), RabbitBusinessConfigurationProperties.class).get();

            exchangeList.addAll(rabbitBusinessConfigurationProperties.getExchanges());

            queuesList.addAll(rabbitBusinessConfigurationProperties.getQueues());

            RabbitMqProductUtils.setRabbitBusinessPropertiesMap(getRabbitBusinessConfig(rabbitBusinessConfigurationProperties.getBusinesses()));

        }
    }

    private RabbitExchangeProperties getExchangeConfig(String exchangeName) {

        Map<String, RabbitExchangeProperties> collect = exchangeList.stream()
                .collect(Collectors.toMap(e -> e.getExchangeName(), e -> e));

        return collect.get(exchangeName);

    }

    private Map<String, RabbitBusinessProperties> getRabbitBusinessConfig(List<RabbitBusinessProperties> rabbitBusinessPropertiesList) {

        return rabbitBusinessPropertiesList.stream()
                .collect(Collectors.toMap(e -> e.getBusinessCode(), e -> e));

    }


    @Bean
    public void createExchange() {

        try {

            if (exchangeList != null && exchangeList.size() > 0) {

                for (int i = 0; i < exchangeList.size(); i++) {

                    RabbitExchangeProperties exchangeProperties = exchangeList.get(i);
                    // 声明交换机
                    Exchange exchange = null;

                    switch (exchangeProperties.getExchangeType()) {

                        case RabbitMqProducerConstants.EXCHANGE_TYPE_DIRECT:

                            exchange = new DirectExchange(exchangeProperties.getExchangeName(), exchangeProperties.getDurable(), exchangeProperties.getAutoDelete());

                            break;

                        case RabbitMqProducerConstants.EXCHANGE_TYPE_TOPIC:

                            exchange = new TopicExchange(exchangeProperties.getExchangeName(), exchangeProperties.getDurable(), exchangeProperties.getAutoDelete());

                            break;

                        case RabbitMqProducerConstants.EXCHANGE_TYPE_HEADERS:

                            exchange = new HeadersExchange(exchangeProperties.getExchangeName(), exchangeProperties.getDurable(), exchangeProperties.getAutoDelete());

                            break;

                        case RabbitMqProducerConstants.EXCHANGE_TYPE_FANOUT:

                            exchange = new FanoutExchange(exchangeProperties.getExchangeName(), exchangeProperties.getDurable(), exchangeProperties.getAutoDelete());

                            break;

                        case RabbitMqProducerConstants.EXCHANGE_TYPE_CUSTOM:

                            exchange = new CustomExchange(exchangeProperties.getExchangeName(), exchangeProperties.getCustomType(), exchangeProperties.getDurable(), exchangeProperties.getAutoDelete(), exchangeProperties.getArguments());

                            break;

                        default:

                            break;

                    }
                    // 将交换机注册到spring bean工厂 让spring实现交换机的管理
                    if (exchange != null) {

                        rabbitAdmin.declareExchange(exchange);

                        SpringBeanUtils.registerBean(exchange.getName(), exchange);

                    }
                }

            }

        }catch(Exception e){

            logger.error("createExchange is error");
        }

    }

    @Bean
    public void createQueue() {

        try {

            if (queuesList != null && queuesList.size() > 0) {

                for (int i = 0; i < queuesList.size(); i++) {

                    RabbitQueueProperties exchangeProperties = queuesList.get(i);

                    Queue queue = new Queue(exchangeProperties.getQueuesName(), exchangeProperties.getDurable(),
                            exchangeProperties.getExclusive(), exchangeProperties.getAutoDelete(), exchangeProperties.getArgs());

                    rabbitAdmin.declareQueue(queue);

                    bindingQueueToExchange(exchangeProperties, queue);

                }

            }

        }catch (Exception e){

            logger.error("createQueue is error");

        }

    }


    public void bindingQueueToExchange(RabbitQueueProperties rabbitQueueProperties, Queue queue){

        RabbitExchangeProperties exchangePropertie = getExchangeConfig(rabbitQueueProperties.getExchangeName());

        switch (exchangePropertie.getExchangeType()) {

            case RabbitMqProducerConstants.EXCHANGE_TYPE_TOPIC:

                bindingQueueToExchangeByTopic(queue, rabbitQueueProperties.getExchangeName(), rabbitQueueProperties.getRoutingkey());

                break;

            case RabbitMqProducerConstants.EXCHANGE_TYPE_DIRECT:

                bindingQueueToExchangeByDirect(queue, rabbitQueueProperties.getExchangeName(), rabbitQueueProperties.getRoutingkey());

                break;

            case RabbitMqProducerConstants.EXCHANGE_TYPE_HEADERS:

                bindingQueueToExchangeByHeaders(queue, rabbitQueueProperties.getExchangeName(), rabbitQueueProperties);

                break;

            case RabbitMqProducerConstants.EXCHANGE_TYPE_FANOUT:

                bindingQueueToExchangeByFanout(queue, rabbitQueueProperties.getExchangeName());

                break;

            case RabbitMqProducerConstants.EXCHANGE_TYPE_CUSTOM:

                bindingQueueToExchangeByCustom(queue, rabbitQueueProperties.getExchangeName(), rabbitQueueProperties.getRoutingkey());

                break;

            default:

                logger.warn("queue [{}] config unspecified exchange!", rabbitQueueProperties.getQueuesName());

                break;

        }

    }

    /**
     * 绑定一个队列到一个匹配型交换器使用一个routingKey
     *
     * @param queue
     * @param exchangeName
     * @param routingKey
     */
    private void bindingQueueToExchangeByDirect(Queue queue, String exchangeName, String routingKey) {

        Binding binding = null;

        binding = BindingBuilder.bind(queue)
                .to(SpringBeanUtils.getBean(exchangeName, DirectExchange.class))
                .with(routingKey);

        rabbitAdmin.declareBinding(binding);

    }

    /**
     * 绑定一个队列到一个匹配型交换器使用一个routingKey
     *
     * @param queue
     * @param exchangeName
     * @param routingKey
     */
    private void bindingQueueToExchangeByTopic(Queue queue, String exchangeName, String routingKey) {

        Binding binding = null;

        binding = BindingBuilder.bind(queue)
                .to(SpringBeanUtils.getBean(exchangeName, TopicExchange.class))
                .with(routingKey);

        rabbitAdmin.declareBinding(binding);

    }

    /**
     * 绑定一个队列到一个匹配型交换器使用一个routingKey
     *
     * @param queue
     * @param exchangeName
     *
     */
    private void bindingQueueToExchangeByHeaders(Queue queue, String exchangeName, RabbitQueueProperties rabbitQueueProperties) {

        Binding binding = null;

        if (rabbitQueueProperties.getWhereAll()) {

            binding = BindingBuilder.bind(queue)
                    .to(SpringBeanUtils.getBean(exchangeName, HeadersExchange.class))
                    .whereAll(rabbitQueueProperties.getHeaders()).match();

        } else {

            binding = BindingBuilder.bind(queue)
                    .to(SpringBeanUtils.getBean(exchangeName, HeadersExchange.class))
                    .whereAny(rabbitQueueProperties.getHeaders()).match();

        }

        rabbitAdmin.declareBinding(binding);

    }

    /**
     * 绑定一个队列到一个匹配型交换器使用一个routingKey
     *
     * @param queue
     * @param exchangeName
     */
    private void bindingQueueToExchangeByFanout(Queue queue, String exchangeName) {

        Binding binding = null;

        binding = BindingBuilder.bind(queue)
                .to(SpringBeanUtils.getBean(exchangeName, FanoutExchange.class));

        rabbitAdmin.declareBinding(binding);

    }

    /**
     * 绑定一个队列到一个匹配型交换器使用一个routingKey
     *
     * @param queue
     * @param exchangeName
     */
    private void bindingQueueToExchangeByCustom(Queue queue, String exchangeName, String routingKey) {

        Binding binding = null;

        binding = BindingBuilder.bind(queue)
                .to(SpringBeanUtils.getBean(exchangeName, CustomExchange.class))
                .with(routingKey).noargs();

        rabbitAdmin.declareBinding(binding);

    }
}
