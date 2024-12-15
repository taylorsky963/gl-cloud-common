package com.gl.myparadise.common.bean.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class PageModel implements Serializable {

    private static final long serialVersionUID = 6798005976764102519L;
    /**
     * 当前页码
     */
    private Integer pageNum = 1;
    /**
     * 每页条数
     */
    private Integer pageSize = 10;

    private Long pageTotal;

    private int pages;

    private boolean isSqlCount = true;

}
