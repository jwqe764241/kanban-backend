package com.standardkim.kanban.service;

import java.sql.SQLException;

import com.standardkim.kanban.dto.UserDto;
import com.standardkim.kanban.entity.User;
import com.standardkim.kanban.exception.LoginAlreadyInUse;
import com.standardkim.kanban.repository.UserRepository;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.jpa.JpaSystemException;
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
	public void join(UserDto.JoinUserRequest joinUserRequest) {
		if(userRepository.findByLogin(joinUserRequest.getLogin()).isPresent()) {
			throw new LoginAlreadyInUse("login already in use");
		}

		User joinUser = joinUserRequest.toEntity(passwordEncoder);
		userRepository.save(joinUser);
	}

	public void update() {
	}

	public void remove() {
	}
}
