package com.standardkim.kanban.unit;

import java.time.LocalDateTime;
import java.util.Optional;

import com.standardkim.kanban.dto.UserDto.JoinUserRequest;
import com.standardkim.kanban.dto.UserDto.UserInfo;
import com.standardkim.kanban.entity.User;
import com.standardkim.kanban.exception.LoginAlreadyInUseException;
import com.standardkim.kanban.repository.UserRepository;
import com.standardkim.kanban.service.UserService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
	@Mock
	private UserRepository userRepository;

	@Spy
	private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	@InjectMocks
	private UserService userService;

	@DisplayName("When add user successfully, return added user's info")
	@Test
	public void when_AddUserSuccessfully_expect_ReturnAddedUserInfo() {
		//given
		String login = "test";
		String password = "1234";
		JoinUserRequest request = new JoinUserRequest(login, password, "test");
		final String encryptedPassword = passwordEncoder.encode(password);
		final User fakeUser = new User(1L, login, encryptedPassword, "test", LocalDateTime.now());

		given(userRepository.findByLogin(login)).willReturn(Optional.empty());
		given(userRepository.findById(1L)).willReturn(Optional.of(fakeUser));
		given(userRepository.save(any(User.class))).willReturn(fakeUser);

		//when
		UserInfo addedUserInfo = userService.addUser(request);

		//then
		User addedUser = userRepository.findById(addedUserInfo.getId()).get();

		assertEquals(fakeUser.getId(), addedUser.getId());
		assertEquals(fakeUser.getLogin(), addedUser.getLogin());
		assertEquals(true, passwordEncoder.matches(password, addedUser.getPassword()));
		assertEquals(fakeUser.getName(), addedUser.getName());
		assertEquals(fakeUser.getRegisterDate(), addedUser.getRegisterDate());
	}

	@DisplayName("When login already in use, exception thrown.")
	@Test
	public void when_LoginAlreadyInUse_expect_ExceptionThrown() {
		//given
		JoinUserRequest request = new JoinUserRequest("aaa", "1234", "test");
		final User fakeUser = new User(1L, "aaa", "1234", "test", LocalDateTime.now());

		given(userRepository.findByLogin("aaa")).willReturn(Optional.of(fakeUser));

		//when, then
		assertThrows(LoginAlreadyInUseException.class, () -> {
			userService.addUser(request);
		});
	}
}
