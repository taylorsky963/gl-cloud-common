package com.gl.myparadise.common.consumer.rabbitmq.producer.service.impl;

import com.gl.myparadise.common.bean.rabbitmq.SendRabbitMessageService;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: SendRabbitMessageServiceImpl
 * @ProjectName: gl-myparadise-common
 * @Date 2023-05-26 - 1:16
 */
@Service
public class SendRabbitMessageServiceImpl implements SendRabbitMessageService {

    @Autowired(required = false)
    private RabbitAdmin rabbitAdmin;

    /**
     * 主题 模式
     * @param exchangeDirectName
     * @param routingkey
     * @param content
     */
    @Override
    public void sendTopicMessageNoAck(String exchangeDirectName, String routingkey, Object content) {

        rabbitAdmin.getRabbitTemplate().convertAndSend(exchangeDirectName, routingkey, content);

    }

    /**
     * 直连模式
     * @param exchangeDirectName
     * @param routingkey
     * @param content
     */
    @Override
    public void sendDirectMessageNoAck(String exchangeDirectName, String routingkey, Object content) {

        rabbitAdmin.getRabbitTemplate().convertAndSend(exchangeDirectName, routingkey, content);
    }

    /**
     * 广播模式
     * @param exchangeDirectName
     * @param content
     */
    @Override
    public void sendFanoutMessageNoAck(String exchangeDirectName, Object content) {

        rabbitAdmin.getRabbitTemplate().convertAndSend(exchangeDirectName, "", content);

    }
}
