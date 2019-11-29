package com.jwj.im.config.exception;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jwj.im.response.SimpleResponse;
import com.jwj.im.syse.HttpCodeE;
import com.jwj.im.syse.SysRespStatusE;


/**
 * 全局异常处理类
 * @author 61959
 *
 */
@ControllerAdvice(basePackages = {"com.virtual.wallet.ptop.controller"})
@ResponseBody
public class GlobalExceptionHandler {

	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	/**
	 * 默认异常处理
	 * @param
	 * @return
	 */
	@ExceptionHandler(value = Exception.class)
	public SimpleResponse defaultHandlerExceptionResolverHandler(Exception e) {
		SimpleResponse simpleResponse = new SimpleResponse();
		if(e instanceof BusinessException) {  
			BusinessException businessException = (BusinessException)e;  
			if ( businessException != null ) {
				simpleResponse.setStatus(SysRespStatusE.失败.getDesc());
				simpleResponse.setCode(businessException.getCode());
				simpleResponse.setMessage(businessException.getMsg());
				logger.error("业务异常："+"错误码："+businessException.getCode() + "，错误消息：" + businessException.getMsg());
				logger.error("业务异常：", e);
			}
			return simpleResponse;  
		}
		simpleResponse.setStatus(SysRespStatusE.失败.getDesc());
		simpleResponse.setCode(HttpCodeE.内部错误.value);
		simpleResponse.setMessage("网络异常请求稍后再试!");
		logger.error("全局异常：", e);
		return simpleResponse;
	}

}
