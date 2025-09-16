package com.gl.myparadise.common.consumer.rabbitmq.producer.service;

import com.gl.myparadise.common.bean.model.ReportEvent;

public interface ReportRetryService {

    void reportRetry(ReportEvent event, Object message);

}
