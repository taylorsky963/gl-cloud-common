package com.gl.myparadise.common.redis.genid.service.impl;

import com.gl.myparadise.common.redis.genid.model.GenId;
import com.gl.myparadise.common.redis.genid.service.GenIdTransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.atomic.AtomicLong;

@Transactional(rollbackFor = Exception.class)
public abstract class GenIdTransactionServiceImpl implements GenIdTransactionService {

	private static final Logger logger = LoggerFactory.getLogger(GenIdTransactionServiceImpl.class);

	/**
	 * 第一次加载
	 */
	@Override
	public boolean initGenIdByDB(String genKey, String subKey) {

		GenIdHelper.getInstance().setGenIdSetp(genKey,subKey, initSept(genKey, subKey));

		logger.info("initGenIdByDB is start genKey = " + genKey + ": subKey = " + subKey);

		GenId vo = getGenIdLock(genKey, subKey);

		if (GenIdHelper.getInstance().containsKey(genKey, subKey)) {

			logger.info("other thread is return" + genKey + ":" + subKey);

			return true;
		}

		Long step = GenIdHelper.getInstance().getGenIdSetp(genKey, subKey);

		logger.info("initGenIdByDB step = " + step );

		Long currNo = 0L;

		Long currNoMax = 0L;

		if (vo != null) {

			currNo = updateGenIdVO(genKey, subKey, vo.getCurrNo());

			logger.info("initGenIdByDB currNo = " + currNo );

			GenIdHelper.getInstance().setGenIDValue(genKey, subKey,
					new AtomicLong(currNo));

		} else {

			insertGenId(genKey, subKey, GenIdHelper.getInstance().getGenIdSetp(genKey, subKey));

			GenIdHelper.getInstance().setGenIDValue(genKey, subKey,
					new AtomicLong(currNo));

			logger.info("getGenIdLock(genKey, subKey) is null");

		}

		currNoMax = currNo	+ GenIdHelper.getInstance().getGenIdSetp(genKey, subKey);

		logger.info("initGenIdByDB currNoMax = " + currNoMax );

		setGenIdCacheMap(genKey, subKey, currNoMax);

		GenIdHelper.getInstance().setLockObject(genKey,
				subKey);

		return true;

	}

	/**
	 * 如果大于最大值就从数据库里取
	 */
	@Override
	public void setGenIdByDB(String genKey, String subKey) {

		logger.info("setGenIdByDB is start genKey = " + genKey + ": subKey = " + subKey);

		GenId vo = getGenIdLock(genKey, subKey);

		//getDataCurrNo 子类告诉返回的CurrNo的值
		Long currNo = updateGenIdVO(genKey, subKey, vo.getCurrNo()) + GenIdHelper.getInstance().getGenIdSetp(genKey, subKey);

		logger.info("setGenIdByDB currNo = " + currNo);

		setGenIdCacheMap(subKey, genKey, currNo);

	}

	/**
	 * 如果数据库不存在就插入
	 * 
	 * @param genKey
	 * @param subKey
	 * @param step
	 * @return
	 */
	protected Long insertGenId(String genKey, String subKey, Long step) {

		GenId genIdVo = new GenId();

		genIdVo.setSubKey(subKey);

		genIdVo.setGenKey(genKey);

		genIdVo.setCurrNo(step);

//		genIdDao.insertGenId(genIdVo);
		insertGenId(genIdVo);

		return step;
	}

	/**
	 * 从数据库里取的时候先锁住这条记录
	 * 
	 * @param genKey
	 * @param subKey
	 * @return
	 */
	protected GenId getGenIdLock(String genKey, String subKey) {

		logger.info("islock genKey = " + genKey + ": subKey = " + subKey);

		GenId genIdVo = new GenId();

		genIdVo.setSubKey(subKey);

		genIdVo.setGenKey(genKey);

//		return genIdDao.getGenIdByPk(genIdVo);

		return getGenIdByLockPk(genIdVo);
	}

	/**
	 * 更新数据的操作
	 * 
	 * @param subKey
	 * @param genKey
	 * @return
	 */
	protected Long updateGenIdVO(String genKey, String subKey, Long dataCurrNo) {

		GenId vo = new GenId();

		logger.info("updateGenIdVO genKey = " + genKey + ": subKey = " + subKey);

		vo.setSubKey(subKey);

		vo.setGenKey(genKey);

		vo.setCurrNo(GenIdHelper.getInstance().getGenIdSetp(genKey, subKey));

//		genIdDao.updateGenIdByPk(vo);
		return updateGenId(vo, dataCurrNo);
	}

	/**
	 * 设置CACHEMAP里的值
	 * 
	 * @param genKey
	 * @param subKey
	 * @param currNo
	 */
	protected void setGenIdCacheMap(String genKey, String subKey, Long currNo) {

		logger.info("setGenIdCacheMap genKey = " + genKey + ": subKey = "
				+ subKey + ": currNo = " + currNo);

		GenIdHelper.getInstance().setMaxID(genKey, subKey, currNo);

	}

	protected abstract void insertGenId(GenId genIdVo);

	/**
	 * select currNo from genId  where genKey = #genKey# and subKey = #subKey#	for update
	 * @param genIdVo
	 * @return
	 */
	protected abstract GenId getGenIdByLockPk(GenId genIdVo);

	/**
	 * update genId  set currNo = currNo + #currNo#  where genKey = #genKey# and subKey = #subKey#
	 * @param genIdVo
	 */
	protected abstract Long updateGenId(GenId genIdVo, Long dataCurrNo);

	protected abstract Long initSept(String genKey, String subKey);

}
