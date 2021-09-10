package com.standardkim.kanban.unit;

import java.time.LocalDateTime;
import java.util.Optional;

import com.standardkim.kanban.dto.UserDto.JoinUserRequest;
import com.standardkim.kanban.dto.UserDto.NewUserInfo;
import com.standardkim.kanban.dto.UserDto.UserInfo;
import com.standardkim.kanban.entity.User;
import com.standardkim.kanban.exception.LoginAlreadyInUseException;
import com.standardkim.kanban.repository.UserRepository;
import com.standardkim.kanban.service.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration.AccessLevel;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
	@Mock
	private UserRepository userRepository;

	@Spy
	private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	@Spy
	private ModelMapper modelMapper = new ModelMapper();

	@InjectMocks
	private UserService userService;

	@BeforeEach
    void setUp() {
		modelMapper.getConfiguration()
			.setFieldAccessLevel(AccessLevel.PRIVATE)
			.setFieldMatchingEnabled(true);
    }

	@DisplayName("When add user successfully, return added user's info")
	@Test
	public void when_AddUserSuccessfully_expect_ReturnAddedUserInfo() {
		//given
		final NewUserInfo newUserInfo = modelMapper.map(getJoinUserRequest(), NewUserInfo.class);
		final User fakeUser = new User(1L, newUserInfo.getLogin(), 
			passwordEncoder.encode(newUserInfo.getPassword()), newUserInfo.getName(), newUserInfo.getEmail(), LocalDateTime.now(), null);

		given(userRepository.existsByLogin(anyString())).willReturn(false);
		given(userRepository.findById(anyLong())).willReturn(Optional.of(fakeUser));
		given(userRepository.save(any(User.class))).willReturn(fakeUser);

		//when
		UserInfo addedUserInfo = userService.addUser(newUserInfo);

		//then
		User addedUser = userRepository.findById(addedUserInfo.getId()).get();

		assertEquals(fakeUser.getId(), addedUser.getId());
		assertEquals(fakeUser.getLogin(), addedUser.getLogin());
		assertEquals(true, passwordEncoder.matches(newUserInfo.getPassword(), addedUser.getPassword()));
		assertEquals(fakeUser.getName(), addedUser.getName());
		assertEquals(fakeUser.getRegisterDate(), addedUser.getRegisterDate());
	}

	@DisplayName("When login already in use, exception thrown.")
	@Test
	public void when_LoginAlreadyInUse_expect_ExceptionThrown() {
		//given
		NewUserInfo newUserInfo = modelMapper.map(getJoinUserRequest(), NewUserInfo.class);

		given(userRepository.existsByLogin(anyString())).willReturn(true);

		//when, then
		assertThrows(LoginAlreadyInUseException.class, () -> {
			userService.addUser(newUserInfo);
		});
	}

	private JoinUserRequest getJoinUserRequest() {
		JoinUserRequest result = new JoinUserRequest("test", "1234", "test", "aaa@example.com");
		return result;
	}
}
