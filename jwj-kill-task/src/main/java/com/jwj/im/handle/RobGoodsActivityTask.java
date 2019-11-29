package com.jwj.im.handle;

import com.jwj.im.until.DateUntil;
import com.jwj.im.service.KillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;


@Component
@Configuration
@EnableScheduling
public class RobGoodsActivityTask {

	private static final Logger logger = LoggerFactory.getLogger(RobGoodsActivityTask.class);

	@Autowired
	private KillService killService;

	
	/**
	 * 处理秒杀活动定时任务
	 */
	@Scheduled(cron = "0/3 * * * * ?")
	public void handleRobGoodsToQueueTask() {
		Timestamp startT = DateUntil.getNowTimestamp();
		logger.info("处理秒杀活动定时任务开始-------start："+DateUntil.getNowTime(DateUntil.FORMAT_YYYY_MM_DD_HH_MM_SS));
		killService.handleRobGoodsToQueueTask();
		logger.info("处理秒杀活动定时任务结束时间："+DateUntil.getNowTime(DateUntil.FORMAT_YYYY_MM_DD_HH_MM_SS));
		logger.info("处理秒杀活动定时任务所需时间："+(DateUntil.getNowTimestamp().getTime()-startT.getTime())+"毫秒");
	}
	
	
	
}
