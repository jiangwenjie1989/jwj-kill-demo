package com.jwj.im.response;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;

public class ApiResponse<T> extends SimpleResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value="返回单个对象数据")
	private T data;
	
	
	@ApiModelProperty(value="返回集合数据")
	private List<T> list;
	
	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

	/**
	 * 设置返回错误消息
	 * @param resp 返回对象
	 * @param code 错误码
	 * @param status 状态
	 * @param message 错误消息
	 * @return
	 */
	public ApiResponse<T> setReturnErrMsg(ApiResponse<T> resp,Integer code,String status,String message){
		resp.setCode(code);
		resp.setStatus(status);
		resp.setMessage(message);
		return resp;
	}
	
}
