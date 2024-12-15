package com.gl.myparadise.common.consumer.rabbitmq.producer.producer;

import com.gl.myparadise.common.consumer.rabbitmq.producer.service.impl.SendRabbitMessageServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ReportProducer {

//    @Value("${report.project}")
//    private String project;
//    @Value("${global.event.exchange}")
//    private String exchange;
//    @Value("${global.event.routekey}")
//    private String routeKey;

    @Autowired
    private SendRabbitMessageServiceImpl sendRabbitMessageServiceImpl;
}