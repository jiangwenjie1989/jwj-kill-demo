package com.jwj.im.until.id;

import java.util.UUID;

/**
 * 获取唯一ID 的工具类
 * @author bowen_wang
 *
 */
public class IdUtils {

	private static IdWorker idWorker=new IdWorker(3);

	public static Long getId(){
		return idWorker.nextId();
	}
	
	public static String getUUID(){
		String uuid=UUID.randomUUID().toString();
		return uuid.replace("-", "");
	}
}
