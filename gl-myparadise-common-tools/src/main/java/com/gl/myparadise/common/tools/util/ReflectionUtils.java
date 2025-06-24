package com.gl.myparadise.common.tools.util;

import java.lang.reflect.Method;

public class ReflectionUtils {

    public static <T> T callFunction(Object obj, String methodName, Class<T> targetType) {
        if (obj == null || methodName == null || targetType == null) {
            return null;
        }

        try {
            Method method = obj.getClass().getMethod(methodName);
            Object result = method.invoke(obj);

            if (result == null) {
                return null;
            }

            if (targetType.isInstance(result)) {
                return targetType.cast(result);
            }

            return null;
        } catch (Exception e) {
            return null;
        }
    }
}
