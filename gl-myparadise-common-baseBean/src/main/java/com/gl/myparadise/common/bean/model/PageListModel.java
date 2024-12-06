package com.gl.myparadise.common.bean.model;

import java.util.List;

public class PageListModel<T>  extends PageModel {

	private static final long serialVersionUID = -3784194549946242864L;
	private List<T> rsData = null;

	public List<T> getRsData() {
		return rsData;
	}

	public PageListModel setRsData(List<T> rsData) {

		this.rsData = rsData;

		return this;
	}

}
