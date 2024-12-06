package com.gl.myparadise.common.tools.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: QueryUtil
 * @ProjectName: myparadise-common
 * @Date 2023-04-18 - 1:20
 */
public class QueryUtil {

	private QueryUtil(){}

	/**
	 * 将请求参数还原为key=value的形式
	 * 
	 * @param params
	 * @return
	 */
	public static String getQueryString(Map<String, Object> params) {
		StringBuffer queryString = new StringBuffer(256);
		Iterator it = params.keySet().iterator();
		int count = 0;
		while (it.hasNext()) {
			String key = (String) it.next();
			String[] param = (String[]) params.get(key);
			for (int i = 0; i < param.length; i++) {
				if (count == 0) {
					count++;
				} else {
					queryString.append("&");
				}
				queryString.append(key);
				queryString.append("=");
				try {
					queryString.append(URLEncoder.encode((String) param[i], "UTF-8"));
				} catch (UnsupportedEncodingException e) {
				}
			}
		}
		return queryString.toString();
	}

	/**
	 * 获取request参数的值
	 * 
	 * @param parameters
	 * @param key
	 * @return
	 */
	public static String getParamValue(Map parameters, String key) {
		String[] value = (String[]) parameters.get(key);
		if (value != null && value.length > 0) {
			return value[0];
		}
		return null;
	}
}