package com.gl.myparadise.common.bean.model;

import java.util.HashMap;
import java.util.Map;

/**
 * 实体店用户展示版本（Display Version）。
 *
 * <p>线下门店机台按 4 项 offering 的组合控制可展示 / 可搜索的游戏品类，共 15 个组合：
 * <ul>
 *     <li>E-Games（电子 / 真人 / 捕鱼 / 棋牌，排除 SG 厂商的 livecasino、poker）</li>
 *     <li>E-Bingo（BINGO 品类，除 Bingo Pilipino Live 外的全部 bingo）</li>
 *     <li>Sportsbook（体育）</li>
 *     <li>Traditional Bingo（仅 fbm 的 Bingo Pilipino Live，唯一 OTB 传统 bingo）</li>
 * </ul>
 *
 * <p>本枚举仅描述每个版本由哪些 offering 组成，是 Java 端版本定义的单一可信源。
 * 具体「品类白名单 / 平台排除规则」由展示端按 offering 派生（见 StoreGameVisibilityFilterService），
 * 校验端只需判断版本号是否合法（{@link #isValidStoreVersion(String)}）。
 */
public enum DisplayVersionEnum {

    V1(true, false, false, false),
    V2(true, true, false, false),
    V3(false, true, false, false),
    V4(false, false, true, false),
    V5(false, false, false, true),
    V6(true, false, true, false),
    V7(true, false, false, true),
    V8(false, true, true, false),
    V9(false, true, false, true),
    V10(false, false, true, true),
    V11(true, true, true, false),
    V12(true, true, false, true),
    V13(true, false, true, true),
    V14(false, true, true, true),
    V15(true, true, true, true);

    private static final Map<String, DisplayVersionEnum> LOOKUP_MAP = new HashMap<>();

    static {
        for (DisplayVersionEnum version : DisplayVersionEnum.values()) {
            LOOKUP_MAP.put(version.name(), version);
        }
    }

    /** 是否包含 E-Games（电子 / 真人 / 捕鱼 / 棋牌）。 */
    private final boolean eGames;

    /** 是否包含 E-Bingo（除 Bingo Pilipino Live 外的 bingo）。 */
    private final boolean eBingo;

    /** 是否包含 Sportsbook（体育）。 */
    private final boolean sportsbook;

    /** 是否包含 Traditional Bingo（仅 Bingo Pilipino Live）。 */
    private final boolean traditionalBingo;

    DisplayVersionEnum(boolean eGames, boolean eBingo, boolean sportsbook, boolean traditionalBingo) {
        this.eGames = eGames;
        this.eBingo = eBingo;
        this.sportsbook = sportsbook;
        this.traditionalBingo = traditionalBingo;
    }

    public boolean hasEGames() {
        return eGames;
    }

    public boolean hasEBingo() {
        return eBingo;
    }

    public boolean hasSportsbook() {
        return sportsbook;
    }

    public boolean hasTraditionalBingo() {
        return traditionalBingo;
    }

    /**
     * 按版本号（不区分大小写、容忍空白）解析枚举，非法返回 {@code null}。
     */
    public static DisplayVersionEnum fromCode(String code) {
        if (code == null || code.trim().isEmpty()) {
            return null;
        }
        return LOOKUP_MAP.get(code.trim().toUpperCase());
    }

    /**
     * 校验是否为合法的实体店展示版本（V1–V15）。
     */
    public static boolean isValidStoreVersion(String code) {
        return fromCode(code) != null;
    }
}
