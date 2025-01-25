package com.gl.myparadise.common.redis.client.utils;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: AspertUtil
 * @ProjectName: ly-common
 * @Date 2023-04-19 - 2:12
 */
public class AspertUtil {

    private static final Logger logger = LoggerFactory.getLogger(AspertUtil.class);

    private AspertUtil() {

    }

    public static Map<String, Object> getMethodNameAndValueMap(JoinPoint joinPoint) {

        Map<String, Object> param = new HashMap<>();

        Object[] paramValues = joinPoint.getArgs();

        String[] paramNames = getMethodSignature(joinPoint).getParameterNames();

        for (int i = 0; i < paramNames.length; i++) {

            if (!CacheKeyUtil.isCollection(paramValues[i])) {

                param.put(paramNames[i], paramValues[i]);

            }

        }

        return param;
    }

    public static Class getMethodReturnType(JoinPoint joinPoint) {

        return getAsperMethod(joinPoint).getReturnType();

    }

    public static Method getAsperMethod(JoinPoint joinPoint) {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        return signature.getMethod();

    }

    public static Class getAsperClass(JoinPoint joinPoint) {

        return joinPoint.getTarget().getClass();

    }

    public static MethodSignature getMethodSignature(JoinPoint joinPoint) {

        return (MethodSignature) joinPoint.getSignature();

    }

    public static String getListActualType(Object o){

        return getListActualType(o, 0);

    }

    /**
     *  获取LIST的范型参数类型名
     * @param obj
     * @param index
     * @return
     */
    public static String getListActualType(Object obj, int index) {

        String actualTypeName = null;

        try {

            if (obj instanceof List){

                if (!((List) obj).isEmpty()){

                    actualTypeName = ((List) obj).get(0).getClass().getName();
                }
            }

//            Type clazz = o.getClass().getGenericSuperclass();
//
//            if (!(clazz instanceof ParameterizedType)) {
//
//                logger.warn(clazz.getTypeName() + "'s superclass not ParameterizedType");
//
//                return o.getClass().getName();
//
//            }
//
//            ParameterizedType pt = (ParameterizedType) clazz;
//
//            Type[] params = pt.getActualTypeArguments();
//
//            if ((index >= params.length) || (index < 0)) {
//
//                logger.warn("Index: " + index + ", Size of " + clazz.getTypeName() + "'s Parameterized Type: "
//                        + params.length);
//
//                return o.getClass().getName();
//
//            }
//
//            if (!(params[index] instanceof Class)) {
//
//                logger.warn(clazz.getTypeName() + " not set the actual class on superclass generic parameter");
//
//                return o.getClass().getName();
//
//            }
//
//            actualTypeName = params[index].getTypeName();

        } catch (Exception e) {

            logger.error("getListActualType is error", e);

        }

//        logger.info("getListActualType actualTypeName is : " + actualTypeName);

        return actualTypeName;

    }

    /**
     * 通过类名获取类
     * @param elementName
     * @return
     */
    public static Class getActualTypeClass(String elementName) {

        try {

            return Class.forName(elementName);

        } catch (ClassNotFoundException e) {

            logger.error("getActualTypeClass is error", e);

        }

        return null;
    }

}
