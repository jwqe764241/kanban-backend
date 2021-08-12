package com.standardkim.kanban.service;

import com.standardkim.kanban.dto.UserDto.NewUserInfo;
import com.standardkim.kanban.dto.UserDto.UserInfo;
import com.standardkim.kanban.entity.User;
import com.standardkim.kanban.exception.LoginAlreadyInUseException;
import com.standardkim.kanban.repository.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;

	private final PasswordEncoder passwordEncoder;

	@Transactional(rollbackFor = Exception.class)
	public UserInfo addUser(NewUserInfo newUserInfo) {
		if(userRepository.existsByLogin(newUserInfo.getLogin())) {
			throw new LoginAlreadyInUseException("login already in use");
		}

		User user = newUserInfo.toEntity(passwordEncoder);
		User result = userRepository.save(user);
		UserInfo addedUserInfo = new UserInfo(result);
		return addedUserInfo;
	}

	public void update() {
	}

	public void remove() {
	}
}
