package com.gl.myparadise.common.bean.model;

import com.gl.myparadise.common.bean.constant.RoutingKeyConstant;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoutingKeyEnum {

    global_user_routing_key(RoutingKeyConstant.GLOBAL_USER_ROUTING_KEY, "全局用户路由键"),
    global_user_other_routing_key(RoutingKeyConstant.GLOBAL_USER_OTHER_ROUTING_KEY, "全局用户其他路由键"),
    global_system_operation_log_routing_key(RoutingKeyConstant.GLOBAL_SYSTEM_OPERATION_LOG_ROUTING_KEY, "全局系统操作日志路由键"),
    global_fund_routing_key(RoutingKeyConstant.GLOBAL_FUND_ROUTING_KEY, "全局财务路由键"),
    global_activity_routing_key(RoutingKeyConstant.GLOBAL_ACTIVITY_ROUTING_KEY, "全局活动路由键"),
    
    // 用戶相關事件
    global_report_user_login(RoutingKeyConstant.GLOBAL_REPORT_USER_LOGIN, "用户登录事件"),
    global_report_user_register(RoutingKeyConstant.GLOBAL_REPORT_USER_REGISTER, "用户注册事件"),
    global_report_member_vip(RoutingKeyConstant.GLOBAL_REPORT_MEMBER_VIP, "会员VIP事件"),
    
    // 遊戲相關事件
    global_report_game_betting(RoutingKeyConstant.GLOBAL_REPORT_GAME_BETTING, "游戏投注事件"),
    global_report_game_transfer(RoutingKeyConstant.GLOBAL_REPORT_GAME_TRANSFER, "游戏转账事件"),
    global_report_game_wallet(RoutingKeyConstant.GLOBAL_REPORT_GAME_WALLET, "游戏钱包事件"),
    
    // 財務相關事件
    global_report_fund_recharge(RoutingKeyConstant.GLOBAL_REPORT_FUND_RECHARGE, "充值事件"),
    global_report_fund_withdraw(RoutingKeyConstant.GLOBAL_REPORT_FUND_WITHDRAW, "提现事件"),
    global_report_fund_balance_change(RoutingKeyConstant.GLOBAL_REPORT_FUND_BALANCE_CHANGE, "余额变动事件"),
    global_report_fund_point_change(RoutingKeyConstant.GLOBAL_REPORT_FUND_POINT_CHANGE, "积分变动事件"),
    global_report_fund_bet_flow_change(RoutingKeyConstant.GLOBAL_REPORT_FUND_BET_FLOW_CHANGE, "流水变动事件"),
    
    // 統計相關事件
    global_report_daily_betting(RoutingKeyConstant.GLOBAL_REPORT_DAILY_BETTING, "每日投注统计"),
    global_report_daily_recharge(RoutingKeyConstant.GLOBAL_REPORT_DAILY_RECHARGE, "每日充值统计"),
    ;

    private final String routingKeyName;
    private final String desc;
}
