package com.gl.myparadise.common.bean.enums;


import com.gl.myparadise.common.bean.constant.ProjectConstant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.HashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
public enum BalanceSubTypeEnum {
    // 存款
    RECHARGE(ProjectConstant.Fund.BalanceType.RECHARGE, 1001, "充值"),

    // 提款
    WITHDRAW(ProjectConstant.Fund.BalanceType.WITHDRAW, 1002, "提现"),

    // 红利
    ACT_MANUAL_BONUS(ProjectConstant.Fund.BalanceType.BONUS, 3999, "人工发放优惠"),
    VIP_UPGRADE_BONUS(ProjectConstant.Fund.BalanceType.BONUS, 3001, "VIP升级礼金"),
    VIP_RETENTION_BONUS(ProjectConstant.Fund.BalanceType.BONUS, 3002, "VIP保级礼金"),
    VIP_WEEKLY_SALARY(ProjectConstant.Fund.BalanceType.BONUS, 3003, "VIP周俸禄"),
    VIP_MONTHLY_SALARY(ProjectConstant.Fund.BalanceType.BONUS, 3004, "VIP月俸禄"),
    VIP_BIRTHDAY_BONUS(ProjectConstant.Fund.BalanceType.BONUS, 3005, "VIP生日礼金"),
    VIP_UPGRADE_POINTS(ProjectConstant.Fund.BalanceType.BONUS, 3006, "VIP升级积分"),
    VIP_RETENTION_POINTS(ProjectConstant.Fund.BalanceType.BONUS, 3007, "VIP保级积分"),
    VIP_APP_DAILY_POINTS(ProjectConstant.Fund.BalanceType.BONUS, 3008, "VIPapp每日积分"),
    VIP_H5_DAILY_POINTS(ProjectConstant.Fund.BalanceType.BONUS, 3009, "VIPh5每日积分"),
    ACT_REALTIME_REBATE_BONUS(ProjectConstant.Fund.BalanceType.BONUS, 3010, "时时返水奖励"),
    ACT_REBATE_BONUS(ProjectConstant.Fund.BalanceType.BONUS, 3011, "返水奖励"),
    ACT_REGISTER_BONUS(ProjectConstant.Fund.BalanceType.BONUS, 3012, "注册奖励"),
    ACT_RECHARGE_BONUS(ProjectConstant.Fund.BalanceType.BONUS, 3013, "存款优惠"),
    ACT_DAILY_FIRST_RECHARGE_BONUS(ProjectConstant.Fund.BalanceType.BONUS, 3014, "每日首存优惠"),
    ACT_LOGIN_RECHARGE_LOGIN_BONUS(ProjectConstant.Fund.BalanceType.BONUS, 3015, "登入"),
    ACT_LOGIN_RECHARGE_RECHARGE_BONUS(ProjectConstant.Fund.BalanceType.BONUS, 3016, "充值"),
    ACT_REDEMPTION_CODE_BONUS(ProjectConstant.Fund.BalanceType.BONUS, 3017, "兑换码优惠"),

    // 人工存入
    MANUAL(ProjectConstant.Fund.BalanceType.MANUAL, 5001, "人工存入"),
    MANUAL_OUT(ProjectConstant.Fund.BalanceType.MANUAL, 5002, "人工转出"),

    // 人工存入 不计存提
    MANUAL_NOMEMO(ProjectConstant.Fund.BalanceType.MANUAL, 5003, "人工存入 不计存款订单"),
    MANUAL_OUT_NOMEMO(ProjectConstant.Fund.BalanceType.MANUAL, 5004, "人工转出 不计提款订单"),

    // 游戏
    GAME_TRANSFER_IN(ProjectConstant.Fund.BalanceType.GAME, 6001, "游戏转入"),
    GAME_TRANSFER_OUT(ProjectConstant.Fund.BalanceType.GAME, 6002, "游戏转出"),

    // 积分兑换
    POINT_CASH_EXCHANGE(ProjectConstant.Fund.BalanceType.POINT, 7001, "积分兑换現金"),
    POINT_CARGO_EXCHANGE(ProjectConstant.Fund.BalanceType.POINT, 7002, "积分兑换實物"),

    ;

    private final Integer transactionTyp;

    private final Integer subType;

    private final String desc;

    private static final Map<Integer, BalanceSubTypeEnum> SUB_TYPE_MAP = new HashMap<>();

    static {
        for (BalanceSubTypeEnum value : values()) {
            SUB_TYPE_MAP.put(value.getSubType(), value);
        }
    }

    public static BalanceSubTypeEnum valueOfSubType(Integer subType) {
        if (subType == null) {
            return null;
        }
        return SUB_TYPE_MAP.get(subType);
    }
}