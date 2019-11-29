package com.jwj.im.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.jwj.domain.WcUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jwj.im.common.RedisCacheKeys;
import com.jwj.im.dao.WsUserDao;
import com.jwj.im.redis.RedisCacheSupport;
import com.jwj.im.service.WsUserService;



@Service
public class WsUserServiceImpl implements WsUserService{
	
	@Autowired
	private WsUserDao wsUserDao;

	@Autowired
	private RedisCacheSupport<WcUser> redisCacheSupport;
	
	@Override
	public WcUser findByUsernameAndPassword(String username, String password) {
		Optional<WcUser> opt=wsUserDao.findByUsernameAndPassword(username,password);
		if(opt.isPresent()) {
			return opt.get();
		}
		return null;
	}

	@Override
	public WcUser findById(Long userId) {
		return wsUserDao.findById(userId);
	}

	@Override
	public WcUser findByUsername(String username) {
		Optional<WcUser> opt=wsUserDao.findByUsername(username);
		if(opt.isPresent()) {
			return opt.get();
		}
		return null;
	}

	@Override
	public void save(WcUser wcUser) {
		wsUserDao.save(wcUser);
	}

	@Override
	public List<WcUser> getUserListByGroup() {
		return redisCacheSupport.getHashValues(RedisCacheKeys.VW_IM_GROUP_Id);
	}

	@Override
	public List<WcUser> addGroup(WcUser wcUser) {
		redisCacheSupport.putHashCached(RedisCacheKeys.VW_IM_GROUP_Id, String.valueOf(wcUser.getId()), wcUser);
		return getUserListByGroup();
	}
	
	
	

}
