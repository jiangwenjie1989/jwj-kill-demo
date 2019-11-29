package com.jwj.im.config;

import com.alibaba.fastjson.JSON;
import com.jwj.domain.WcUser;
import com.jwj.im.common.Message;
import com.jwj.im.common.RedisCacheKeys;
import com.jwj.im.common.RedisMassgae;
import com.jwj.im.redis.RedisCacheSupport;
import com.jwj.im.redis.RedisStringCacheSupport;
import com.jwj.im.service.WsUserService;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.timeout.IdleStateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.yeauty.annotation.*;
import org.yeauty.pojo.ParameterMap;
import org.yeauty.pojo.Session;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint(prefix = "netty-websocket")
@Component
public class MyWebSocket {

	@Value("${channel.im.name}")
	private String imChannel;

	@Autowired
	private WsUserService wsUserService;

	@Autowired
	private RedisStringCacheSupport redisString;
	
	@Autowired
	private RedisCacheSupport<WcUser> redisCacheSupport;

	public static final Map<String, Session> userSocketSessionMap;

	static {
		userSocketSessionMap = new ConcurrentHashMap<String, Session>();
	}

	@OnOpen
	public void onOpen(Session session, HttpHeaders headers, ParameterMap parameterMap) throws IOException {
		String userId = parameterMap.getParameter("userId");
		session.setAttribute("userId", userId);
		WcUser wcUser = wsUserService.findById(Long.parseLong(userId));
		String username = wcUser.getUsername();
		String num = "";
		if (!userSocketSessionMap.containsKey(userId)) {
			userSocketSessionMap.put(userId, session);
			if(redisString.getCached(RedisCacheKeys.VW_IM_PEOPLE_NUM)==null) {
				redisString.put(RedisCacheKeys.VW_IM_PEOPLE_NUM, "0");
			}
			num = redisString.incr(RedisCacheKeys.VW_IM_PEOPLE_NUM);
		} else {
			num = redisString.getCached(RedisCacheKeys.VW_IM_PEOPLE_NUM);
		}
		RedisMassgae redisMassgae = new RedisMassgae();
		redisMassgae.setChannel(imChannel);
		redisMassgae.setSendType(2);

		Message message = new Message();
		message.setPeopleNum(Integer.parseInt(num));
		redisMassgae.setMessage(message);
		message.setFromUsername(username);
		message.setFromUserId(userId);
		message.setMessageType(1);

		redisString.sendMessage(imChannel, JSON.toJSONString(redisMassgae));
	}

	@OnClose
	public void onClose(Session session) throws IOException {
		String userId = session.getAttribute("userId").toString();
		if(userSocketSessionMap.containsKey(userId)) {
			String num = redisString.decr(RedisCacheKeys.VW_IM_PEOPLE_NUM);
			redisCacheSupport.deleteHashCached(RedisCacheKeys.VW_IM_GROUP_Id, userId);
			
			RedisMassgae redisMassgae = new RedisMassgae();
			redisMassgae.setChannel(imChannel);
			redisMassgae.setSendType(2);
			
			Message message = new Message();
			message.setPeopleNum(Integer.parseInt(num));
			redisMassgae.setMessage(message);
			WcUser wcUser = wsUserService.findById(Long.parseLong(userId));
			String username = wcUser.getUsername();
			message.setFromUsername(username);
			message.setFromUserId(userId);
			message.setMessageType(2);

			redisString.sendMessage(imChannel, JSON.toJSONString(redisMassgae));
			
			userSocketSessionMap.remove(userId);
		}
		
	}

	@OnError
	public void onError(Session session, Throwable throwable) {
		throwable.printStackTrace();
	}

	@OnMessage
	public void onMessage(Session session, String message) {
		Message messageDate = JSON.parseObject(message, Message.class);
		messageDate.setPeopleNum(Integer.parseInt(redisString.getCached(RedisCacheKeys.VW_IM_PEOPLE_NUM)));
		RedisMassgae redisMassgae = new RedisMassgae();
		redisMassgae.setChannel(imChannel);
		redisMassgae.setSendType(messageDate.getSendType());
		redisMassgae.setMessage(messageDate);
		redisString.sendMessage(imChannel, JSON.toJSONString(redisMassgae));
	}

	@OnBinary
	public void onBinary(Session session, byte[] bytes) {
		for (byte b : bytes) {
			System.out.println(b);
		}
		session.sendBinary(bytes);
	}

	@OnEvent
	public void onEvent(Session session, Object evt) {
		if (evt instanceof IdleStateEvent) {
			IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
			switch (idleStateEvent.state()) {
			case READER_IDLE:
				System.out.println("read idle");
				break;
			case WRITER_IDLE:
				System.out.println("write idle");
				break;
			case ALL_IDLE:
				System.out.println("all idle");
				break;
			default:
				break;
			}
		}
	}

	/**
	 * 给指定用户发送消息
	 * 
	 * @param message
	 * @param userId
	 * @throws IOException
	 */
	public void sendMessageToUser(final String message, String userId) {
		Session session = userSocketSessionMap.get(userId);
		if (session != null && session.isOpen()) {
			session.sendText(message);
		}
	}

	/**
	 * 给所有在线用户发送消息
	 * 
	 * @param message
	 * @throws IOException
	 */
	public void broadcast(final String message) {
		Iterator<Map.Entry<String, Session>> it = userSocketSessionMap.entrySet().iterator();
		while (it.hasNext()) {
			final Map.Entry<String, Session> entry = it.next();
			entry.getValue().sendText(message);
		}
	}

	public Map<String, Session> getUserSocketSessionMap() {
		return userSocketSessionMap;
	}

	/**
	 * 发送消息到redis
	 * 
	 * @param redisMassgae
	 */
	public void sendMessage(RedisMassgae redisMassgae) {
		redisMassgae.setChannel(imChannel);
		Message message = redisMassgae.getMessage();
		message.setPeopleNum(Integer.parseInt(redisString.getCached(RedisCacheKeys.VW_IM_PEOPLE_NUM)));
		redisMassgae.setMessage(message);
		redisString.sendMessage(imChannel, JSON.toJSONString(redisMassgae));
	}

}
