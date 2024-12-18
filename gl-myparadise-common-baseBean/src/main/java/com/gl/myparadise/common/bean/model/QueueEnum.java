package com.gl.myparadise.common.bean.model;


import com.gl.myparadise.common.bean.constant.QueueConstant;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum QueueEnum {

    global_event_save_to_elasticsearch(ExchangeEnum.globalReport, RoutingKeyEnum.global_report_routing_key, QueueConstant.GLOBAL_EVENT_SAVE_TO_ELASTICSEARCH, "全局上报的事件消费保存到ES"),

    global_user_data_save_to_elasticsearch(ExchangeEnum.globalUser, RoutingKeyEnum.global_user_routing_key, QueueConstant.GLOBAL_USER_SAVE_TO_ELASTICSEARCH, "全局上报的用户资料消费保存到ES"),

    global_user_other_save_to_elasticsearch(ExchangeEnum.globalUser, RoutingKeyEnum.global_user_other_routing_key, QueueConstant.GLOBAL_USER_OTHER_SAVE_TO_ELASTICSEARCH, "全局上报的用户其他资料消费保存到ES"),

    ;

    private final ExchangeEnum exchange;
    private final RoutingKeyEnum routingKey;
    private final String queueName;
    private final String desc;

    public String exchangeName() {
        return this.exchange.getExchangeName();
    }

    public String routingKeyName() {
        return this.routingKey.getRoutingKeyName();
    }
}
