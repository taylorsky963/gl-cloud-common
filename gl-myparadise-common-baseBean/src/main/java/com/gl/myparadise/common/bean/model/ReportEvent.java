package com.gl.myparadise.common.bean.model;


import lombok.AllArgsConstructor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@AllArgsConstructor
public enum ReportEvent {

    Recharge(1000, "充值"),

    Withdraw(1001, "提现"),

    Transfer(1002, "转账"),

    Betting(1003, "投注"),

    Login(1005, "用户登录"),

    Register(1004, "用户注册"),

    WithdrawReturn(1007, "提现退回"),

    Bonus(1009, "红利"),

    SubCoin(1011, "减币"),

    Commission(1012, "佣金"),

    AddCoin(1018, "加币"),

    GameUserBalance(1014, "游戏余额变动"),

    Rebate(1010, "返水"),

    UpAmount(1015, "代充"),

    BetEffectClear(1026, "流水清零"),

    BettingBalance(1020, "流水详情"),

    WithdrawParentOrder(1025, "大额提现拆单"),

    BankDeductionCredit(1027, "代充清算-银行卡充值清算"),

    ProxyRechargeRebateRebate(1029, "代充返利结算"),

    BalanceTransfer(1030, "转账"),

    BalanceChange(1031, "账变"),

    ProxyPayoutRecharge(1035, "会员代充"),

    ProxyTransferOut(1036, "代理转账"),

    ProxyTransferIn(1037, "代理转账"),

    ProxyTransferReturn(1038, "代理转账退回"),

    SubCoinReturn(1040, "减币退回"),

    LiveGiftPresent(1041, "直播礼物赠送记录"),

    VipExp(1042, "VIP经验值变动"),

    GoldMallBalanceRecord(1043, "金币商城交易明细"),

    ShareWalletGame(2000, "游戏-共用钱包"),

    BalanceRecord(3000, "资金明细"),

    DigitalWithdraw(5000, "提币"),

    GuessingBetRecord(7000, "竞猜投注记录"),

    DigitalRecharge(5001, "充币"),

    DigitalTransferReturn(5002, "转账失败退回"),

    DigitalAddCoin(5003, "加币"),

    DigitalSubtractCoin(5004, "减币"),

    JPushRecord(5005, "极光推送"),

    OkHttpLogReport(6001, "极光推送"),

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

    public String desc() {
        return this.desc;
    }

    public static ReportEvent getReportEvent(int eventId) {
        return eventMap.get(eventId);
    }

}