package com.gl.myparadise.common.tools.util;

import lombok.SneakyThrows;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.util.*;
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


    public static <T> List<List<T>> splitList(Collection<T> list, int splitSize) {
        // 檢查輸入參數
        if (list == null || list.isEmpty()) {
            return new ArrayList<>(); // 如果輸入為空，返回空列表
        }
        if (splitSize <= 0) {
            throw new IllegalArgumentException("splitSize 必須大於 0");
        }

        // 將集合轉為列表以便操作
        List<T> sourceList = new ArrayList<>(list);
        List<List<T>> result = new ArrayList<>();

        // 計算需要分割的子列表數量
        int size = sourceList.size();
        for (int i = 0; i < size; i += splitSize) {
            // 計算子列表的結束索引
            int end = Math.min(i + splitSize, size);
            // 截取子列表並加入結果
            result.add(new ArrayList<>(sourceList.subList(i, end)));
        }

        return result;
    }

    public static <T> List<Set<T>> splitListToSet(Collection<T> list, int splitSize) {
        // 檢查輸入參數
        if (list == null || list.isEmpty()) {
            return new ArrayList<>(); // 如果輸入為空，返回空列表
        }
        if (splitSize <= 0) {
            throw new IllegalArgumentException("splitSize 必須大於 0");
        }

        // 將集合轉為列表以便操作
        List<T> sourceList = new ArrayList<>(list);
        List<Set<T>> result = new ArrayList<>();

        // 計算需要分割的子列表數量
        int size = sourceList.size();
        for (int i = 0; i < size; i += splitSize) {
            // 計算子列表的結束索引
            int end = Math.min(i + splitSize, size);
            // 截取子列表並加入結果
            result.add(new HashSet<>(sourceList.subList(i, end)));
        }

        return result;
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
