package com.gl.myparadise.common.bean.constant;

public class RoutingKeyConstant {
    public final static String GLOBAL_USER_ROUTING_KEY = "global-user-route-key";
    public final static String GLOBAL_USER_OTHER_ROUTING_KEY = "global-user-other-route-key";
    public final static String GLOBAL_SYSTEM_OPERATION_LOG_ROUTING_KEY = "global-system-operation-log-route-key";
    public final static String GLOBAL_FUND_ROUTING_KEY = "global-fund-routing-key";
    public final static String GLOBAL_ACTIVITY_ROUTING_KEY = "global-activity-routing-key";

    // 用戶相關事件
    public final static String GLOBAL_REPORT_USER_LOGIN = "global-report-user-login";
    public final static String GLOBAL_REPORT_USER_REGISTER = "global-report-user-register";
    public final static String GLOBAL_REPORT_MEMBER_VIP = "global-report-member-vip";

    // 遊戲相關事件
    public final static String GLOBAL_REPORT_GAME_BETTING = "global-report-game-betting";
    public final static String GLOBAL_REPORT_GAME_TRANSFER = "global-report-game-transfer";
    public final static String GLOBAL_REPORT_GAME_WALLET = "global-report-game-wallet";

    // 財務相關事件
    public final static String GLOBAL_REPORT_FUND_RECHARGE = "global-report-fund-recharge";
    public final static String GLOBAL_REPORT_FUND_WITHDRAW = "global-report-fund-withdraw";
    public final static String GLOBAL_REPORT_FUND_BALANCE_CHANGE = "global-report-fund-balance-change";
    public final static String GLOBAL_REPORT_FUND_POINT_CHANGE = "global-report-fund-point-change";
    public final static String GLOBAL_REPORT_FUND_BET_FLOW_CHANGE = "global-report-fund-bet-flow-change";

    // 統計相關事件
    public final static String GLOBAL_REPORT_DAILY_BETTING = "global-report-daily-betting";
    public final static String GLOBAL_REPORT_DAILY_RECHARGE = "global-report-daily-recharge";

    // 活动相关事件
    public final static String GLOBAL_REPORT_ACTIVITY_MEMBER_AWARD = "global-report-activity-member-award";
} 