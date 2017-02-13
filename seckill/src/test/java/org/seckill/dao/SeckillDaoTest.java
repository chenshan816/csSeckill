package test.java.org.seckill.dao;

import java.util.Date;
import java.util.List;

import main.java.org.seckill.dao.SeckillDao;
import main.java.org.seckill.entity.Seckill;

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
public class SeckillDaoTest {
	//ע��Daoʵ��������
	@Resource
	private SeckillDao seckillDao;
	
	@Test
	public void testReduceNumber(){
		int updateCount=seckillDao.reduceNumber(1000L,new Date());
		System.out.println(updateCount);
	}
	
	@Test
	public void testQueryById(){
		long id = 1000;
		Seckill seckill = seckillDao.queryById(id);
		System.out.println(seckill.getName());
		System.out.println(seckill);
	}
	
	@Test
	public void testQueryAll(){
		List<Seckill> seckills = seckillDao.queryAll(0, 100);
		for(Seckill seckill : seckills){
			System.out.println(seckill);
		}
	}
}
