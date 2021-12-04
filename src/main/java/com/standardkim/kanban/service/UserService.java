package com.standardkim.kanban.service;

import java.util.List;

import com.standardkim.kanban.dto.AuthenticationDto.SecurityUser;
import com.standardkim.kanban.dto.UserDto.CreateUserParam;
import com.standardkim.kanban.entity.User;
import com.standardkim.kanban.exception.user.UserNotFoundException;
import com.standardkim.kanban.exception.user.DuplicateUserNameException;
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

	public SecurityUser getSecurityUser() {
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

	@Transactional(readOnly = true)
	public List<User> findNotMemberOrNotInvitedUser(Long projectId, String query) {
		List<User> users = userRepository.findNotMemberOrNotInvited(projectId, query);
		return users;
	}

	@Transactional(rollbackFor = Exception.class)
	public User create(CreateUserParam createUserParam) {
		if(isLoginExists(createUserParam.getLogin())) {
			throw new DuplicateUserNameException("duplicate user name");
		}
		User user = User.from(createUserParam, passwordEncoder);
		user = userRepository.save(user);
		return user;
	}
}
