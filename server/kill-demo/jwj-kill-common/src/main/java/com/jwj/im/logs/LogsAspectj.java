package com.jwj.im.logs;

import java.lang.reflect.Method;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 日志打印类
 * @author Administrator
 *
 */
@Configuration
@Aspect
public class LogsAspectj {

	private static final Logger logger = LoggerFactory.getLogger(LogsAspectj.class);

	@Pointcut("execution(* com.jwj.im..*.*Controller.*(..))")
	public void logsAspect() {
	};

	/**
	 * 前置通知
	 * @param joinPoint  切点
	 */
	@Before("logsAspect()")
	public void before(JoinPoint joinPoint) {
		Signature st = joinPoint.getSignature();// 切面点得到签名
		Object classObj = joinPoint.getTarget().getClass(); //获取目标对象对应的类名
		Object[] args = joinPoint.getArgs(); // 得到参数列表
		
		StringBuffer reStr = new StringBuffer();
		
		if (st instanceof MethodSignature) { // 判断是否是方法签名
			MethodSignature mst = (MethodSignature) st;
			Method method = mst.getMethod(); // 得到方法名
			reStr.append("类："+classObj.toString()+",方法：" + method.getName());
			StringBuffer paramStr = new StringBuffer();
			if (args != null && args.length > 0) {
				paramStr.append("参数列表：");
				HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();//获取request
				for (int i = 0; i < args.length; i++) {
					paramStr.append("【"+args[i]+"】");
				}
			}
			logger.info(reStr.toString()+"=="+paramStr.toString());
		}
		
	}

	/*@After("logsAspect()")
	public void after(JoinPoint joinPoint) {

	}*/
	

/*	@AfterReturning(pointcut = "logsAspect()", returning = "returnVal")
	public void afterReturning(JoinPoint joinPoint, Object returnVal) {
		// 方法调用之后，打印返回值
//		logger.info("返回值为： "+ returnVal.toString());
		
	}*/

	/**
	 * 环绕通知
	 * 
	 * @param point
	 * @return
	 */
	/*@Around("logsAspect()")
	public Object around(ProceedingJoinPoint point) {
		Object result=null;
		try {      
	        result=point.proceed();
		} catch (Throwable e) {
			e.printStackTrace();
		} 
		return result;
	}*/

}
