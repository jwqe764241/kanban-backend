package com.standardkim.kanban.domain.user.application;

import com.standardkim.kanban.domain.user.dao.UserRepository;
import com.standardkim.kanban.domain.user.domain.User;
import com.standardkim.kanban.domain.user.dto.CreateUserParam;
import com.standardkim.kanban.domain.user.exception.DuplicateUserNameException;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserCreateService {
	private final UserFindService userFindService;

	private final UserRepository userRepository;

	private final PasswordEncoder passwordEncoder;

	@Transactional(rollbackFor = Exception.class)
	public User create(CreateUserParam createUserParam) {
		if(userFindService.isLoginExists(createUserParam.getLogin())) {
			throw new DuplicateUserNameException("duplicate user name");
		}
		User user = User.of(createUserParam.getLogin(), 
			createUserParam.getPassword(),
			createUserParam.getName(),
			createUserParam.getEmail(),	
			passwordEncoder);
		return userRepository.save(user);
	}
}
