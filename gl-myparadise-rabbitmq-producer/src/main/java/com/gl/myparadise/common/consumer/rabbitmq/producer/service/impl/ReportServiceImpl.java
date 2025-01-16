package com.gl.myparadise.common.consumer.rabbitmq.producer.service.impl;

import com.gl.myparadise.common.bean.model.ReportEvent;
import com.gl.myparadise.common.bean.report.system.SystemOperationLogReport;
import com.gl.myparadise.common.bean.report.user.BettingReport;
import com.gl.myparadise.common.bean.report.user.LoginReport;
import com.gl.myparadise.common.bean.report.user.RegisterReport;
import com.gl.myparadise.common.bean.report.user.UserSync;
import com.gl.myparadise.common.consumer.rabbitmq.producer.producer.DefaultProducer;
import com.gl.myparadise.common.consumer.rabbitmq.producer.producer.ReportProducer;
import com.gl.myparadise.common.consumer.rabbitmq.producer.producer.UserProducer;
import com.gl.myparadise.common.consumer.rabbitmq.producer.service.ReportService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ReportServiceImpl implements ReportService {

    @Autowired
    private ReportProducer reportProducer;

    @Autowired
    private UserProducer userProducer;

    @Autowired
    private DefaultProducer defaultProducer;

    @Override
    public void loginReport(LoginReport loginReport) {
        reportProducer.sendMessage(ReportEvent.Login, loginReport);
    }

    @Override
    public void registerReport(RegisterReport registerReport) {
        reportProducer.sendMessage(ReportEvent.Register, registerReport);
    }

    @Override
    public void userSync(@NonNull UserSync userSync) {
        userProducer.sendMessage(userSync);
    }

    @Override
    public void systemOperationReport(SystemOperationLogReport logReport) {
        defaultProducer.systemOperationReport(logReport);
    }

    @Override
    public void bettingReport(BettingReport report) {
        reportProducer.sendMessage(ReportEvent.Betting, report);
    }
}
