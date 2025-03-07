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

    global_system_operation_save_to_elasticsearch(ExchangeEnum.globalSystem, RoutingKeyEnum.global_system_operation_log_routing_key, QueueConstant.GLOBAL_SYSTEM_OPERATION_LOG_SAVE_TO_ELASTICSEARCH, "全局系统操作日志消费保存到ES"),

    global_game_data_collection_save(ExchangeEnum.globalReport, RoutingKeyEnum.global_report_routing_key, QueueConstant.GLOBAL_GAME_DATA_COLLECTION_SAVE, "全局上报的投注紀錄保存到數據庫"),

    global_fund(ExchangeEnum.globalFund, RoutingKeyEnum.global_fund_routing_key, QueueConstant.GLOBAL_FUND, "全局财务相关上报"),
    ;

    private final ExchangeEnum exchange;
    private final RoutingKeyEnum routingKey;
    private final String queueName;
    private final String desc;

    public String exchange() {
        return this.exchange.getExchangeName();
    }

    public String routing() {
        return this.routingKey.getRoutingKeyName();
    }
}
