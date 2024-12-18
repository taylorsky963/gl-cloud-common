package com.gl.myparadise.common.consumer.rabbitmq.producer.producer;

import com.gl.myparadise.common.bean.rabbitmq.SendRabbitMessageService;
import com.gl.myparadise.common.bean.report.user.UserBaseReport;
import com.gl.myparadise.common.bean.report.user.UserSync;
import org.springframework.amqp.AmqpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UserProducer {

    @Value("${global.user.exchange}")
    private String exchange;
    @Value("${global.user.route.key}")
    private String routeKey;
    @Value("${global.user.other.route.key}")
    private String otherRouteKey;

    @Autowired
    private SendRabbitMessageService sendRabbitMessageService;

    public void sendMessage(UserSync userSync) throws AmqpException {
        try {
            sendRabbitMessageService.sendDirectMessageNoAck(exchange, routeKey, userSync);
        } catch (Exception e) {
            // TODO
        }
    }

    public <T extends UserBaseReport> void sendMessage(T report) throws AmqpException {
        try {
            sendRabbitMessageService.sendDirectMessageNoAck(exchange, otherRouteKey, report);
        } catch (Exception e) {
            // TODO
        }
    }
}