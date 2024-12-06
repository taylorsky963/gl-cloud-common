package com.gl.myparadise.common.tools.jason;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.ToNumberPolicy;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.Map;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: GJonUtil
 * @ProjectName: myparadise-common
 * @Date 2023-05-07 - 20:33
 */
public class GJonUtil {

    private static Gson gson;

    static {

        gson = new GsonBuilder().setNumberToNumberStrategy(ToNumberPolicy.LONG_OR_DOUBLE).setObjectToNumberStrategy(ToNumberPolicy.LONG_OR_DOUBLE)
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();

    }

    /**
     * 获取GsonBuilder实例
     *
     * @return
     */
    public static GsonBuilder builder() {
        return new GsonBuilder();
    }

    /**
     * 将对象转为json字符串
     *
     * @param object
     * @return
     */
    public static String toJson(Object object) {

        String json = null;

        if (gson != null) {

            json = gson.toJson(object);

        }

        return json;
    }

    /**
     * 将json字符串转为指定类型的实例
     *
     * @param json
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> T parse(String json, Class<T> cls) {

        T t = null;

        if (gson != null) {

            t = gson.fromJson(json, cls);

        }

        return t;
    }

    /**
     * 将json字符串转为指定类型的实例
     *
     * @param json
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> T parse(Object json, Class<T> cls) {

        T t = null;

        if (gson != null) {

            t = gson.fromJson(toJson(json), cls);

        }

        return t;
    }

    /**
     * 将json转为Map
     *
     * @param json
     * @param <T>
     * @return
     */
    public static <T> Map<String, T> toMap(String json) {

        Map<String, T> map = null;

        if (gson != null) {

            map = gson.fromJson(json, new TypeToken<Map<String, T>>() {
            }.getType());

        }

        return map;
    }

    /**
     * 将json转为指定类型的List
     *
     * @param json
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> List<T> toList(String json, Class<T> cls) {

        List<T> list = null;

        if (gson != null) {

            // 根据泛型返回解析指定的类型,TypeToken<List<T>>{}.getType()获取返回类型
            list = gson.fromJson(json, new TypeToken<List<T>>() {
            }.getType());

        }

        return list;
    }

    /**
     * 将json转为Map List
     *
     * @param json
     * @param <T>
     * @return
     */
    public static <T> List<Map<String, T>> toMapList(String json) {

        List<Map<String, T>> list = null;

        if (gson != null) {

            list = gson.fromJson(json,
                    new TypeToken<List<Map<String, T>>>() {
                    }.getType());

        }

        return list;

    }
}
