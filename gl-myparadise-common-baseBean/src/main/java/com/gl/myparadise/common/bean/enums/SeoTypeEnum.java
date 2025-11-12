package com.gl.myparadise.common.bean.enums;

import java.util.Arrays;
import java.util.Objects;

/**
 * SEO 页面类型枚举。
 */
public enum SeoTypeEnum {
    ARTICLE_DETAIL(2, "文章详情"),
    ARTICLE_CATEGORY(1, "文章分类"),
    ARTICLE_TAG(4, "文章标签");


    private final int code;
    private final String description;

    SeoTypeEnum(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static SeoTypeEnum fromCode(Integer code) {
        if (Objects.isNull(code)) {
            return null;
        }
        return Arrays.stream(values())
                .filter(item -> item.code == code)
                .findFirst()
                .orElse(null);
    }

    public static String getDescriptionByCode(Integer code) {
        SeoTypeEnum typeEnum = fromCode(code);
        return typeEnum == null ? null : typeEnum.getDescription();
    }
}
