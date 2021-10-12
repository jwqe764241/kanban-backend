package com.standardkim.kanban.unit;

import java.time.LocalDateTime;
import java.util.Optional;

import com.standardkim.kanban.dto.UserDto.CreateUserParameter;
import com.standardkim.kanban.dto.UserDto.UserDetail;
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

	@DisplayName("When add user successfully, return added user's detail")
	@Test
	public void when_AddUserSuccessfully_expect_ReturnAddedUserDetail() {
		//given
		final CreateUserParameter createUserParameter = modelMapper.map(getCreateUserParameter(), CreateUserParameter.class);
		final User fakeUser = new User(1L, createUserParameter.getLogin(), 
			passwordEncoder.encode(createUserParameter.getPassword()), createUserParameter.getName(), createUserParameter.getEmail(), LocalDateTime.now(), null);

		given(userRepository.existsByLogin(anyString())).willReturn(false);
		given(userRepository.findById(anyLong())).willReturn(Optional.of(fakeUser));
		given(userRepository.save(any(User.class))).willReturn(fakeUser);

		//when
		UserDetail addedUserDetail = userService.create(createUserParameter);

		//then
		User addedUser = userRepository.findById(addedUserDetail.getId()).get();

		assertEquals(fakeUser.getId(), addedUser.getId());
		assertEquals(fakeUser.getLogin(), addedUser.getLogin());
		assertEquals(true, passwordEncoder.matches(createUserParameter.getPassword(), addedUser.getPassword()));
		assertEquals(fakeUser.getName(), addedUser.getName());
		assertEquals(fakeUser.getRegisterDate(), addedUser.getRegisterDate());
	}

	@DisplayName("When login already in use, exception thrown.")
	@Test
	public void when_LoginAlreadyInUse_expect_ExceptionThrown() {
		//given
		CreateUserParameter createUserParameter = modelMapper.map(getCreateUserParameter(), CreateUserParameter.class);

		given(userRepository.existsByLogin(anyString())).willReturn(true);

		//when, then
		assertThrows(LoginAlreadyInUseException.class, () -> {
			userService.create(createUserParameter);
		});
	}

	private CreateUserParameter getCreateUserParameter() {
		CreateUserParameter result = new CreateUserParameter("test", "1234", "test", "aaa@example.com");
		return result;
	}
}
