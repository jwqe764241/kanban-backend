package com.standardkim.kanban.service;

import com.standardkim.kanban.dto.AuthenticationDto.SecurityUser;
import com.standardkim.kanban.dto.UserDto.CreateUserParam;
import com.standardkim.kanban.entity.User;
import com.standardkim.kanban.exception.user.UserNotFoundException;
import com.standardkim.kanban.exception.user.DuplicateUserNameException;
import com.standardkim.kanban.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
	User testUser;
	SecurityUser testSecurityUser;

	@Mock
	UserRepository userRepository;

	@Spy
	PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	@Spy
	@InjectMocks
	UserService userService;

	@BeforeEach
	public void init() {
		testUser = getUser();
		testSecurityUser = getSecurityUser();
	}

	@Test
	public void isLoginExists_UserIsExist_true() {
		given(userRepository.existsByLogin(anyString())).willReturn(true);

		boolean isExist = userService.isLoginExists("");
		
		assertThat(isExist).isTrue();
	}

	@Test
	public void isLoginExists_UserIsNotExist_False() {
		given(userRepository.existsByLogin(anyString())).willReturn(false);

		boolean isExist = userService.isLoginExists("");

		assertThat(isExist).isFalse();
	}

	@Test
	public void findById_UserIsExist_User() {
		given(userRepository.findById(anyLong())).willReturn(Optional.of(testUser));

		User user = userService.findById(1L);

		assertThat(user).isNotNull();
	}

	@Test
	public void findById_UserIsNotExist_ThrowUserNotFoundException() {
		given(userRepository.findById(anyLong())).willReturn(Optional.empty());

		assertThatThrownBy(() -> {
			userService.findById(1L);
		}).isInstanceOf(UserNotFoundException.class);
	}

	@Test
	public void findByLogin_UserIsExist_User() {
		given(userRepository.findByLogin(anyString())).willReturn(Optional.of(testUser));

		User user = userService.findByLogin("");

		assertThat(user).isNotNull();
	}

	@Test
	public void findByLogin_UserIsNotExist_ThrowUserNotFoundException() {
		given(userRepository.findByLogin(anyString())).willReturn(Optional.empty());

		assertThatThrownBy(() -> {
			userService.findByLogin("");
		}).isInstanceOf(UserNotFoundException.class);
	}

	@Test
	void findNotMemberOrNotInvitedUser_UserIsExist_ListOfUser() {
		given(userRepository.findNotMemberOrNotInvited(eq(1L), anyString())).willReturn(getUserList(3));
	
		List<User> list = userService.findNotMemberOrNotInvitedUser(1L, "a");

		assertThat(list).hasSize(3);
	}

	@Test
	void findNotMemberOrNotInvitedUser_UserIsNotExist_EmptyList() {
		given(userRepository.findNotMemberOrNotInvited(eq(1L), anyString())).willReturn(new ArrayList<User>());
	
		List<User> list = userService.findNotMemberOrNotInvitedUser(1L, "a");

		assertThat(list).isEmpty();
	}

	@Test
	public void create_LoginIsExist_ThrowDuplicateUserNameException() {
		given(userRepository.existsByLogin(anyString())).willReturn(true);

		assertThatThrownBy(() -> {
			userService.create(getCreateUserParam());
		}).isInstanceOf(DuplicateUserNameException.class);
	}

	@Test
	public void create_LoginIsNotExist_User() {
		given(userRepository.existsByLogin(anyString())).willReturn(false);
		given(userRepository.save(any(User.class))).willReturn(testUser);

		User user = userService.create(getCreateUserParam());

		assertThat(user).isNotNull();
	}

	private User getUser() {
		return User.builder()
			.id(1L)
			.login("example")
			.password("$2a$10$bblgiOA1X6W06J0kyBm1ie13O8o5Q2rm4C4.oZyA8e55PNP7OAXKu")
			.name("example")
			.email("example@example.com")
			.registerDate(LocalDateTime.of(2021, 7, 1, 22, 0, 0, 0))
			.build();
	}
	
	private List<User> getUserList(int size) {
		ArrayList<User> list = new ArrayList<>(size);
		for(int i = 1; i <= size; ++i) {
			list.add(User.builder().id(Long.valueOf(i)).build());
		}
		return list;
	}

	private SecurityUser getSecurityUser() {
		return SecurityUser.builder()
			.id(1L)
			.login("example")
			.password("$2a$10$bblgiOA1X6W06J0kyBm1ie13O8o5Q2rm4C4.oZyA8e55PNP7OAXKu")
			.name("example")
			.registerDate(LocalDateTime.of(2021, 7, 1, 22, 0, 0, 0))
			.build();
	}

	private CreateUserParam getCreateUserParam() {
		return CreateUserParam.builder()
			.login("example")
			.password("qwer1234")
			.name("example")
			.email("example@example.com")
			.build();
	}
}
