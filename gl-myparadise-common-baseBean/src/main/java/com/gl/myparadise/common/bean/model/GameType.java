package com.gl.myparadise.common.bean.model;

import java.util.HashMap;
import java.util.Map;

public enum GameType {

    SLOT("1", "电子"),
    LIVE("2", "真人"),
    SPORT("3", "体育"),
    LOTTERY("4", "彩票"),
    FISH("5", "捕鱼"),
    CHESS("6", "棋牌"),
    XLIVE("9", "竞猜"),
    ESPORTS("10", "电竞"),
    UNKNOWN("0", "未知");

    private static final Map<String, GameType> LOOKUP_MAP = new HashMap<>();

    static {
        for (GameType gameType : GameType.values()) {
            LOOKUP_MAP.put(gameType.key, gameType);
        }
    }

    private final String key;
    private final String val;

    GameType(String key, String val) {
        this.key = key;
        this.val = val;
    }

    public String key() {
        return key;
    }

    public String val() {
        return val;
    }

    @Override
    public String toString() {
        return val;
    }

    public static String val(String key) {
        return getEnum(key).val;
    }

    public static GameType getEnum(String key) {
        return LOOKUP_MAP.getOrDefault(key, UNKNOWN);
    }
}
