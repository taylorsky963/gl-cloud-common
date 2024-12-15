package com.gl.myparadise.common.consumer.rabbitmq.producer.service.impl;

import com.gl.myparadise.common.bean.model.ReportEvent;
import com.gl.myparadise.common.bean.report.user.LoginReport;
import com.gl.myparadise.common.bean.report.user.RegisterReport;
import com.gl.myparadise.common.consumer.rabbitmq.producer.producer.ReportProducer;
import com.gl.myparadise.common.consumer.rabbitmq.producer.service.ReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class ReportServiceImpl implements ReportService {

    @Resource
    private ReportProducer reportProducer;

    @Override
    public void loginReport(LoginReport loginReport) {
        reportProducer.sendMessage(ReportEvent.Login, loginReport);
    }

    @Override
    public void registerReport(RegisterReport registerReport) {
        reportProducer.sendMessage(ReportEvent.Register, registerReport);
    }
}
