package com.standardkim.kanban.service;

import com.standardkim.kanban.dto.UserDto;
import com.standardkim.kanban.entity.User;
import com.standardkim.kanban.repository.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;

	private final PasswordEncoder passwordEncoder;

	public void join(UserDto.JoinUserRequest joinUserRequest) {
		User joinUser = joinUserRequest.toEntity(passwordEncoder);
		userRepository.save(joinUser);
	}

	public void update() {
	}

	public void remove() {
	}
}
