package com.gl.myparadise.common.consumer.rabbitmq.producer.util;

import com.gl.myparadise.common.consumer.rabbitmq.producer.config.RabbitBusinessProperties;
import com.gl.myparadise.common.consumer.rabbitmq.producer.constants.RabbitMqProducerConstants;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: RabbitMqProductUtil
 * @ProjectName: gl-myparadise-common
 * @Date 2023-05-29 - 0:46
 */
public class RabbitMqProductUtils {

    public static void setRabbitBusinessPropertiesMap(Map<String, RabbitBusinessProperties> rabbitBusinessPropertiesMap){

        RabbitMqProducerConstants.rabbitBusinessPropertiesMap.putAll(rabbitBusinessPropertiesMap);

    }

    public static RabbitBusinessProperties getRabbitBusinessPropertiesMap(String businessCode){

        return RabbitMqProducerConstants.rabbitBusinessPropertiesMap.get(businessCode);

    }
}
