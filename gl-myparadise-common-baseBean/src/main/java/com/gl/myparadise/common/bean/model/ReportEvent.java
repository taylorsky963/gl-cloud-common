package com.gl.myparadise.common.bean.model;


import lombok.AllArgsConstructor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@AllArgsConstructor
public enum ReportEvent {

    Recharge(1000, "充值"),

    Withdraw(1001, "提现"),

    Betting(1003, "投注"),

    Register(1004, "用户注册"),

    Login(1005, "用户登录"),

    GameTransfer(1016, "用戶遊戲轉帳紀錄"),

    MemberVip(1017, "用戶VIP上報"),

    BalanceChange(1018, "用户余额变动上报"),

    UserWallet(1019, "用户钱包建立上报"),

    PointChange(1020, "用户积分变动上报"),

    BetFlowChange(1021, "用户流水变动上报"),

    ActivityMemberAward(1022, "活動领奖纪录上报"),

    DailyUserBetting(7000, "每日用户投注数据上報"),

    DailyUserRecharge(7001, "每日用户充值数据上報"),

    SystemOperationLogReport(6002, "系统操作日志"),
    ;

    private final int value;
    private final String desc;

    private final static Map<Integer, ReportEvent> eventMap;

    static {
        eventMap = new ConcurrentHashMap<>();
        for (ReportEvent event : ReportEvent.values()) {
            eventMap.put(event.value, event);
        }
    }

    public int value() {
        return this.value;
    }

    public String string() {
        return String.valueOf(this.value);
    }

    public String desc() {
        return this.desc;
    }

    public static ReportEvent getReportEvent(int eventId) {
        return eventMap.get(eventId);
    }

}