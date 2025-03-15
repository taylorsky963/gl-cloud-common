package com.gl.myparadise.common.tools.util;

import lombok.SneakyThrows;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: ListUtil
 * @ProjectName: myparadise-common
 * @Date 2023-04-18 - 1:21
 */
public class ListUtil {

    private ListUtil() {
    }


    public static <T> List<List<T>> splitList(List<T> list, int splitSize) {
        //判断集合是否为空
        if (CollectionUtils.isEmpty(list))
            return Collections.emptyList();
        //计算分割后的大小
        int maxSize = (list.size() + splitSize - 1) / splitSize;
        //开始分割
        return Stream.iterate(0, n -> n + 1)
                .limit(maxSize)
                .parallel()
                .map(a -> list.parallelStream().skip(a * splitSize).limit(splitSize).collect(Collectors.toList()))
                .filter(b -> !b.isEmpty())
                .collect(Collectors.toList());
    }

    public static boolean isNotEmpty(List list) {
        //判断集合是否为空
       return !CollectionUtils.isEmpty(list);


    }

    /**
     * 判断是否是集合类 list 数组
     * @param obj
     * @return
     */
    public static boolean isCollection(Object obj) {

        Class userCla = (Class) obj.getClass();

        return isCollection(userCla);

    }

    /**
     * 断是否是集合类 list 数组
     * @param userCla
     * @return
     */
    public static boolean isCollection(Class userCla) {

        //判断返回类型是否是集合类型
        boolean isCollection = Collection.class.isAssignableFrom(userCla);

        //判断返回类型是否是数组类型
        boolean isArray = userCla.isArray();

        return isCollection || isArray;
    }

    @SneakyThrows
    public static <S, T> List<T> convertList(List<S> sourceList, Class<T> targetClass)
             {
        if (CollectionUtils.isEmpty(sourceList)) {
            return new ArrayList<>();
        }

        List<T> result = new ArrayList<>();
        for (S source : sourceList) {
            if (source != null) {
                T target = targetClass.getDeclaredConstructor().newInstance();
                BeanUtils.copyProperties(source, target);
                result.add(target);
            }
        }
        return result;
    }
}
