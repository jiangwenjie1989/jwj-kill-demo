package com.jwj.im.dao;

import java.util.Optional;

import com.jwj.domain.WcUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.jwj.im.repository.WcUserRepository;

@Repository
public class WsUserDao {
	
	
	@Autowired
	private WcUserRepository wcUserRepository;

	public Optional<WcUser> findByUsernameAndPassword(String username, String password) {
		return wcUserRepository.findByUsernameAndPassword(username,password);
	}

	public WcUser findById(Long userId) {
		return wcUserRepository.findOne(userId);
	}

	public Optional<WcUser> findByUsername(String username) {
		return wcUserRepository.findByUsername(username);
	}

	public void save(WcUser wcUser) {
		wcUserRepository.save(wcUser);
	}
	
	
	

}
