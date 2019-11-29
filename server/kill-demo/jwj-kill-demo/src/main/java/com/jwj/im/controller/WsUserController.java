package com.jwj.im.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import com.jwj.domain.WcUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.jwj.im.config.annotation.CacheLock;
import com.jwj.im.config.annotation.CacheParam;
import com.jwj.im.service.WsUserService;
import com.jwj.im.until.ValidationUtils;


@Controller
public class WsUserController {

	@Autowired
	private WsUserService wsUserService;
	
	


	@RequestMapping(value = "/login.do")
	public String login() throws Exception {
		return "login";
	}
	
	@RequestMapping(value = "/toRegist.do")
	public String toRegist() throws Exception {
		return "regist";
	}
	
	
	@CacheLock(prefix = "app:im:user:register") //防止重复提交
	@RequestMapping(value = "/regist.do")
	public String regist(
			@CacheParam(name = "username")
			@RequestParam("username") String username, 
			
			@CacheParam(name = "password")
			@RequestParam("password") String password,
			
			HttpSession session
			) throws Exception {
		if (ValidationUtils.isStrsNull(username, password)) {
			return "regist";
		}
		WcUser wcUserDB = wsUserService.findByUsername(username);
		if(wcUserDB==null) {
			WcUser wcUser = new WcUser();
			wcUser.setPassword(password);
			wcUser.setUsername(username);
			wsUserService.save(wcUser);
		}else {
			session.setAttribute("error", "该用户已经存在请求重新注册！");
			return "fail";
		}
		return "login";
	}

	@RequestMapping("/loginValidate")
	public String loginValidate(@RequestParam("username") String username, @RequestParam("password") String password,
			HttpSession session) {
		if (ValidationUtils.isStrsNull(username, password)) {
			return "login";
		}
		WcUser wcUser = wsUserService.findByUsernameAndPassword(username, password);
		if (wcUser != null) {
			session.setAttribute("userId", wcUser.getId());
			session.setAttribute("username", username);
			//将用户添加到组群里面
			List<WcUser> userList=wsUserService.addGroup(wcUser);
			session.setAttribute("userList", userList);
			return "chatroom";
		} else {
			session.setAttribute("error", "您输入的用户名或密码有误！");
			return "fail";
		}
	}
	
	@RequestMapping(value = "/logout.do")
	public String logout(@RequestParam("userId") Long userId,HttpSession session) throws Exception {
		session.removeAttribute(String.valueOf(userId));
		return "login";
	}

}
