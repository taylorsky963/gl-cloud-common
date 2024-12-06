package com.gl.myparadise.common.consumer.rabbitmq.producer.constants;

import com.gl.myparadise.common.consumer.rabbitmq.producer.config.RabbitBusinessProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: RabbitMqConstants
 * @ProjectName: gl-myparadise-common
 * @Date 2023-05-26 - 1:01
 */
public class RabbitMqProducerConstants {

    public static final String PROPERTIES_SPLIT_TAG = ".";

    public final static String EXCHANGE_TYPE_DIRECT = "DIRECT";

    public final static String EXCHANGE_TYPE_TOPIC = "TOPIC";

    public final static String EXCHANGE_TYPE_HEADERS = "HEADERS";

    public final static String EXCHANGE_TYPE_FANOUT = "FANOUT";

    public final static String EXCHANGE_TYPE_CUSTOM = "CUSTOM";

    private static final String RABBITMQ_CONFIG_TAG = "spring.lg.rabbitmq";

    public static final String RABBITMQ_PRODUCT_CONFIG_TAG = RABBITMQ_CONFIG_TAG
            + PROPERTIES_SPLIT_TAG + "product";
    public static final String RABBITMQ_PRODUCT_BUSINESSCODE_CONFIG_TAG = RABBITMQ_PRODUCT_CONFIG_TAG
            + PROPERTIES_SPLIT_TAG + "businesscode";

    public static Map<String, RabbitBusinessProperties>  rabbitBusinessPropertiesMap
            = new HashMap<String, RabbitBusinessProperties>();
}
