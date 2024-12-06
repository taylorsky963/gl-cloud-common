package com.gl.myparadise.common.redis.genid.service;


public interface GenIdService {

	Long getGenId(String genKey, String subKey);

}
