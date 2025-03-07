package com.gl.myparadise.common.bean.enums;

import lombok.Getter;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

/**
 * 数字货币精度枚举类
 * 此类定义了不同数字货币的代码、精度、小数点后保留位数、数学上下文以及最低限额等信息。
 */
@Getter
public enum DigitalCoinScaleEnum {

    USDT("USDT", DigitalCoinEnum.USDT, 4, new MathContext(4, RoundingMode.HALF_UP), BigDecimal.valueOf(0.01d)),

    BRL("BRL", DigitalCoinEnum.BRL, 5, new MathContext(5, RoundingMode.HALF_UP), BigDecimal.valueOf(0.001d)),

    ETH("ETH", DigitalCoinEnum.ETH, 6, new MathContext(6, RoundingMode.HALF_UP), BigDecimal.valueOf(0.0001d)),

    BTC("BTC", DigitalCoinEnum.BTC, 8, new MathContext(8, RoundingMode.HALF_UP), BigDecimal.valueOf(0.0001d)),

    CNY("CNY", DigitalCoinEnum.CNY, 2, new MathContext(2, RoundingMode.HALF_UP), BigDecimal.valueOf(0.01d)),

    VND("VND", DigitalCoinEnum.VND, 2, new MathContext(0, RoundingMode.HALF_UP), BigDecimal.valueOf(0.01d)),

    MYR("MYR", DigitalCoinEnum.MYR, 2, new MathContext(2, RoundingMode.HALF_UP), BigDecimal.valueOf(0.01d)),

    IDR("IDR", DigitalCoinEnum.IDR, 2, new MathContext(2, RoundingMode.HALF_UP), BigDecimal.valueOf(0.01d)),

    THB("THB", DigitalCoinEnum.THB, 2, new MathContext(2, RoundingMode.HALF_UP), BigDecimal.valueOf(0.01d)),

    TRY("TRY", DigitalCoinEnum.TRY, 2, new MathContext(2, RoundingMode.HALF_UP), BigDecimal.valueOf(0.01d)),

    USD("USD", DigitalCoinEnum.USD, 2, new MathContext(2, RoundingMode.HALF_UP), BigDecimal.valueOf(0.01d)),

    EGP("EGP", DigitalCoinEnum.EGP, 2, new MathContext(2, RoundingMode.HALF_UP), BigDecimal.valueOf(0.01d)),

    ;

    private final String code;

    private final DigitalCoinEnum coinEnum;

    private final int scale;

    private final MathContext mathContext;

    private final BigDecimal minLimitMoney;

    private final static Map<String, DigitalCoinScaleEnum> coinEnumCodeMap = new HashMap<>();

    private final static Map<DigitalCoinEnum, DigitalCoinScaleEnum> coinEnumMap = new HashMap<>();

    DigitalCoinScaleEnum(String code, DigitalCoinEnum coinEnum, int scale, MathContext mathContext, BigDecimal minMoney) {
        this.code = code;
        this.coinEnum = coinEnum;
        this.scale = scale;
        this.mathContext = mathContext;
        this.minLimitMoney = minMoney;
    }

    static {
        for (DigitalCoinScaleEnum coinEnum : DigitalCoinScaleEnum.values()) {
            coinEnumCodeMap.put(coinEnum.getCode(), coinEnum);
            coinEnumMap.put(coinEnum.getCoinEnum(), coinEnum);
        }
    }

    public static int getScale(String code) {
        return coinEnumCodeMap.getOrDefault(code, DigitalCoinScaleEnum.VND).getScale();
    }

    public static int getScale(DigitalCoinEnum coinEnum) {
        return coinEnumMap.getOrDefault(coinEnum, DigitalCoinScaleEnum.VND).getScale();
    }

    public static MathContext getMathContext(String code) {
        return coinEnumCodeMap.getOrDefault(code, DigitalCoinScaleEnum.VND).getMathContext();
    }

    public static MathContext getMathContext(DigitalCoinEnum coinEnum) {
        return coinEnumMap.getOrDefault(coinEnum, DigitalCoinScaleEnum.VND).getMathContext();
    }

    public static BigDecimal getMinLimitMoney(String code) {
        return coinEnumCodeMap.getOrDefault(code, DigitalCoinScaleEnum.VND).getMinLimitMoney();
    }

    public static BigDecimal scale(String coinCode, BigDecimal amount) {
        if (coinCode == null || amount == null) {
            return amount;
        }
        return amount.setScale(DigitalCoinScaleEnum.getScale(coinCode), DigitalCoinScaleEnum.getMathContext(coinCode).getRoundingMode());
    }
}