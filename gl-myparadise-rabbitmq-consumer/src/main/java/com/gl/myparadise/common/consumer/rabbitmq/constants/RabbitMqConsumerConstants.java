package com.gl.myparadise.common.consumer.rabbitmq.constants;

import org.springframework.amqp.core.AcknowledgeMode;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: RabbitMqConstants
 * @ProjectName: gl-myparadise-common
 * @Date 2023-05-26 - 1:01
 */
public class RabbitMqConsumerConstants {

    public static final String PROPERTIES_SPLIT_TAG = ".";

    public final static String EXCHANGE_TYPE_DIRECT = "DIRECT";

    public final static String EXCHANGE_TYPE_TOPIC = "TOPIC";

    public final static String EXCHANGE_TYPE_HEADERS = "HEADERS";

    public final static String EXCHANGE_TYPE_FANOUT = "FANOUT";

    public final static String EXCHANGE_TYPE_CUSTOM = "CUSTOM";

    public static final String RABBITMQ_CONSUMER_CONNECT_FACTORY_NAME = "connectionFactoryListener";

    public static final String RABBITMQ_CONSUMER_CONFIG_TAG = "spring.lg.rabbitmq.consumer";

    public static final String RABBITMQ_CONSUMER_LISTENER_CONFIG_TAG = RABBITMQ_CONSUMER_CONFIG_TAG + PROPERTIES_SPLIT_TAG + "listener";

    public static final String RABBITMQ_CONSUMER_QUEUES_CONFIG_TAG = RABBITMQ_CONSUMER_LISTENER_CONFIG_TAG + PROPERTIES_SPLIT_TAG + "queues";

    public static final String RABBITMQ_CONSUMER_LISTENER_ACK_AUTO = AcknowledgeMode.AUTO.toString();

    public static final String RABBITMQ_CONSUMER_LISTENER_RETRY_RECOVERER = "com.gl.myparadise.common.consumer.rabbitmq.error.DefaultRejectAndDontRequeueRecoverer";
}
