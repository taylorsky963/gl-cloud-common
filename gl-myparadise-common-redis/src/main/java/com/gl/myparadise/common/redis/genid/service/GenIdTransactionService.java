package com.gl.myparadise.common.redis.genid.service;


public interface GenIdTransactionService {
	
	/**
	 * 第一次加载
	 * @param
	 */
	boolean initGenIdByDB(String genKey, String subKey);

    /**
     * 如果大于最大值就从数据库里取
	 * @param genKey
     * @param subKey
	 */
	void setGenIdByDB(String genKey, String subKey);

}
