package test.java.org.seckill.dao;

import java.util.Date;
import java.util.List;

import main.java.org.seckill.dao.SuccessKilledDao;
import main.java.org.seckill.entity.SuccessKilled;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * ����spring��junit���ϣ�����ʱ����springIOC����
 * @author dell
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/spring-dao.xml")
public class SuccessKilledTest {
	
	//ע��Daoʵ��������
	@Resource
	private SuccessKilledDao successKilledDao;
	
	@Test
	public void testInsertSuccessKilled(){
		int insertCount = successKilledDao.insertSuccessKilled(1000L, 12345678911L);
		System.out.println(insertCount);
	}
	
	
	@Test
	public void testQueryByIdWithSeckill(){
		SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(1000L,12345678911L);
		System.out.println(successKilled);
		System.out.println(successKilled.getSeckill().getName());
	}
}
