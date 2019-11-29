package com.jwj.im.config.exception;


public class BusinessException extends RuntimeException {
	 /**  
	 * @Fields field:field:  
	 */  
	private static final long serialVersionUID = 7807721275707654655L;
	
	private Integer code;  //错误码 
	private String msg;  //错误消息
	  
    public BusinessException() {}  
      
    public BusinessException(Integer code, String msg) { 
    	super(msg);  
        this.code = code;  
        this.msg = msg;  
    }  
      
    public Integer getCode() {  
        return code;  
    }  
  
    public void setCode(Integer code) {  
        this.code = code;  
    }

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}  
    
    
}
