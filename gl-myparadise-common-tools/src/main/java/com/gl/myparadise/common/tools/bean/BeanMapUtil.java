package com.gl.myparadise.common.tools.bean;

import com.gl.myparadise.common.tools.util.ListUtil;
import com.google.common.base.CaseFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: BeanMapUtil
 * @ProjectName: gl-common
 * @Date 2023-07-10 - 9:40 p.m.
 */
public class BeanMapUtil {

    private static final Logger logger = LoggerFactory.getLogger(BeanMapUtil.class);


    public static Map beanToMap(Object object, boolean isCase) throws IllegalAccessException {

        List<Field> fields = BeanUtil.getAllFieldsList(object.getClass());

        return getBeanToMap(object, isCase, fields, false);

    }

    public static Map beanToMapNotNull(Object object, boolean isCase) throws IllegalAccessException {

        List<Field> fields = BeanUtil.getAllFieldsList(object.getClass());

        return getBeanToMap(object, isCase, fields, true);

    }

    public static Map beanToMapNotParent(Object object, boolean isCase) throws IllegalAccessException {

        List<Field> fields = BeanUtil.getFieldsList(object.getClass());

        return getBeanToMap(object, isCase, fields, false);

    }

    public static Map beanToMapNotParentNotNull(Object object, boolean isCase) throws IllegalAccessException {

        List<Field> fields = BeanUtil.getFieldsList(object.getClass());

        return getBeanToMap(object, isCase, fields, true);

    }

    private static Map getBeanToMap(Object object, boolean isCase, List<Field> fields, boolean valueNotNull) throws IllegalAccessException {

        Map<String, Object> map = new HashMap<String, Object>();

        if(ListUtil.isNotEmpty(fields)){

            for (Field field : fields) {

                field.setAccessible(true);

                String key = field.getName();

                //将 bean 的名字转换为下划线 例如 userId -->> user_id
                if (isCase) {

                    key = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, key);
                }

                if(valueNotNull){

                    if(field.get(object) != null){

                        map.put(key, field.get(object));

                    }

                }else{

                    map.put(key, field.get(object));

                }


            }

        }

        return map;
    }

    public static <T> T mapToBeanCase(Map map, Class<T> beanClass) {

        return mapToBean(map, beanClass, true);

    }

    public static <T> T mapToBeanNoCase(Map map, Class<T> beanClass) {

        return mapToBean(map, beanClass, false);

    }

    public static <T> T mapToBean(Map map, Class<T> beanClass, boolean isCase) {

        try {

            T object = beanClass.newInstance();

            return mapToBean(map, object, isCase);

        } catch (Exception e) {

            logger.error("mapToBean is error", e);

        }

        return null;

    }

    public static <T> T mapToBeanNoCase(Map map, T object) {

        return mapToBean(map, object, false);
    }

    public static <T> T mapToBeanCase(Map map, T object) {

        return mapToBean(map, object, true);
    }


    public static <T> T mapToBean(Map map, T object, boolean isCase) {

        try {

            List<Field> fields = BeanUtil.getAllFieldsList(object.getClass());

            for (Field field : fields) {

                int mod = field.getModifiers();

                if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {

                    continue;

                }

                field.setAccessible(true);

                String key = field.getName();

                //将 bean 的名字转换为下划线 例如 userId -->> user_id
                if (isCase) {

                    key = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, key);
                }

                if (map.containsKey(key)) {

                    field.set(object, map.get(key));

                }

            }

            return object;

        } catch (Exception e) {

            logger.error("mapToBean is error", e);
        }

        return null;
    }
}