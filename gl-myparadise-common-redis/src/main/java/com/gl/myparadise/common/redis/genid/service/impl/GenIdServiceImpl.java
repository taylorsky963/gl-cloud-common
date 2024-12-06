package com.gl.myparadise.common.redis.genid.service.impl;

import com.gl.myparadise.common.redis.genid.service.GenIdService;
import com.gl.myparadise.common.redis.genid.service.GenIdTransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicLong;

@Service
public class GenIdServiceImpl  implements GenIdService {

	private static final Logger logger = LoggerFactory.getLogger(GenIdServiceImpl.class);

	@Autowired(required = false)
	@Qualifier("genIdTransactionService")
	private GenIdTransactionService genIdTransactionService;

	@Override
	public Long getGenId(String genKey, String subKey) {

		Long currNo = null;

		if (!GenIdHelper.getInstance().containsKey(genKey, subKey)) {

			logger.info("getGenId GenIdHelper.getInstance().containsKey(genKey, subKey) false");

			if(genIdTransactionService.initGenIdByDB(genKey, subKey)){

				logger.info("genIdTransactionService.initGenIdByDB(genKey, subKey) true");

				return null;
			}

		}

		synchronized (GenIdHelper.getInstance().getLockObject(genKey, subKey)) {
			
			currNo = getGenIdByCache(genKey, subKey);
				
			logger.info("return currNo = " + currNo);

		}

		return currNo;

	}
	
	/**
	 * 比较当前值和最大值，返回自增ID
	 * @param genKey
	 * @param subKey
	 * @return
	 */
	private Long getGenIdByCache(String genKey, String subKey) {
		
		Long maxId = GenIdHelper.getInstance().getMaxID(genKey, subKey)	;
		
		logger.info("getGenIdByCache maxId = " + maxId);
		
		AtomicLong currNo = GenIdHelper.getInstance().getGenIDValue(genKey, subKey);
		
		logger.info("getGenIdByCache currNo = " + currNo);
		
		if(currNo.get() >= maxId){
			
			genIdTransactionService.setGenIdByDB(genKey, subKey);
		
		}

		return Long.valueOf(currNo.getAndIncrement());
		
	}

}
