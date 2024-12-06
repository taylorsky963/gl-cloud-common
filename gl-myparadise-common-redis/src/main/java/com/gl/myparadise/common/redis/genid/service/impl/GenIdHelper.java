package com.gl.myparadise.common.redis.genid.service.impl;


import com.gl.myparadise.common.redis.genid.common.GenIdConstants;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class GenIdHelper {
	
	//每条记录的线程锁
	private static ConcurrentHashMap<String, Object> mapLock = new ConcurrentHashMap<String, Object>();
	
	//每个记录的步长
	private static ConcurrentHashMap<String, Long> mapStep = new ConcurrentHashMap<String, Long>();
	
	//每个记录的ID值
	private static ConcurrentHashMap<String, AtomicLong> mapGenId = new ConcurrentHashMap<String, AtomicLong>();
	
	//每个记录的MAXID值
	private static ConcurrentHashMap<String, Long> mapMaxId = new ConcurrentHashMap<String, Long>();
	
	private static GenIdHelper cc = null;
	
	public static synchronized GenIdHelper getInstance() {
		
		if (cc == null){		
				
			cc = new GenIdHelper();
		}
		
		return cc;

	}
	
	private GenIdHelper(){
		
	}
	
	/**
	 * 设置每个自增ID的线程锁对象
	 * 
	 */	
	protected  void setLockObject(String genKey, String subKey){
		
		mapLock.put(buildLockKey(genKey, subKey), new Object());
	}
	
	/**
	 * 设置每个自增ID的线程锁对象
	 * 
	 */	
	protected Object getLockObject(String genKey, String subKey){
		
		return mapLock.get(buildLockKey(genKey, subKey));
	}
	
	/**
	 * 设置每个自增ID的最大值
	 * 
	 */	
	protected  void setMaxID(String genKey, String subKey, Long maxId){
		
		mapMaxId.put(buildLockKey(genKey, subKey), maxId);
	}
	
	/**
	 * 获取自增ID的最大值
	 * 
	 */	
	protected  Long getMaxID(String genKey, String subKey){
		
		return mapMaxId.get(buildLockKey(genKey, subKey));
	}
	
	/**
	 * 获取自增ID的值
	 * 
	 */	
	protected  AtomicLong getGenIDValue(String genKey, String subKey){
		
		return mapGenId.get(buildLockKey(genKey, subKey));
	}
	
	/**
	 * 设置获取自增ID的值
	 * 
	 */	
	protected void setGenIDValue(String genKey, String subKey, AtomicLong currNo){
		
		mapGenId.put(buildLockKey(genKey, subKey), currNo);
	}
	

	/**
     * 判断是否存在线程锁对象
	 * @param genKey
     * @param subKey
     * @return
     */
	protected  boolean containsKey(String genKey, String subKey){
		
		return mapLock.containsKey(buildLockKey(genKey, subKey));
	}
	
	private  String buildLockKey(String genKey, String subKey){
		
		return genKey + GenIdConstants.BASE_CACHE_SEPARATOR + subKey;
	}
	
	protected Long getGenIdSetp(String genKey, String subKey){
		
		Long step = mapStep.get(buildLockKey(genKey, subKey));
		
		if(step == null){
			
			step = GenIdConstants.GenIdStep;
		}
		
		return step;
		
	}

	protected void setGenIdSetp(String genKey, String subKey, Long step){

		if(step == null){

			step = GenIdConstants.GenIdStep;

		}

		mapStep.put(buildLockKey(genKey, subKey), step);

	}

}
