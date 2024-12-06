package com.gl.myparadise.common.bean.model;

import java.io.Serializable;

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

	public boolean isSqlCount() {
		return isSqlCount;
	}

	public void setSqlCount(boolean sqlCount) {
		isSqlCount = sqlCount;
	}

	public Long getPageTotal() {
		return pageTotal;
	}

	public void setPageTotal(Long pageTotal) {
		this.pageTotal = pageTotal;
	}

	public int getPages() {
		return pages;
	}

	public void setPages(int pages) {
		this.pages = pages;
	}

	public Integer getPageNum() {
		return pageNum;
	}

	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getLimitIndex(){
		if(this.pageNum != null && this.pageNum > 0){
			return (this.pageNum - 1) * pageSize;
		}
		return 0;
	}
	

}
