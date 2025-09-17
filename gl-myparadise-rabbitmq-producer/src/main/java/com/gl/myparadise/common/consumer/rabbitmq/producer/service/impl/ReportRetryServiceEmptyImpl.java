package com.gl.myparadise.common.consumer.rabbitmq.producer.service.impl;

import com.gl.myparadise.common.bean.model.ReportEvent;
import com.gl.myparadise.common.consumer.rabbitmq.producer.service.ReportRetryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * ReportRetryService 的空实现
 * 当没有配置 sharding.jdbc.datasource.master.url 时使用
 * 所有方法都是空操作，不会执行任何数据库操作
 */
@Slf4j
@Component
@ConditionalOnProperty(name = "sharding.jdbc.datasource.master.url", havingValue = "false", matchIfMissing = true)
public class ReportRetryServiceEmptyImpl implements ReportRetryService {

    @Override
    public void reportRetry(ReportEvent event, Object message) {
        log.debug("ReportRetryService 空实现：reportRetry 被调用，但未配置数据库，跳过处理。事件类型: {}, 消息类型: {}", 
                event != null ? event.name() : "null", 
                message != null ? message.getClass().getSimpleName() : "null");
    }
}
