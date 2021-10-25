package com.standardkim.kanban.service;

import com.standardkim.kanban.dto.ProjectInvitationDto.InvitedUserDetail;
import com.standardkim.kanban.entity.ProjectInvitation;
import com.standardkim.kanban.entity.ProjectInvitationKey;
import com.standardkim.kanban.entity.User;
import com.standardkim.kanban.exception.project.InvitationNotFoundException;
import com.standardkim.kanban.exception.project.UserAlreadyInvitedException;
import com.standardkim.kanban.repository.ProjectInvitationRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration.AccessLevel;

import static org.mockito.BDDMockito.*;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ProjectInvitationServiceTest {
	@Mock
	ProjectInvitationRepository projectInvitationRepository;

	@Mock
	ProjectService projectService;

	@Mock
	ProjectMemberService projectMemberService;

	@Mock
	UserService userService;

	@Spy
	ModelMapper modelMapper = new ModelMapper();

	@Mock
	MailService mailService;

	@InjectMocks
	ProjectInvitationService projectInvitationService;

	@BeforeEach
	void init() {
		modelMapper.getConfiguration()
			.setFieldAccessLevel(AccessLevel.PRIVATE)
			.setFieldMatchingEnabled(true);
	}

	@Test
	void isExists_ProjectInvitationIsExist_True() {
		given(projectInvitationRepository.existsById(getProjectInvitationKey(1L, 1L))).willReturn(true);

		boolean isExist = projectInvitationService.isExists(1L, 1L);

		assertThat(isExist).isTrue();
	}

	@Test
	void isExists_ProjectInvitationIsNotExist_False() {
		given(projectInvitationRepository.existsById(getProjectInvitationKey(1L, 1L))).willReturn(false);

		boolean isExist = projectInvitationService.isExists(1L, 1L);

		assertThat(isExist).isFalse();
	}

	@Test
	void findInvitedUserDetailByProjectId_ProjectInvitationIsExist_ListOfInvitedUserDetail() {
		given(projectInvitationRepository.findByProjectId(1L)).willReturn(getProjectInvitationList(1L, 3));

		List<InvitedUserDetail> list = projectInvitationService.findInvitedUserDetailByProjectId(1L);

		assertThat(list).hasSize(3);
	}

	@Test
	void findInvitedUserDetailByProjectId_ProjectInvitationIsNotExist_EmptyList() {
		given(projectInvitationRepository.findByProjectId(1L)).willReturn(new ArrayList<>());
	
		List<InvitedUserDetail> list = projectInvitationService.findInvitedUserDetailByProjectId(1L);

		assertThat(list).isEmpty();
	}

	@Test
	void delete_ProjectInvitationIsExist_DoesNotThrowAnyException() {
		given(projectInvitationRepository.existsById(getProjectInvitationKey(1L, 1L))).willReturn(true);

		assertThatCode(() -> {
			projectInvitationService.delete(1L, 1L);
		}).doesNotThrowAnyException();
	}

	@Test
	void invite_ProjectInvitationIsExist_ThrowUserAlreadyInvitedException() {
		given(userService.findById(1L)).willReturn(getUser(1L));
		given(projectInvitationRepository.existsById(getProjectInvitationKey(1L, 1L))).willReturn(true);

		assertThatThrownBy(() -> {
			projectInvitationService.invite(1L, 1L);
		}).isInstanceOf(UserAlreadyInvitedException.class);
	}

	@Test
	void accept_ProjectInvitationIsNotExist_ThrowInvitationNotFoundException() {
		given(userService.findBySecurityUser()).willReturn(getUser(1L));
		given(projectInvitationRepository.existsById(getProjectInvitationKey(1L, 1L))).willReturn(false);

		assertThatThrownBy(() -> {
			projectInvitationService.accept(1L);
		}).isInstanceOf(InvitationNotFoundException.class);
	}

	private User getUser(Long userId) {
		return User.builder()
			.id(userId)
			.build();
	}

	private ProjectInvitationKey getProjectInvitationKey(Long projectId, Long invitedUserId) {
		return ProjectInvitationKey.builder()
			.projectId(projectId)
			.invitedUserId(invitedUserId)
			.build();
	}

	private ProjectInvitation getProjectInvitation(Long projectId, Long invitedUserId) {
		return ProjectInvitation.builder()
			.id(getProjectInvitationKey(projectId, invitedUserId))
			.build();
	}
	
	private List<ProjectInvitation> getProjectInvitationList(Long projectId, int size) {
		ArrayList<ProjectInvitation> list = new ArrayList<>(size);
		for(int i = 1; i <= size; ++i) {
			list.add(getProjectInvitation(projectId, Long.valueOf(i)));
		}
		return list;
	}
}
