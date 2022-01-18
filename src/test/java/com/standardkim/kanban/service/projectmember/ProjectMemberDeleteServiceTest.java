package com.standardkim.kanban.service.projectmember;

import com.standardkim.kanban.domain.projectinvitation.application.ProjectInvitationDeleteService;
import com.standardkim.kanban.domain.projectmember.application.ProjectMemberDeleteService;
import com.standardkim.kanban.domain.projectmember.application.ProjectMemberFindService;
import com.standardkim.kanban.domain.projectmember.domain.ProjectMember;
import com.standardkim.kanban.domain.projectmember.domain.ProjectMemberKey;
import com.standardkim.kanban.domain.projectmember.exception.CannotDeleteProjectOwnerException;
import com.standardkim.kanban.domain.projectmember.exception.ProjectMemberNotFoundException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ProjectMemberDeleteServiceTest {
	@Mock
	private ProjectMemberFindService projectMemberFindService;
	
	@Mock
	private ProjectInvitationDeleteService projectInvitationDeleteService;

	@InjectMocks
	private ProjectMemberDeleteService ProjectMemberDeleteService;

	@Test
	void delete_ProjectMemberIsNotExist_DoesNotThrowAnyException() {
		given(projectMemberFindService.findById(1L, 1L)).willThrow(new ProjectMemberNotFoundException("not found"));

		assertThatCode(() -> {
			ProjectMemberDeleteService.delete(1L, 1L);
		}).doesNotThrowAnyException();
	}

	@Test
	void delete_ProjectMemberIsProjectOwner_ThrowCannotDeleteProjectOwnerException() {
		given(projectMemberFindService.findById(1L, 1L)).willReturn(getProjectMember(1L, 1L, true));

		assertThatThrownBy(() -> {
			ProjectMemberDeleteService.delete(1L, 1L);
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
			.isRegister(isRegister)
			.build();
	}
}
