package com.gl.myparadise.common.bean.enums;


import com.gl.myparadise.common.bean.constant.ProjectConstant;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BalanceSubTypeEnum {
    // 存款
    RECHARGE(ProjectConstant.Fund.BalanceType.RECHARGE, 1001, "充值"),

    // 提款
    WITHDRAW(ProjectConstant.Fund.BalanceType.WITHDRAW, 1001, "提现"),

    // 红利
    ACT_MANUAL_BONUS(ProjectConstant.Fund.BalanceType.BONUS, 3999, "人工发放"),
    VIP_UPGRADE_BONUS(ProjectConstant.Fund.BalanceType.BONUS, 3001, "升级礼金"),
    VIP_RETENTION_BONUS(ProjectConstant.Fund.BalanceType.BONUS, 3002, "保级礼金"),
    VIP_WEEKLY_SALARY(ProjectConstant.Fund.BalanceType.BONUS, 3003, "周俸禄"),
    VIP_MONTHLY_SALARY(ProjectConstant.Fund.BalanceType.BONUS, 3004, "月俸禄"),
    VIP_BIRTHDAY_BONUS(ProjectConstant.Fund.BalanceType.BONUS, 3005, "生日礼金"),
    VIP_UPGRADE_POINTS(ProjectConstant.Fund.BalanceType.BONUS, 3006, "升级积分"),
    VIP_RETENTION_POINTS(ProjectConstant.Fund.BalanceType.BONUS, 3007, "保级积分"),
    VIP_APP_DAILY_POINTS(ProjectConstant.Fund.BalanceType.BONUS, 3008, "app每日积分"),
    VIP_H5_DAILY_POINTS(ProjectConstant.Fund.BalanceType.BONUS, 3009, "h5每日积分"),
    ACT_REALTIME_REBATE_BONUS(ProjectConstant.Fund.BalanceType.BONUS, 3010, "时时返水奖励"),
    ACT_REBATE_BONUS(ProjectConstant.Fund.BalanceType.BONUS, 3011, "返水奖励"),

    // 人工存入
    MANUAL(ProjectConstant.Fund.BalanceType.MANUAL, 5001, "人工發放"),

    // 游戏
    GAME_TRANSFER_IN(ProjectConstant.Fund.BalanceType.GAME, 6001, "游戏转入"),
    GAME_TRANSFER_OUT(ProjectConstant.Fund.BalanceType.GAME, 6002, "游戏转出"),

    // 积分兑换
    POINT_IN(ProjectConstant.Fund.BalanceType.POINT, 7001, "积分兑换"),

    ;

    private final Integer transactionTyp;

    private final Integer subType;

    private final String desc;


}