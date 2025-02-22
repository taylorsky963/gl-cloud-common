package com.gl.myparadise.common.bean.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@ToString(callSuper = true)
public class PageModel extends MemberRequestModel {

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

    public Integer getLimitIndex(){
        if(this.pageNum != null && this.pageNum > 0){
            return (this.pageNum - 1) * pageSize;
        }
        return 0;
    }
    public PageModel addPage() {
        setPageNum(getPageNum() + 1);
        return this;
    }
}
