package com.standardkim.kanban.service;

import com.standardkim.kanban.domain.project.domain.Project;
import com.standardkim.kanban.domain.projectinvitation.application.ProjectInvitationDeleteService;
import com.standardkim.kanban.domain.projectinvitation.application.ProjectInvitationFindService;
import com.standardkim.kanban.domain.projectmember.application.ProjectMemberService;
import com.standardkim.kanban.domain.projectmember.dao.ProjectMemberRepository;
import com.standardkim.kanban.domain.projectmember.domain.ProjectMember;
import com.standardkim.kanban.domain.projectmember.domain.ProjectMemberKey;
import com.standardkim.kanban.domain.projectmember.exception.CannotDeleteProjectOwnerException;
import com.standardkim.kanban.domain.projectmember.exception.InvitationNotFoundException;
import com.standardkim.kanban.domain.projectmember.exception.ProjectMemberNotFoundException;
import com.standardkim.kanban.domain.user.domain.User;

import org.junit.jupiter.api.BeforeEach;
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
public class ProjectMemberServiceTest {
	@Mock
	ProjectMemberRepository projectMemberRepository;

	@Mock
	private ProjectInvitationFindService projectInvitationFindService;
	
	@Mock
	private ProjectInvitationDeleteService projectInvitationDeleteService;

	@InjectMocks
	ProjectMemberService projectMemberService;

	@BeforeEach
	void init() {
	}

	@Test
	void isExist_ProjectMemberIsExist_True() {
		given(projectMemberRepository.existsById(eq(getProjectMemberKey(1L, 1L)))).willReturn(true);

		boolean isExist = projectMemberService.isExist(1L, 1L);
		
		assertThat(isExist).isTrue();
	}

	@Test
	void isExist_ProjectMemberIsNotExist_False() {
		given(projectMemberRepository.existsById(eq(getProjectMemberKey(1L, 1L)))).willReturn(false);

		boolean isExist = projectMemberService.isExist(1L, 1L);

		assertThat(isExist).isFalse();
	}

	@Test
	void isProjectOwner_UserIsProjectOwner_True() {
		given(projectMemberRepository.findById(getProjectMemberKey(1L, 1L))).willReturn(Optional.of(getProjectMember(1L, 1L, true)));
	
		boolean isProjectOwner = projectMemberService.isProjectOwner(1L, 1L);

		assertThat(isProjectOwner).isTrue();
	}

	@Test
	void isProjectOwner_UserIsNotProjectOwner_False() {
		given(projectMemberRepository.findById(getProjectMemberKey(1L, 1L))).willReturn(Optional.of(getProjectMember(1L, 1L, false)));
	
		boolean isProjectOwner = projectMemberService.isProjectOwner(1L, 1L);

		assertThat(isProjectOwner).isFalse();
	}

	@Test
	void isProjectOwner_ProjectMemberIsNotExist_ThrowProjectMemberNotFoundException() {
		given(projectMemberRepository.findById(getProjectMemberKey(1L, 1L))).willReturn(Optional.empty());
	
		assertThatThrownBy(() -> {
			projectMemberService.isProjectOwner(1L, 1L);
		}).isInstanceOf(ProjectMemberNotFoundException.class);
	}

	@Test
	void findById_ProjectMemberIsExist_ProjectMember() {
		given(projectMemberRepository.findById(getProjectMemberKey(1L, 1L))).willReturn(Optional.of(getProjectMember(1L, 1L, true)));

		ProjectMember projectMember = projectMemberService.findById(1L, 1L);

		assertThat(projectMember).isNotNull();
	}

	@Test
	void findById_ProjectMemberIsNotExist_ThrowProjectMemberNotFoundException() {
		given(projectMemberRepository.findById(getProjectMemberKey(1L, 1L))).willReturn(Optional.empty());

		assertThatThrownBy(() -> {
			projectMemberService.findById(1L, 1L);
		}).isInstanceOf(ProjectMemberNotFoundException.class);
	}

	@Test
	void findByProjectId_ProjectMemberIsExist_ListOfProjectMember() {
		given(projectMemberRepository.findByProjectIdOrderByCreatedAtAsc(1L)).willReturn(getProjectMemberList(1L, 3));

		List<ProjectMember> projectMembers = projectMemberService.findByProjectId(1L);

		assertThat(projectMembers).hasSize(3);
	}
	
	@Test
	void findByProjectId_ProjectMemberIsNotExist_EmptyList() {
		given(projectMemberRepository.findByProjectIdOrderByCreatedAtAsc(1L)).willReturn(new ArrayList<ProjectMember>());

		List<ProjectMember> projectMembers = projectMemberService.findByProjectId(1L);

		assertThat(projectMembers).isEmpty();
	}

	@Test
	void accept_ProjectInvitationIsNotExist_ThrowInvitationNotFoundException() {
		given(projectInvitationFindService.isExist(1L, 1L)).willReturn(false);

		assertThatThrownBy(() -> {
			projectMemberService.accept(1L, 1L);
		}).isInstanceOf(InvitationNotFoundException.class);
	}

	@Test
	void delete_ProjectMemberIsNotExist_DoesNotThrowAnyException() {
		given(projectMemberRepository.findById(getProjectMemberKey(1L, 1L))).willReturn(Optional.empty());

		assertThatCode(() -> {
			projectMemberService.delete(1L, 1L);
		}).doesNotThrowAnyException();
	}

	@Test
	void delete_ProjectMemberIsProjectOwner_ThrowCannotDeleteProjectOwnerException() {
		given(projectMemberRepository.findById(getProjectMemberKey(1L, 1L))).willReturn(Optional.of(getProjectMember(1L, 1L, true)));

		assertThatThrownBy(() -> {
			projectMemberService.delete(1L, 1L);
		}).isInstanceOf(CannotDeleteProjectOwnerException.class);
	}

	private ProjectMemberKey getProjectMemberKey(Long projectId, Long userId) {
		return ProjectMemberKey.builder()
			.projectId(projectId)
			.userId(userId)
			.build();
	}

	private ProjectMember getProjectMember(Long projectId, Long userId, boolean isRegister) {
		ProjectMemberKey id = getProjectMemberKey(projectId, userId);
		return ProjectMember.builder()
			.id(id)
			.project(Project.builder()
				.id(projectId)
				.build()
			)
			.user(User.builder()
				.id(userId)
				.build()
			)
			.isRegister(isRegister)
			.build();
	}

	private List<ProjectMember> getProjectMemberList(Long projectId, int size) {
		ArrayList<ProjectMember> list = new ArrayList<>(size);
		for(int i = 1; i <= size; ++i) {
			list.add(getProjectMember(projectId, Long.valueOf(i), i  == 1));
		}
		return list;
	}
}
