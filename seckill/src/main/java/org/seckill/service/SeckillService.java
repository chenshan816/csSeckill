package main.java.org.seckill.service;

import java.util.List;

import main.java.org.seckill.dto.Exposer;
import main.java.org.seckill.dto.SeckillExecution;
import main.java.org.seckill.entity.Seckill;
import main.java.org.seckill.exception.RepeatKillException;
import main.java.org.seckill.exception.SeckillCloseException;
import main.java.org.seckill.exception.ServiceException;


/**
 * 业务接口：站在"使用者"角度设计接口
 * @author dell
 *
 */
public interface SeckillService {
	/**
	 * 查询所有秒杀接口
	 * @return
	 */
	List<Seckill> getSeckillList();
	/**
	 * 查询单个秒杀记录
	 * @param seckillId
	 * @return
	 */
	Seckill getById(long seckillId);
	/**
	 * 秒杀开启时：输出秒杀接口的地址
	 * 否则输出系统时间和秒杀时间
	 * dto：与数据库实体无关，是在业务逻辑层需要的实体
	 * @param seckillId
	 */
	Exposer exportSeckillUrl(long seckillId);
	/**
	 * 执行秒杀操作，如果md5值改变则拒绝秒杀
	 * @param seckillId
	 * @param userPhone
	 * @param md5
	 */
	SeckillExecution executeSeckill(long seckillId,long userPhone,String md5) throws SeckillCloseException,RepeatKillException,ServiceException;
	/**
	 * 调用存储过程完成秒杀，提高热点商品的qps
	 * @param seckillId
	 * @param userPhone
	 * @param md5
	 * @return
	 * @throws SeckillCloseException
	 * @throws RepeatKillException
	 * @throws ServiceException
	 */
	SeckillExecution executeSeckillProcedire(long seckillId,long userPhone,String md5);
}
