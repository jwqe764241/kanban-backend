package com.standardkim.kanban.service;

import com.standardkim.kanban.dto.AuthenticationDto.SecurityUser;
import com.standardkim.kanban.dto.UserDto.CreateUserParam;
import com.standardkim.kanban.dto.UserDto.UserDetail;
import com.standardkim.kanban.entity.User;
import com.standardkim.kanban.exception.user.UserNotFoundException;
import com.standardkim.kanban.exception.user.UsernameAlreadyExistsException;
import com.standardkim.kanban.repository.UserRepository;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;

	private final PasswordEncoder passwordEncoder;

	private final ModelMapper modelMapper;

	private SecurityUser getSecurityUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return (SecurityUser) authentication.getPrincipal();
	}

	@Transactional(readOnly = true)
	public boolean isLoginExists(String login) {
		return userRepository.existsByLogin(login);
	}

	@Transactional(readOnly = true)
	public User findById(Long id) {
		return userRepository.findById(id)
			.orElseThrow(() -> new UserNotFoundException("user not found"));
	}

	@Transactional(readOnly = true)
	public User findByLogin(String login) {
		return userRepository.findByLogin(login)
			.orElseThrow(() -> new UserNotFoundException("user not found"));
	}

	@Transactional(readOnly = true)
	public User findBySecurityUser() {
		SecurityUser securityUser = getSecurityUser();
		User user = userRepository.findById(securityUser.getId())
			.orElseThrow(() -> new UserNotFoundException("user not found"));
		return user;
	}

	@Transactional(readOnly = true)
	public SecurityUser findSecurityUserByLogin(String login) {
		User user = userRepository.findByLogin(login)
			.orElseThrow(() -> new UserNotFoundException("user not found"));
		SecurityUser securityUser = modelMapper.map(user, SecurityUser.class);
		return securityUser;
	}

	@Transactional(rollbackFor = Exception.class)
	public UserDetail create(CreateUserParam createUserParam) {
		if(isLoginExists(createUserParam.getLogin())) {
			throw new UsernameAlreadyExistsException("username already exists");
		}
		User user = createUserParam.toEntity(passwordEncoder);
		user = userRepository.save(user);
		return modelMapper.map(user, UserDetail.class);
	}
}
