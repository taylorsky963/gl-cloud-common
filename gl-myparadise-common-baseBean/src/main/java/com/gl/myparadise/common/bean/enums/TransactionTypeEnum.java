package com.gl.myparadise.common.bean.enums;


import lombok.Getter;

/**
 * 账户钱包变动类型
 */
@Getter
public enum TransactionTypeEnum {

    TRANSFER_OUT(71801001, "从游戏转出"),  // 游戏转出 YL SLOT
    TRANSFER_IN(71801002, "转入游戏"), //转入游戏YL SLOT
    MANUAL_DEPOSIT(71801003, "人工存入"), //人工存提
    WITHDRAWAL(71801004, "提现"), // 提现
    DEPOSIT(71801005, "存款"), // 充值
    DISCOUNT(71801006, "优惠"), //VIPX升级礼金 VIPX周俸禄
    POINTS(71801007, "积分"), // 积分兑换
    ;

    /**
     * 编码
     */
    private final Integer code;

    /**
     * 描述
     */
    private final String desc;

    TransactionTypeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

}