package com.gl.myparadise.common.bean.rabbitmq;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: SendRabbitMessageService
 * @ProjectName: gl-myparadise-common
 * @Date 2023-05-26 - 1:15
 */
public interface SendRabbitMessageService {

    void sendTopicMessageNoAck(String exchangeDirectName, String routingkey, Object content);

    void sendDirectMessageNoAck(String exchangeDirectName, String routingkey, Object content);

    void sendFanoutMessageNoAck(String exchangeDirectName, Object content);

}
