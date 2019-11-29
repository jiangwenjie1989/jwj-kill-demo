package com.jwj.im.config;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
/**
 * 线程池的配置
 * @author jiangwenjie
 *
 */
@EnableAsync
@Configuration
public class TaskPoolConfig {
	
	@Bean
	public Executor taskExecutor(){
//		ThreadPoolTaskScheduler executor1 =new ThreadPoolTaskScheduler();
//		executor.setPoolSize(20);
//		executor.setThreadNamePrefix("taskExecutor-");
//		executor.setWaitForTasksToCompleteOnShutdown(true);//优雅关闭
//		executor.setAwaitTerminationSeconds(60);
//		return executor;
		
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();  
        executor.setCorePoolSize(5);  //线程池维护线程的最少数量
        executor.setMaxPoolSize(20);  //线程池维护线程的最大数量
        executor.setKeepAliveSeconds(3000);//线程池维护线程所允许的空闲时间
        executor.setQueueCapacity(100);  //线程池所使用的缓冲队列 
        executor.setThreadNamePrefix("taskExecutor-"); 
        executor.setWaitForTasksToCompleteOnShutdown(true);//优雅关闭
        executor.initialize();  
        return executor;  
	}
}
