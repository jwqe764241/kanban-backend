package com.standardkim.kanban.service;

import com.standardkim.kanban.dto.UserDto;
import com.standardkim.kanban.dto.UserDto.UserInfo;
import com.standardkim.kanban.entity.User;
import com.standardkim.kanban.exception.LoginAlreadyInUse;
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
	public UserInfo addUser(UserDto.JoinUserRequest joinUserRequest) {
		if(userRepository.findByLogin(joinUserRequest.getLogin()).isPresent()) {
			throw new LoginAlreadyInUse("login already in use");
		}

		User joinUser = joinUserRequest.toEntity(passwordEncoder);
		
		UserInfo newUserInfo = new UserInfo(userRepository.save(joinUser));
		return newUserInfo;
	}

	public void update() {
	}

	public void remove() {
	}
}
