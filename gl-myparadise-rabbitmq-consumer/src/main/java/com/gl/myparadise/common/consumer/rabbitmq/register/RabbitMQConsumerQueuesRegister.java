package com.gl.myparadise.common.consumer.rabbitmq.register;

import com.gl.myparadise.common.consumer.rabbitmq.config.RabbitMQListenerQueueProperties;
import com.gl.myparadise.common.consumer.rabbitmq.constants.RabbitMqConsumerConstants;
import com.gl.myparadise.common.tools.bean.SpringBeanUtils;
import com.gl.myparadise.common.tools.util.ListUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: RabbitManagerRegister
 * @ProjectName: gl-myparadise-common
 * @Date 2023-05-26 - 1:04
 */
@Configuration
@ConditionalOnBean(RabbitConsumerConnectRegister.class)
@ConditionalOnProperty(prefix = RabbitMqConsumerConstants.RABBITMQ_CONSUMER_QUEUES_CONFIG_TAG, name = "auto", havingValue = "true")
public class RabbitMQConsumerQueuesRegister implements EnvironmentAware {

    private static final Logger logger = LoggerFactory.getLogger(RabbitMQConsumerQueuesRegister.class);

    private RabbitAdmin rabbitAdmin;

    private Environment environment;

    private Binder binder;

    private List<RabbitMQListenerQueueProperties> queuesList = new ArrayList<RabbitMQListenerQueueProperties>();

    @Override
    public void setEnvironment(Environment environment) {

        logger.info("RabbitMQConsumerQueuesRegister is start");

        this.environment = environment;

        binder = Binder.get(environment);

        this.rabbitAdmin = SpringBeanUtils.getBean(RabbitMqConsumerConstants.RABBITMQ_CONSUMER_RABBIT_ADMIN_NAME);

        List<String> quequesNameList
                = binder.bind((RabbitMqConsumerConstants.RABBITMQ_CONSUMER_QUEUES_CONFIG_TAG + ".names").trim(), List.class).get();


        setQuequeList(quequesNameList);

    }

    private void setQuequeList(List<String> quequesNameList){

        if(ListUtil.isNotEmpty(quequesNameList)){

            for(int i = 0; i < quequesNameList.size(); i++){

                setRabbitMQListenerQueueProperties(quequesNameList.get(i));

            }

        }else{

            logger.error("RabbitMQConsumerQueuesRegister setQuequeList quequesNameList  is error");
        }
    }

    private void setRabbitMQListenerQueueProperties(String quequeName){

        logger.debug("businessCodeName is : " + quequeName);

        if(StringUtils.isNotEmpty(quequeName)){

            RabbitMQListenerQueueProperties rabbitMQListenerQueueProperties = binder.bind((RabbitMqConsumerConstants.RABBITMQ_CONSUMER_QUEUES_CONFIG_TAG
                    + RabbitMqConsumerConstants.PROPERTIES_SPLIT_TAG + quequeName).trim(), RabbitMQListenerQueueProperties.class).get();

            rabbitMQListenerQueueProperties.setQueuesName(quequeName);

            queuesList.add(rabbitMQListenerQueueProperties);

            rabbitMQListenerQueueProperties = null;

        }
    }

    @PostConstruct
    public void createConsumerQueue() {

        try {

            if (queuesList != null && queuesList.size() > 0) {

                for (int i = 0; i < queuesList.size(); i++) {

                    RabbitMQListenerQueueProperties exchangeProperties = queuesList.get(i);

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


    public void bindingQueueToExchange(RabbitMQListenerQueueProperties rabbitMQListenerQueueProperties, Queue queue){

        Binding binding = null;

        binding = new Binding(queue.getName(), Binding.DestinationType.QUEUE,
                rabbitMQListenerQueueProperties.getExchangeName(), rabbitMQListenerQueueProperties.getRoutingkey(), null);

        rabbitAdmin.declareBinding(binding);

    }

}
