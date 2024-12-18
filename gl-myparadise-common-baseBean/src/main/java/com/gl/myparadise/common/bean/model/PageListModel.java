package com.gl.myparadise.common.bean.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class PageListModel<T>  extends PageModel {

	private static final long serialVersionUID = -3784194549946242864L;
	private List<T> rsData = null;

}
