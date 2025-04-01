package com.gl.myparadise.common.tools.util;

import com.gl.myparadise.common.bean.model.PageListModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ListPageUtils implements Serializable {

    public static <T> PageListModel<T> page(List<T> list, Integer pageNum, Integer pageSize) {
        if (list == null) {
            list = new ArrayList<T>();
        }
        if (pageNum == null) {
            pageNum = 1;
        }
        if (pageSize == null) {
            pageSize = 10;
        }
        if (pageNum <= 0) {
            pageNum = 1;
        }

        int totalItems = list.size();
        List<T> pagingList = new ArrayList<>();

        long pageTotal = Math.min(((pageNum - 1) * pageSize) + pageSize, totalItems);
        for (int i = (pageNum - 1) * pageSize; i < pageTotal; i++) {
            pagingList.add(list.get(i));
        }

        PageListModel<T> result = new PageListModel<>(pagingList);
        result.setPageSize(pageSize);
        result.setPageNum(pageNum);
        result.setPageTotal((long) totalItems);
        result.setPages((totalItems + pageSize - 1) / pageSize);

        return result;
    }

    public static <T> PageListModel<T> page(List<T> list, Integer pageNum, Integer pageSize, Long totalItems) {
        if (list == null) {
            list = new ArrayList<T>();
            totalItems = 0L;
        }

        PageListModel<T> result = new PageListModel<>(list);
        result.setPageSize(pageSize);
        result.setPageNum(pageNum);
        result.setPageTotal(totalItems);
        result.setPages((int) ((totalItems + pageSize - 1) / pageSize));

        return result;
    }

}
