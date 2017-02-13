package main.java.org.seckill.dao;

import main.java.org.seckill.entity.Seckill;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface SeckillDao {
	/**
	 * ���ٿ��
	 * @param seckillId ��Ʒ���
	 * @param killTime  ��ɱʱ��
	 * @return ���Ӱ������>1����ʾ���¼�¼������
	 */
	int reduceNumber(@Param("seckillId") long seckillId,@Param("killTime") Date killTime);
	/**
	 * ����id��ȡ��ɱ��Ʒ
	 * @param seckillId ��Ʒ���
	 * @return
	 */
	Seckill queryById(long seckillId);
	/**
	 * ����ƫ������ѯ��ɱ��Ʒ�б�
	 * @param offset
	 * @param limit
	 * @return
	 */
	List<Seckill> queryAll(@Param("offset") int offset,@Param("limit") int limit);
	/**
	 * ͨ���洢���̽�����ɱ
	 * @param paramMap
	 */
	void killByProcedure(Map<String,Object> paramMap);
}
