package com.standardkim.kanban.service.user;

import com.standardkim.kanban.domain.user.application.UserFindService;
import com.standardkim.kanban.domain.user.dao.UserRepository;
import com.standardkim.kanban.domain.user.domain.User;
import com.standardkim.kanban.domain.user.exception.UserNotFoundException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserFindServiceTest {
	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private UserFindService userFindService;

	@Test
	public void isUsernameExists_UserIsExist_true() {
		given(userRepository.existsByUsername("example")).willReturn(true);

		boolean isExist = userFindService.isUsernameExists("example");
		
		assertThat(isExist).isTrue();
	}

	@Test
	public void isUsernameExists_UserIsNotExist_False() {
		given(userRepository.existsByUsername("example")).willReturn(false);

		boolean isExist = userFindService.isUsernameExists("example");

		assertThat(isExist).isFalse();
	}

	@Test
	public void findById_UserIsExist_User() {
		given(userRepository.findById(1L)).willReturn(Optional.of(getUser(1L)));

		User user = userFindService.findById(1L);

		assertThat(user).isNotNull();
	}

	@Test
	public void findById_UserIsNotExist_ThrowUserNotFoundException() {
		given(userRepository.findById(1L)).willReturn(Optional.empty());

		assertThatThrownBy(() -> {
			userFindService.findById(1L);
		}).isInstanceOf(UserNotFoundException.class);
	}

	@Test
	public void findByUsername_UserIsExist_User() {
		given(userRepository.findByUsername("example")).willReturn(Optional.of(getUser("example")));

		User user = userFindService.findByUsername("example");

		assertThat(user).isNotNull();
	}

	@Test
	public void findByUsername_UserIsNotExist_ThrowUserNotFoundException() {
		given(userRepository.findByUsername("example")).willReturn(Optional.empty());

		assertThatThrownBy(() -> {
			userFindService.findByUsername("example");
		}).isInstanceOf(UserNotFoundException.class);
	}

	@Test
	void findNotMemberOrNotInvitedUser_UserIsExist_ListOfUser() {
		given(userRepository.findNotMemberOrNotInvited(eq(1L), anyString())).willReturn(getUsers(3));
	
		List<User> list = userFindService.findNotMemberOrNotInvitedUser(1L, "a");

		assertThat(list).hasSize(3);
	}

	@Test
	void findNotMemberOrNotInvitedUser_UserIsNotExist_EmptyList() {
		given(userRepository.findNotMemberOrNotInvited(eq(1L), anyString())).willReturn(new ArrayList<User>());
	
		List<User> list = userFindService.findNotMemberOrNotInvitedUser(1L, "a");

		assertThat(list).isEmpty();
	}

	private User getUser(Long id) {
		return User.builder()
			.id(id)
			.build();
	}

	private User getUser(String username) {
		return User.builder()
			.username(username)
			.build();
	}

	private List<User> getUsers(int size) {
		ArrayList<User> list = new ArrayList<>(size);
		for(int i = 1; i <= size; ++i) {
			list.add(getUser(Long.valueOf(i)));
		}
		return list;
	}
}
