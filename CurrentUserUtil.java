package com.ekishancredit.auth.util;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ekishancredit.auth.config.SecurityUtils;
import com.ekishancredit.auth.exception.GenericException;
import com.ekishancredit.common.entity.User;
import com.ekishancredit.common.repository.UserCommonRepository;
import com.ekishancredit.common.repository.mapper.UserAndDetailsMapper;

@Service
public class CurrentUserUtil {

	
	@Autowired
	private UserCommonRepository userCommonRepository;
	
	@Transactional(readOnly = true)
	public Optional<User> getUserWithAuthorities() {
		return SecurityUtils.getCurrentUserLogin().flatMap(userCommonRepository::findByUserName);
	}

	public User getCurrentLoginUser() {
		try {
			Optional<String> userLogin = SecurityUtils.getCurrentUserLogin();
			if (userLogin.isEmpty()) {
				throw new GenericException("User is not login, please login", HttpStatus.BAD_REQUEST);
			}

			String login = userLogin.orElse(null);
			if(login==null) {
				throw new GenericException("User Do not have permission to access this resource", HttpStatus.BAD_REQUEST);
			}
			
			Optional<User> user = userCommonRepository.findByUserName(login);

			return user.orElse(null);

		} catch (Exception e1) {
			e1.printStackTrace();
			throw new GenericException("User Do not have permission to access this resource", HttpStatus.BAD_REQUEST);
		}
	}
	
	public UserAndDetailsMapper getCurrentLoginUserDetails() {
		try {
			Optional<String> userLogin = SecurityUtils.getCurrentUserLogin();
			if (userLogin.isEmpty()) {
				throw new GenericException("User is not login, please login", HttpStatus.BAD_REQUEST);
			}

			String login = userLogin.orElse(null);
			if(login==null) {
				throw new GenericException("User Do not have permission to access this resource", HttpStatus.BAD_REQUEST);
			}
			
			UserAndDetailsMapper user = userCommonRepository.findUserDetailByUserName(login);

			return user;

		} catch (Exception e1) {
			e1.printStackTrace();
			throw new GenericException("User Do not have permission to access this resource", HttpStatus.BAD_REQUEST);
		}
	}
	
}
