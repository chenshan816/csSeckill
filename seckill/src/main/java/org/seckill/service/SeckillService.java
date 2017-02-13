package main.java.org.seckill.service;

import java.util.List;

import main.java.org.seckill.dto.Exposer;
import main.java.org.seckill.dto.SeckillExecution;
import main.java.org.seckill.entity.Seckill;
import main.java.org.seckill.exception.RepeatKillException;
import main.java.org.seckill.exception.SeckillCloseException;
import main.java.org.seckill.exception.ServiceException;


/**
 * ҵ��ӿڣ�վ��"ʹ����"�Ƕ���ƽӿ�
 * @author dell
 *
 */
public interface SeckillService {
	/**
	 * ��ѯ������ɱ�ӿ�
	 * @return
	 */
	List<Seckill> getSeckillList();
	/**
	 * ��ѯ������ɱ��¼
	 * @param seckillId
	 * @return
	 */
	Seckill getById(long seckillId);
	/**
	 * ��ɱ����ʱ�������ɱ�ӿڵĵ�ַ
	 * �������ϵͳʱ�����ɱʱ��
	 * dto�������ݿ�ʵ���޹أ�����ҵ���߼�����Ҫ��ʵ��
	 * @param seckillId
	 */
	Exposer exportSeckillUrl(long seckillId);
	/**
	 * ִ����ɱ���������md5ֵ�ı���ܾ���ɱ
	 * @param seckillId
	 * @param userPhone
	 * @param md5
	 */
	SeckillExecution executeSeckill(long seckillId,long userPhone,String md5) throws SeckillCloseException,RepeatKillException,ServiceException;
	/**
	 * ���ô洢���������ɱ������ȵ���Ʒ��qps
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
