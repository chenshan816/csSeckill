package test.java.org.seckill.dao;

import java.util.Date;
import java.util.List;

import main.java.org.seckill.dao.SeckillDao;
import main.java.org.seckill.dto.Exposer;
import main.java.org.seckill.dto.SeckillExecution;
import main.java.org.seckill.entity.Seckill;
import main.java.org.seckill.exception.RepeatKillException;
import main.java.org.seckill.exception.SeckillCloseException;
import main.java.org.seckill.exception.ServiceException;
import main.java.org.seckill.service.SeckillService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * 配置spring和junit整合，启动时加载springIOC容器
 * 
 * @author dell
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:spring/spring-dao.xml",
		"classpath:spring/spring-service.xml" })
public class SeckillServiceTest {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Resource
	private SeckillService seckillService;
	
	@Test
	public void testGetSeckillList() {
		List<Seckill> seckillList = seckillService.getSeckillList();
		logger.info("list={}",seckillList);
	}

	@Test
	public void testGetById() {
		Seckill seckill = seckillService.getById(1000L);
		logger.info("seckill={}",seckill);
	}

	@Test
	public void testExecuteSeckill() {
		long id =1000;
		Exposer export = seckillService.exportSeckillUrl(id);
		if(export.isExposed()){
			logger.info("export={}",export);
			String md5 =export.getMd5();
			long userPhone = 12345678888L;
			try {
				SeckillExecution executeSeckill = seckillService.executeSeckill(id, userPhone, md5);
				logger.info("executeSeckill={}",executeSeckill);
			} catch (SeckillCloseException e) {
				logger.error(e.getMessage());
			}catch (RepeatKillException e) {
				logger.error(e.getMessage());
			}catch (ServiceException e) {
				logger.error(e.getMessage());
			}
		}else{
			logger.warn("秒杀未开启");
		}
	}
	
	@Test
	public void testExecuteSeckillByProcedure() {
		long id =1000;
		Exposer export = seckillService.exportSeckillUrl(id);
		if(export.isExposed()){
			logger.info("export={}",export);
			String md5 =export.getMd5();
			long userPhone = 12345678888L;
			SeckillExecution executeSeckill = seckillService.executeSeckillProcedire(id, userPhone, md5);
			logger.info(executeSeckill.getStateInfo());
		}
	}	
}
