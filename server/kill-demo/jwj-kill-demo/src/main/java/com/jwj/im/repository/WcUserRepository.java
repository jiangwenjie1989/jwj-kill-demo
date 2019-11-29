package com.jwj.im.repository;

import com.jwj.domain.WcUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;




public interface WcUserRepository extends JpaRepository<WcUser, Long> {

	Optional<WcUser> findByUsernameAndPassword(String username, String password);

	Optional<WcUser> findByUsername(String username);

	
}
