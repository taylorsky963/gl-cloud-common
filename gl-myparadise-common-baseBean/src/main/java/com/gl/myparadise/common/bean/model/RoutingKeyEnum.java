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
    global_fund_diversion_routing_key(RoutingKeyConstant.GLOBAL_FUND_DIVERSION_ROUTING_KEY, "全局财务分流路由键"),
    global_activity_routing_key(RoutingKeyConstant.GLOBAL_ACTIVITY_ROUTING_KEY, "全局活动路由键"),
    
    // 用戶相關事件
    global_report_user_login(RoutingKeyConstant.GLOBAL_REPORT_USER_LOGIN, "用户登录事件"),
    global_report_user_register(RoutingKeyConstant.GLOBAL_REPORT_USER_REGISTER, "用户注册事件"),
    global_report_member_vip(RoutingKeyConstant.GLOBAL_REPORT_MEMBER_VIP, "会员VIP事件"),
    global_report_member_login_log(RoutingKeyConstant.GLOBAL_REPORT_MEMBER_LOGIN_LOG, "会员登入事件"),
    
    // 遊戲相關事件
    global_report_game_betting(RoutingKeyConstant.GLOBAL_REPORT_GAME_BETTING, "游戏投注事件"),
    global_report_game_seamless_game_transaction(RoutingKeyConstant.GLOBAL_REPORT_GAME_SEAMLESS_GAME_TRANSACTION, "单一钱包游戏交易纪录"),
    global_report_game_seamless_sport_transaction(RoutingKeyConstant.GLOBAL_REPORT_GAME_SEAMLESS_SPORT_TRANSACTION, "单一钱包体育交易纪录"),
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

    // 活动相关事件
    global_report_activity_member_award(RoutingKeyConstant.GLOBAL_REPORT_ACTIVITY_MEMBER_AWARD, "流水变动事件"),

    // 批次事件
    global_event_batch_routing_key(RoutingKeyConstant.GLOBAL_EVENT_BATCH_ROUTING_KEY, "全局事件批次路由鍵"),
    ;

    private final String routingKeyName;
    private final String desc;
}
