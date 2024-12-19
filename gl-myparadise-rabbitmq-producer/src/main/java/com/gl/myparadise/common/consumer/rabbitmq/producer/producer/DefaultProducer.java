package com.gl.myparadise.common.consumer.rabbitmq.producer.producer;

import com.gl.myparadise.common.bean.model.QueueEnum;
import com.gl.myparadise.common.bean.rabbitmq.SendRabbitMessageService;
import com.gl.myparadise.common.bean.report.system.SystemOperationLogReport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DefaultProducer {

    @Autowired
    private SendRabbitMessageService sendRabbitMessageService;


    private final QueueEnum logQueue = QueueEnum.global_system_operation_save_to_elasticsearch;

    public void systemOperationReport(SystemOperationLogReport logReport) {
        try {
            sendRabbitMessageService.sendDirectMessageNoAck(logQueue.exchange(), logQueue.routing(), logReport);
        } catch (Exception e) {
            // TODO
        }
    }


}
