package com.gl.myparadise.common.bean.enums;


import lombok.Getter;

import java.util.*;

/**
 * 数字货币枚举类
 * 此类表示不同的数字货币及其相关属性。
 */
@Getter
public enum DigitalCoinEnum {

    USDT("USDT", "USDT", false, "ETH"),

    TON("TON", "TON", false, "ETH"),

    ETH("ETH", "ETH", false, "BTC"),

    BTC("BTC", "BTC", false, "CNY"),

    CNY("CNY", "￥", true, "VND"),

    VND("VND", "₫", true, "IDR"),

    IDR("IDR", "Rp", true, "MYR"),

    MYR("MYR", "RM", true, "USDT"),

    THB("THB", "฿", true, "TRY"),

    TRY("TRY", "₺", true, "USD"),

    USD("USD", "$", true, "JPY"),

    JPY("JPY", "¥", true, "BRL"),

    BRL("BRL", "R$", true, "MXN"),

    MXN("MXN", "$", true, "EGP"),

    EGP("EGP", "E£", true, "NGN"),

    NGN("NGN", "₦", true, "PHP"),

    PHP("PHP", "₱", true, "USDT"),
    ;

    /**
     * 货币代码
     */
    private final String code;

    /**
     * 货币描述
     */
    private final String desc;

    /**
     * 是否为法定货币
     */
    private final boolean isCurrency;

    /**
     * 下一个货币代码
     */
    private final String nextCode;

    private static final Map<String, DigitalCoinEnum> coinEnumMap = new HashMap<>();

    private static final List<String> currencyCoinList = new ArrayList<>();

    private static final List<String> coinList = new ArrayList<>();

    DigitalCoinEnum(String code, String desc, Boolean isCurrency, String nextCode) {
        this.code = code;
        this.desc = desc;
        this.isCurrency = isCurrency;
        this.nextCode = nextCode;
    }

    static {
        Arrays.stream(DigitalCoinEnum.values()).forEach(coinEnum -> {
            coinEnumMap.put(coinEnum.getCode(), coinEnum);
            if (coinEnum.isCurrency()) {
                currencyCoinList.add(coinEnum.getCode());
            } else {
                coinList.add(coinEnum.getCode());
            }
        });
    }

    public static DigitalCoinEnum getDigitalCoin(String coin) {
        return coinEnumMap.get(coin);
    }

    public static DigitalCoinEnum defaultDigitalCoin() {
        return CNY;
    }

    public static Map<String, DigitalCoinEnum> getCoinMap() {
        return Collections.unmodifiableMap(coinEnumMap);
    }

    public static List<String> getCurrencyCoinList() {
        return Collections.unmodifiableList(currencyCoinList);
    }

    public static List<String> getCoinList() {
        return Collections.unmodifiableList(coinList);
    }
}