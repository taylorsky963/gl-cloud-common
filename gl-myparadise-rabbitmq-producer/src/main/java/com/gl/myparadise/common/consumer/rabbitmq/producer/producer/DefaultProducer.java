package com.gl.myparadise.common.consumer.rabbitmq.producer.producer;

import com.gl.myparadise.common.bean.model.QueueEnum;
import com.gl.myparadise.common.bean.rabbitmq.SendRabbitMessageService;
import com.gl.myparadise.common.bean.report.activity.ActivityBaseReport;
import com.gl.myparadise.common.bean.report.fund.FundBaseReport;
import com.gl.myparadise.common.bean.report.system.SystemOperationLogReport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DefaultProducer {

    @Autowired
    private SendRabbitMessageService sendRabbitMessageService;


    private final QueueEnum logQueue = QueueEnum.global_system_operation_save_to_elasticsearch;
    private final QueueEnum fundQueue = QueueEnum.global_fund;
    private final QueueEnum fundDiversionQueue = QueueEnum.global_fund_diversion;
    private final QueueEnum activityQueue = QueueEnum.global_activity;

    public void systemOperationReport(SystemOperationLogReport report) {
        try {
            sendRabbitMessageService.sendDirectMessageNoAck(logQueue.exchange(), logQueue.routing(), report);
        } catch (Exception e) {
            log.error("DefaultProducer.systemOperationReport: ", e);
        }
    }

    public void sendFundReport(FundBaseReport report) throws AmqpException {
        sendRabbitMessageService.sendDirectMessageNoAck(fundQueue.exchange(), fundQueue.routing(), report);
    }

    public void sendFundDiversionReport(FundBaseReport report) throws AmqpException {
        sendRabbitMessageService.sendDirectMessageNoAck(fundDiversionQueue.exchange(), fundDiversionQueue.routing(), report);
    }

    public void sendActivity(ActivityBaseReport report) {
        sendRabbitMessageService.sendDirectMessageNoAck(activityQueue.exchange(), activityQueue.routing(), report);
    }
}
