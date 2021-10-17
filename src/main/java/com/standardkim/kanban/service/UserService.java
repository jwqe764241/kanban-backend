package com.standardkim.kanban.service;

import java.util.Optional;

import com.standardkim.kanban.dto.AuthenticationDto.SecurityUser;
import com.standardkim.kanban.dto.UserDto.CreateUserParam;
import com.standardkim.kanban.dto.UserDto.UserDetail;
import com.standardkim.kanban.entity.User;
import com.standardkim.kanban.exception.LoginAlreadyInUseException;
import com.standardkim.kanban.exception.ResourceNotFoundException;
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

	private Authentication getAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}

	private SecurityUser getSecurityUser() {
		Authentication authentication = getAuthentication();
		return (SecurityUser) authentication.getPrincipal();
	}

	@Transactional(readOnly = true)
	public boolean isLoginExists(String login) {
		return userRepository.existsByLogin(login);
	}

	@Transactional(readOnly = true)
	public User findById(Long id) {
		Optional<User> user = userRepository.findById(id);
		return user.orElseThrow(() -> new ResourceNotFoundException("user not found"));
	}

	@Transactional(readOnly = true)
	public User findByLogin(String login) {
		Optional<User> user = userRepository.findByLogin(login);
		return user.orElseThrow(() -> new ResourceNotFoundException("user not found"));
	}

	@Transactional(readOnly = true)
	public User findBySecurityUser() {
		SecurityUser securityUser = getSecurityUser();
		return findById(securityUser.getId());
	}

	@Transactional(readOnly = true)
	public SecurityUser findSecurityUserByLogin(String login) {
		User user = findByLogin(login);
		SecurityUser securityUser = modelMapper.map(user, SecurityUser.class);
		return securityUser;
	}

	@Transactional(rollbackFor = Exception.class)
	public UserDetail create(CreateUserParam createUserParam) {
		if(isLoginExists(createUserParam.getLogin())) {
			throw new LoginAlreadyInUseException("login already in use");
		}
		User user = createUserParam.toEntity(passwordEncoder);
		user = userRepository.save(user);
		return modelMapper.map(user, UserDetail.class);
	}
}
