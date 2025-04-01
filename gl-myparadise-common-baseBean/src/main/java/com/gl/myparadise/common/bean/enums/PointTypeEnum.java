package com.gl.myparadise.common.bean.enums;


import lombok.Getter;

/**
 * 积分变动类型
 */
@Getter
public enum PointTypeEnum {

    MANUAL_ADJUSTMENT_INCREASE(71901001, "人工调整增加"),

    MANUAL_ADJUSTMENT_DEDUCTION(71901002, "人工调整扣减"),

    CASH_EXCHANGE(71901003, "积分兑换现金"),

    UPGRADE_POINTS(71901004, "升级积分"),

    RELEGATION_POINTS(71901005, "保级积分"),

    APP_DAILY_POINTS(71901006, "app每日积分"),

    H5_DAILY_POINTS(71901007, "h5每日积分"),

    ;

    /**
     * 编码
     */
    private final Integer code;

    /**
     * 描述
     */
    private final String desc;

    PointTypeEnum(Integer code, String desc) {
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