package com.gl.myparadise.common.tools.bean;

import org.apache.commons.lang3.Validate;

import java.lang.reflect.Field;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: BeanUtil
 * @ProjectName: myparadise-common
 * @Date 2023-05-20 - 18:15
 */
public class BeanUtil {


    public static List<Class<?>> getsuperClass(Class<?> calzz) {

        List<Class<?>> listSuperClass = new ArrayList<Class<?>>();

        Class<?> superclass = calzz.getSuperclass();

        while (superclass != null) {

            if (superclass.getName().equals("java.lang.Object")) {

                break;

            }

            listSuperClass.add(superclass);

            superclass = superclass.getSuperclass();

        }

        return listSuperClass;

    }

    public static List<Field> getAllFieldsList(final Class<?> cls) {

        Validate.isTrue(cls != null, "The class must not be null");

        final List<Field> allFields = new ArrayList<Field>();

        Class<?> currentClass = cls;

        while (currentClass != null) {

            final Field[] declaredFields = currentClass.getDeclaredFields();

            for (final Field field : declaredFields) {

                if (!Modifier.isFinal(field.getModifiers())) {

                    allFields.add(field);

                }

            }

            currentClass = currentClass.getSuperclass();

        }

        return allFields;
    }

    public static List<Field> getFieldsList(final Class<?> cls) {

        Validate.isTrue(cls != null, "The class must not be null");

        final List<Field> allFields = new ArrayList<Field>();

        Class<?> currentClass = cls;

        final Field[] declaredFields = currentClass.getDeclaredFields();

        for (final Field field : declaredFields) {

            if (!Modifier.isFinal(field.getModifiers())) {

                allFields.add(field);

            }

        }

        return allFields;
    }

}
