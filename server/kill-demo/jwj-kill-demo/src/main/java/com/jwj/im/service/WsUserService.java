package com.jwj.im.service;

import com.jwj.domain.WcUser;

import java.util.List;


public interface WsUserService {

	WcUser findByUsernameAndPassword(String username, String password);

	WcUser findById(Long userId);

	WcUser findByUsername(String username);

	void save(WcUser wcUser);

	List<WcUser> getUserListByGroup();

	List<WcUser> addGroup(WcUser wcUser);

}
