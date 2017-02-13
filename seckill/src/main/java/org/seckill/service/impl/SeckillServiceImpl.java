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
	// md5字符串的混淆,盐值
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
		// 秒杀优化---数据库访问
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
		// 系统时间
		Date curTime = new Date();
		if (curTime.getTime() < startTime.getTime()
				|| curTime.getTime() > endTime.getTime()) {
			// 说明没开始或者已经结束了
			return new Exposer(false, seckillId, curTime.getTime(),
					startTime.getTime(), endTime.getTime());
		}
		// 转化特定字符串的过程，不可逆
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
		// 执行秒杀业务逻辑 减库存+记录购买行为
		Date curTime = new Date();
		try {
			// 记录购买行为
			int insertCount = successKilledDao.insertSuccessKilled(seckillId,
					userPhone);
			if (insertCount <= 0) {
				// 说明插入冲突，已购买
				throw new RepeatKillException("seckill repeated");
			} else {
				// 减库存，热点商品竞争
				int updateCount = seckillDao.reduceNumber(seckillId, curTime);
				if (updateCount <= 0) {
					// 没有更新记录,秒杀结束=》库存没有或者秒杀结束
					throw new SeckillCloseException("seckill is closed");
				} else {
					// 秒杀成功
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
			// 运行期异常
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
			// 获取result
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
