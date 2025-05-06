package com.gl.myparadise.common.consumer.rabbitmq.producer.producer;

import com.gl.myparadise.common.bean.model.ExchangeEnum;
import com.gl.myparadise.common.bean.model.ReportEvent;
import com.gl.myparadise.common.bean.model.RoutingKeyEnum;
import com.gl.myparadise.common.bean.rabbitmq.SendRabbitMessageService;
import com.gl.myparadise.common.bean.report.BaseReport;
import com.gl.myparadise.common.tools.bean.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
public class ReportProducer {

    @Autowired
    private SendRabbitMessageService sendRabbitMessageService;

    public void sendMessage(ReportEvent event, BaseReport report) {
        try {
            this.packMessage(event, report);
            log.info("事件上报:{},{}", event, JsonUtil.toJson(report));
            sendRabbitMessageService.sendDirectMessageNoAck(ExchangeEnum.globalReport.getExchangeName(), getRoutingKey(event).getRoutingKeyName(), report.getMsg());
        } catch (Exception ex) {
            log.error("上报事件数据发生异常", ex);
        }
    }

    /**
     * 根据事件类型获取对应的路由键
     */
    private RoutingKeyEnum getRoutingKey(ReportEvent event) {
        switch (event) {
            case Login:
                return RoutingKeyEnum.global_report_user_login;
            case Register:
                return RoutingKeyEnum.global_report_user_register;
            case Betting:
                return RoutingKeyEnum.global_report_game_betting;
            case GameTransfer:
                return RoutingKeyEnum.global_report_game_transfer;
            case UserWallet:
                return RoutingKeyEnum.global_report_game_wallet;
            case Recharge:
                return RoutingKeyEnum.global_report_fund_recharge;
            case Withdraw:
                return RoutingKeyEnum.global_report_fund_withdraw;
            case MemberVip:
                return RoutingKeyEnum.global_report_member_vip;
            case DailyUserBetting:
                return RoutingKeyEnum.global_report_daily_betting;
            case DailyUserRecharge:
                return RoutingKeyEnum.global_report_daily_recharge;
            case BalanceChange:
                return RoutingKeyEnum.global_report_fund_balance_change;
            case PointChange:
                return RoutingKeyEnum.global_report_fund_point_change;
            case BetFlowChange:
                return RoutingKeyEnum.global_report_fund_bet_flow_change;
            case ActivityMemberAward:
                return RoutingKeyEnum.global_report_activity_member_award;
            default:
                throw new IllegalArgumentException("未知的事件类型: " + event);
        }
    }

    /**
     * 打包待上传的数据
     */
    private void packMessage(ReportEvent event, BaseReport report) throws IllegalAccessException {
        int eventId = event.value();
        report.set("event", eventId);
        report.set("project", "gl-cloud");
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