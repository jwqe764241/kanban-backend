package com.standardkim.kanban.service.projectmember;

import com.standardkim.kanban.domain.projectinvitation.application.ProjectInvitationDeleteService;
import com.standardkim.kanban.domain.projectmember.application.ProjectMemberDeleteService;
import com.standardkim.kanban.domain.projectmember.application.ProjectMemberFindService;
import com.standardkim.kanban.domain.projectmember.application.ProjectRoleHierarchy;
import com.standardkim.kanban.domain.projectmember.domain.ProjectMember;
import com.standardkim.kanban.domain.projectmember.domain.ProjectMemberKey;
import com.standardkim.kanban.domain.projectmember.domain.ProjectRole;
import com.standardkim.kanban.domain.projectmember.dto.ProjectRoleName;
import com.standardkim.kanban.domain.projectmember.exception.CannotDeleteProjectOwnerException;
import com.standardkim.kanban.domain.projectmember.exception.ProjectMemberNotFoundException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ProjectMemberDeleteServiceTest {
	@Mock
	private ProjectMemberFindService projectMemberFindService;
	
	@Mock
	private ProjectInvitationDeleteService projectInvitationDeleteService;

	@Spy
	ProjectRoleHierarchy projectRoleHierarchy;

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
	void delete_ProjectMemberIsAdmin_ThrowCannotDeleteProjectOwnerException() {
		given(projectMemberFindService.findById(1L, 1L)).willReturn(getAdminProjectMember(1L, 1L));

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

	private ProjectMember getAdminProjectMember(Long projectId, Long userId) {
		ProjectMemberKey id = getProjectMemberKey(projectId, userId);
		return ProjectMember.builder()
			.id(id)
			.projectRole(getAdminProjectRole())
			.build();
	}

	private ProjectRole getAdminProjectRole() {
		try {
			Constructor<?> ctor = ProjectRole.class.getDeclaredConstructor();
			ctor.setAccessible(true);
			ProjectRole projectRole = (ProjectRole)ctor.newInstance();
			Field f = projectRole.getClass().getDeclaredField("name");
			f.setAccessible(true);
			f.set(projectRole, ProjectRoleName.ADMIN);
			return projectRole;
		} catch (Exception e) {
			return null;
		}
	}
}
