package main.java.org.seckill.dao;

import main.java.org.seckill.entity.Seckill;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface SeckillDao {
	/**
	 * 减少库存
	 * @param seckillId 商品编号
	 * @param killTime  秒杀时间
	 * @return 如果影响行数>1，表示更新记录的行数
	 */
	int reduceNumber(@Param("seckillId") long seckillId,@Param("killTime") Date killTime);
	/**
	 * 根据id获取秒杀商品
	 * @param seckillId 商品编号
	 * @return
	 */
	Seckill queryById(long seckillId);
	/**
	 * 根据偏移量查询秒杀商品列表
	 * @param offset
	 * @param limit
	 * @return
	 */
	List<Seckill> queryAll(@Param("offset") int offset,@Param("limit") int limit);
	/**
	 * 通过存储过程进行秒杀
	 * @param paramMap
	 */
	void killByProcedure(Map<String,Object> paramMap);
}
