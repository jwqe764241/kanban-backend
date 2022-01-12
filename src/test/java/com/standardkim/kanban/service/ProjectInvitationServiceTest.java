package com.standardkim.kanban.service;

import com.standardkim.kanban.domain.project.application.ProjectService;
import com.standardkim.kanban.domain.projectinvitation.application.ProjectInvitationService;
import com.standardkim.kanban.domain.projectinvitation.domain.ProjectInvitation;
import com.standardkim.kanban.domain.projectinvitation.domain.ProjectInvitationKey;
import com.standardkim.kanban.domain.projectinvitation.domain.ProjectInvitationRepository;
import com.standardkim.kanban.domain.projectinvitation.exception.UserAlreadyInvitedException;
import com.standardkim.kanban.domain.projectmember.application.ProjectMemberService;
import com.standardkim.kanban.domain.projectmember.domain.ProjectMemberKey;
import com.standardkim.kanban.domain.user.application.UserService;
import com.standardkim.kanban.domain.user.domain.User;
import com.standardkim.kanban.infra.mail.MailService;

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
	void isExist_ProjectInvitationIsExist_True() {
		given(projectInvitationRepository.existsByProjectIdAndInvitedUserId(1L, 1L)).willReturn(true);

		boolean isExist = projectInvitationService.isExist(1L, 1L);

		assertThat(isExist).isTrue();
	}

	@Test
	void isExist_ProjectInvitationIsNotExist_False() {
		given(projectInvitationRepository.existsByProjectIdAndInvitedUserId(1L, 1L)).willReturn(false);

		boolean isExist = projectInvitationService.isExist(1L, 1L);

		assertThat(isExist).isFalse();
	}

	@Test
	void findByProjectId_ProjectInvitationIsExist_ListOfProjectInvitation() {
		given(projectInvitationRepository.findByProjectId(1L)).willReturn(getProjectInvitationList(1L, 3));

		List<ProjectInvitation> list = projectInvitationService.findByProjectId(1L);

		assertThat(list).hasSize(3);
	}

	@Test
	void findByProjectId_ProjectInvitationIsNotExist_EmptyList() {
		given(projectInvitationRepository.findByProjectId(1L)).willReturn(new ArrayList<>());
	
		List<ProjectInvitation> list = projectInvitationService.findByProjectId(1L);

		assertThat(list).isEmpty();
	}

	@Test
	void invite_ProjectInvitationIsExist_ThrowUserAlreadyInvitedException() {
		given(userService.findById(2L)).willReturn(getUser(2L));
		given(projectInvitationRepository.existsByProjectIdAndInvitedUserId(1L, 2L)).willReturn(true);

		assertThatThrownBy(() -> {
			projectInvitationService.invite(1L, 1L, 2L);
		}).isInstanceOf(UserAlreadyInvitedException.class);
	}

	private User getUser(Long userId) {
		return User.builder()
			.id(userId)
			.build();
	}

	private ProjectInvitationKey getProjectInvitationKey(Long projectId, Long userId, Long invitedUserId) {
		ProjectMemberKey id = ProjectMemberKey.from(projectId, userId);
		return ProjectInvitationKey.from(id, invitedUserId);
	}

	private ProjectInvitation getProjectInvitation(Long projectId, Long userId, Long invitedUserId) {
		return ProjectInvitation.builder()
			.id(getProjectInvitationKey(projectId, userId, invitedUserId))
			.build();
	}
	
	private List<ProjectInvitation> getProjectInvitationList(Long projectId, int size) {
		ArrayList<ProjectInvitation> list = new ArrayList<>(size);
		for(int i = 1; i <= size; ++i) {
			list.add(getProjectInvitation(projectId, 1L, Long.valueOf(i)));
		}
		return list;
	}
}
