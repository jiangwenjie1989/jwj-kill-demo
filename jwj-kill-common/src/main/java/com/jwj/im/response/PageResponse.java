package com.jwj.im.response;

import java.io.Serializable;

import com.jwj.im.dao.Pagetool;


public class PageResponse<T> extends SimpleResponse implements Serializable {

	private static final long serialVersionUID = 3940773897929380705L;
	
	private Pagetool<T> page;

	public Pagetool<T> getPage() {
		return page;
	}

	public void setPage(Pagetool<T> page) {
		this.page = page;
	}

	
	/**
	 * 设置返回错误消息
	 * @param resp 返回对象
	 * @param code 错误码
	 * @param status 状态
	 * @param message 错误消息
	 * @return
	 */
	public PageResponse<T> setReturnErrMsg(PageResponse<T> resp,Integer code,String status,String message){
		resp.setCode(code);
		resp.setStatus(status);
		resp.setMessage(message);
		return resp;
	}
	
}
