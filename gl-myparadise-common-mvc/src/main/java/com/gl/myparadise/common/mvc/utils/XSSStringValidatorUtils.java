package com.gl.myparadise.common.mvc.utils;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author Vincent
 * @description:
 * @className: InputValidatorUtil
 * @projectName: gl-cloud-base
 * @date 2025-07-23 - 00:16
 */
public class XSSStringValidatorUtils {

    private static final Pattern ILLEGAL_PATTERN = Pattern.compile(".*[<>\"'].*");

    public static boolean hasXSS(String value) {
        return value != null && ILLEGAL_PATTERN.matcher(value).matches();
    }

    public static void validateStringFields(Object obj) {
        if (obj == null) return;
        Class<?> clazz = obj.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            if (field.getType().equals(String.class)) {
                field.setAccessible(true);
                try {
                    String value = (String) field.get(obj);
                    if (hasXSS(value)) {
                        throw new IllegalArgumentException("param [" + field.getName() + "] include illegal characters");
                    }
                } catch (IllegalAccessException ignored) {}
            }
        }
    }

    public static void validateRawString(String value, String name) {
        if (hasXSS(value)) {
            throw new IllegalArgumentException("param [" + name + "] include illegal characters");
        }
    }

}
