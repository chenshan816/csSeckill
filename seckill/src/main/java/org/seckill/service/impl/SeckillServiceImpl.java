package main.java.org.seckill.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import main.java.org.seckill.dao.SeckillDao;
import main.java.org.seckill.dao.SuccessKilledDao;
import main.java.org.seckill.dto.Exposer;
import main.java.org.seckill.dto.SeckillExecution;
import main.java.org.seckill.dto.cache.RedisDao;
import main.java.org.seckill.entity.Seckill;
import main.java.org.seckill.entity.SuccessKilled;
import main.java.org.seckill.enums.SeckillStateEnum;
import main.java.org.seckill.exception.RepeatKillException;
import main.java.org.seckill.exception.SeckillCloseException;
import main.java.org.seckill.exception.ServiceException;
import main.java.org.seckill.service.SeckillService;

@Service("seckillService")
public class SeckillServiceImpl implements SeckillService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private SeckillDao seckillDao;
	@Autowired
	private SuccessKilledDao successKilledDao;
	// md5�ַ����Ļ���,��ֵ
	private final String slat = "djhfkhslanjdksha4^%#sdfnh321$#$T";
	@Autowired
	private RedisDao redisDao;

	public List<Seckill> getSeckillList() {
		return seckillDao.queryAll(0, 4);
	}

	public Seckill getById(long seckillId) {

		return seckillDao.queryById(seckillId);
	}

	public Exposer exportSeckillUrl(long seckillId) {
		// ��ɱ�Ż�---���ݿ����
		Seckill seckill = redisDao.getSeckill(seckillId);
		if (seckill == null) {
			seckill = seckillDao.queryById(seckillId);
			if (seckill != null) {
				String result = redisDao.putSeckill(seckill);
				seckill = redisDao.getSeckill(seckillId);
			} else {
				return new Exposer(false, seckillId);
			}
		}
		if (seckill == null) {
			return new Exposer(false, seckillId);
		}
		Date startTime = seckill.getStartTime();
		Date endTime = seckill.getEndTime();
		// ϵͳʱ��
		Date curTime = new Date();
		if (curTime.getTime() < startTime.getTime()
				|| curTime.getTime() > endTime.getTime()) {
			// ˵��û��ʼ�����Ѿ�������
			return new Exposer(false, seckillId, curTime.getTime(),
					startTime.getTime(), endTime.getTime());
		}
		// ת���ض��ַ����Ĺ��̣�������
		String md5 = getMD5(seckillId);
		return new Exposer(true, md5, seckillId);
	}

	private String getMD5(long seckillId) {
		String base = seckillId + "/" + slat;
		return DigestUtils.md5DigestAsHex(base.getBytes());
	}

	@Transactional
	public SeckillExecution executeSeckill(long seckillId, long userPhone,
			String md5) throws SeckillCloseException, RepeatKillException,
			ServiceException {
		if (md5 == null || !md5.equals(getMD5(seckillId))) {
			throw new ServiceException("seckill data rewrite");
		}
		// ִ����ɱҵ���߼� �����+��¼������Ϊ
		Date curTime = new Date();
		try {
			// ��¼������Ϊ
			int insertCount = successKilledDao.insertSuccessKilled(seckillId,
					userPhone);
			if (insertCount <= 0) {
				// ˵�������ͻ���ѹ���
				throw new RepeatKillException("seckill repeated");
			} else {
				// ����棬�ȵ���Ʒ����
				int updateCount = seckillDao.reduceNumber(seckillId, curTime);
				if (updateCount <= 0) {
					// û�и��¼�¼,��ɱ����=�����û�л�����ɱ����
					throw new SeckillCloseException("seckill is closed");
				} else {
					// ��ɱ�ɹ�
					SuccessKilled successKilled = successKilledDao
							.queryByIdWithSeckill(seckillId, userPhone);
					return new SeckillExecution(seckillId,
							SeckillStateEnum.SUCCESS, successKilled);
				}
			}

		} catch (SeckillCloseException e) {
			throw e;
		} catch (RepeatKillException e) {
			throw e;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			// �������쳣
			throw new ServiceException("seckill inner error:" + e.getMessage());
		}
	}

	public SeckillExecution executeSeckillProcedire(long seckillId,
			long userPhone, String md5) {
		if (md5 == null || !md5.equals(getMD5(seckillId))) {
			return new SeckillExecution(seckillId,
					SeckillStateEnum.DATA_REWRITE);
		}
		Date killTime = new Date();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("seckillId", seckillId);
		map.put("phone", userPhone);
		map.put("killTime", new Date());
		map.put("result", null);
		try {
			seckillDao.killByProcedure(map);
			// ��ȡresult
			Integer result = MapUtils.getInteger(map, "result", -2);
			if (result == 1) {
				SuccessKilled successKilled = successKilledDao
						.queryByIdWithSeckill(seckillId, userPhone);
				return new SeckillExecution(seckillId,
						SeckillStateEnum.SUCCESS, successKilled);
			} else {
				return new SeckillExecution(seckillId,
						SeckillStateEnum.stateOf(result));
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new SeckillExecution(seckillId, SeckillStateEnum.INNER_ERROR);
		}
	}

}
