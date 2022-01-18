package com.standardkim.kanban.service.user;

import com.standardkim.kanban.domain.user.application.UserCreateService;
import com.standardkim.kanban.domain.user.application.UserFindService;
import com.standardkim.kanban.domain.user.dao.UserRepository;
import com.standardkim.kanban.domain.user.domain.User;
import com.standardkim.kanban.domain.user.dto.CreateUserParam;
import com.standardkim.kanban.domain.user.exception.DuplicateUserNameException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserCreateServiceTest {
	@Mock
	private UserFindService userFindService;

	@Mock
	private UserRepository userRepository;

	@Spy
	PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	@InjectMocks
	private UserCreateService userCreateService;

	@Test
	public void create_LoginIsExist_ThrowDuplicateUserNameException() {
		given(userFindService.isLoginExists("example")).willReturn(true);

		assertThatThrownBy(() -> {
			userCreateService.create(getCreateUserParam("example"));
		}).isInstanceOf(DuplicateUserNameException.class);
	}

	@Test
	public void create_LoginIsNotExist_User() {
		given(userFindService.isLoginExists("example")).willReturn(false);
		given(userRepository.save(any(User.class))).willReturn(getUser("example"));

		User user = userCreateService.create(getCreateUserParam("example"));

		assertThat(user).isNotNull();
	}

	private User getUser(String login) {
		return User.builder()
			.login(login)
			.build();
	}

	private CreateUserParam getCreateUserParam(String login) {
		return CreateUserParam.builder()
			.login(login)
			.password("qwer1234")
			.name("example")
			.email("example@example.com")
			.build();
	}
}
