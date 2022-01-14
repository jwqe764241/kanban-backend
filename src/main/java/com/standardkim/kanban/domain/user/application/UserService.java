package com.standardkim.kanban.domain.user.application;

import java.util.List;

import com.standardkim.kanban.domain.user.dao.UserRepository;
import com.standardkim.kanban.domain.user.domain.User;
import com.standardkim.kanban.domain.user.dto.CreateUserParam;
import com.standardkim.kanban.domain.user.exception.DuplicateUserNameException;
import com.standardkim.kanban.domain.user.exception.UserNotFoundException;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;

	private final PasswordEncoder passwordEncoder;

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
	public List<User> findNotMemberOrNotInvitedUser(Long projectId, String query) {
		return userRepository.findNotMemberOrNotInvited(projectId, query);
	}

	@Transactional(rollbackFor = Exception.class)
	public User create(CreateUserParam createUserParam) {
		if(isLoginExists(createUserParam.getLogin())) {
			throw new DuplicateUserNameException("duplicate user name");
		}
		User user = User.from(createUserParam.getLogin(), 
			createUserParam.getPassword(),
			createUserParam.getName(),
			createUserParam.getEmail(),	
			passwordEncoder);
		return userRepository.save(user);
	}
}
