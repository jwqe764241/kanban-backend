package com.standardkim.kanban.service;

import com.standardkim.kanban.dto.UserDto;
import com.standardkim.kanban.dto.AuthenticationDto.SecurityUser;
import com.standardkim.kanban.entity.User;
import com.standardkim.kanban.repository.UserRepository;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
	private final UserRepository userRepository;

	private final PasswordEncoder passwordEncoder;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByLogin(username);
		
		if (user == null)
			throw new UsernameNotFoundException("cannot find user");

		SecurityUser securityUser = SecurityUser.builder()
			.id(user.getId())
			.login(user.getLogin())
			.password(user.getPassword())
			.name(user.getName())
			.registerDate(user.getRegisterDate())
			.build();

		return securityUser;
	}

	public void join(UserDto.JoinUserRequest joinUserRequest) {
		User joinUser = joinUserRequest.toEntity(passwordEncoder);
		userRepository.save(joinUser);
	}

	public void update() {
	}

	public void remove() {
	}
}
