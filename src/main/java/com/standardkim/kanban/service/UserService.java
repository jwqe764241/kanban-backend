package com.standardkim.kanban.service;

import java.util.Optional;

import com.standardkim.kanban.dto.AuthenticationDto.SecurityUser;
import com.standardkim.kanban.dto.UserDto.NewUserInfo;
import com.standardkim.kanban.dto.UserDto.UserInfo;
import com.standardkim.kanban.entity.User;
import com.standardkim.kanban.exception.LoginAlreadyInUseException;
import com.standardkim.kanban.exception.UserNotFoundException;
import com.standardkim.kanban.repository.UserRepository;
import com.standardkim.kanban.util.AuthenticationFacade;

import org.modelmapper.ModelMapper;
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

	private final AuthenticationFacade authenticationFacade;

	@Transactional(readOnly = true)
	public boolean isLoginExists(String login) {
		return userRepository.existsByLogin(login);
	}

	@Transactional(readOnly = true)
	public User getUserById(Long id) {
		Optional<User> user = userRepository.findById(id);
		return user.orElseThrow(() -> new UserNotFoundException("user not found"));
	}

	@Transactional(readOnly = true)
	public User getUserByLogin(String login) {
		Optional<User> user = userRepository.findByLogin(login);
		return user.orElseThrow(() -> new UserNotFoundException("user not found"));
	}

	@Transactional(readOnly = true)
	public User getAuthenticatedUser() {
		SecurityUser securityUser = authenticationFacade.getSecurityUser();
		return getUserById(securityUser.getId());
	}

	@Transactional(rollbackFor = Exception.class)
	public UserInfo addUser(NewUserInfo newUserInfo) {
		if(isLoginExists(newUserInfo.getLogin())) {
			throw new LoginAlreadyInUseException("login already in use");
		}
		User newUser = newUserInfo.toEntity(passwordEncoder);
		newUser = userRepository.save(newUser);
		return modelMapper.map(newUser, UserInfo.class);
	}

	public void update() {
	}

	public void remove() {
	}
}
