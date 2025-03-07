package com.gl.myparadise.common.bean.model;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoutingKeyEnum {

    global_report_routing_key(ExchangeEnum.globalReport, "global-report-route-key", "全局事件上报默认路由"),
    global_user_routing_key(ExchangeEnum.globalUser, "global-user-route-key", "全局用户主资料上报默认路由"),
    global_user_other_routing_key(ExchangeEnum.globalUser, "global-user-other-route-key", "全局用户其他资料上报默认路由"),
    global_system_operation_log_routing_key(ExchangeEnum.globalSystem, "global-system-operation-log-route-key", "全局系统操作日志上报默认路由"),
    global_fund_routing_key(ExchangeEnum.globalFund, "global-fund-routing-key", "全局财务上报"),
    ;

    private final ExchangeEnum exchange;
    private final String routingKeyName;
    private final String desc;

    public String exchange() {
        return this.exchange.getExchangeName();
    }

    public String routing() {
        return this.routingKeyName;
    }
}
