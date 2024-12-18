package com.gl.myparadise.common.consumer.rabbitmq.producer.producer;

import com.gl.myparadise.common.bean.model.ReportEvent;
import com.gl.myparadise.common.bean.rabbitmq.SendRabbitMessageService;
import com.gl.myparadise.common.bean.report.BaseReport;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
public class ReportProducer {

    @Value("${report.project}")
    private String project;
    @Value("${global.event.exchange}")
    private String exchange;
    @Value("${global.event.route.key}")
    private String routeKey;

    @Autowired
    private SendRabbitMessageService sendRabbitMessageService;

    public void sendMessage(ReportEvent event, BaseReport report) {
        try {
            this.packMessage(event, report);
            log.info("事件上报:{},{}", event, report);
            sendRabbitMessageService.sendDirectMessageNoAck(exchange, routeKey, report.getMsg());
        } catch (Exception ex) {
            log.error("上报事件数据发生异常", ex);
        }
    }

    /**
     * 打包待上传的数据
     */
    private void packMessage(ReportEvent event, BaseReport report) throws IllegalAccessException {
        int eventId = event.value();
        report.set("event", eventId);
        report.set("project", project);
        if (null == report.get("timestamp")) {
            report.set("timestamp", new Date());
        }
        if (null == report.get("index")) {
            Date ts = (Date) report.get("timestamp");
            report.set("index", DateFormatUtils.format(ts, "yyyy.MM.dd"));
        }
        if (null == report.get("uuid")) {
            report.set("uuid", UUID.randomUUID().toString().replace("-", ""));
        }
        Class<?> clazz = report.getClass();
        Field[] fields = clazz.getDeclaredFields();
        if (fields.length == 0) {
            return;
        }
        String dataKey = String.valueOf(eventId);
        Map<String, Object> data = new HashMap<>();
        for (Field field : fields) {
            field.setAccessible(true);
            String fieldKey = field.getName();
            Object fieldValue = field.get(report);
            if (null == fieldValue) {
                continue;
            }
            data.put(fieldKey, fieldValue);
        }
        report.set(dataKey, data);
    }
}