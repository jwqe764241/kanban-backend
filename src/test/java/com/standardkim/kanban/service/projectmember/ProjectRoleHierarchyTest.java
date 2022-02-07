package com.standardkim.kanban.service.projectmember;

import com.standardkim.kanban.domain.projectmember.application.ProjectRoleHierarchy;
import com.standardkim.kanban.domain.projectmember.dto.ProjectRoleName;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ProjectRoleHierarchyTest {
	@InjectMocks
	private ProjectRoleHierarchy projectRoleHierarchy;

	@Test
	void hasAdminRole_Admin_True() {
		boolean result = projectRoleHierarchy.hasAdminRole(ProjectRoleName.ADMIN);
		assertThat(result).isTrue();
	}

	@Test
	void hasAdminRole_Manager_False() {
		boolean result = projectRoleHierarchy.hasAdminRole(ProjectRoleName.MANAGER);
		assertThat(result).isFalse();
	}

	@Test
	void hasAdminRole_Member_False() {
		boolean result = projectRoleHierarchy.hasAdminRole(ProjectRoleName.MEMBER);
		assertThat(result).isFalse();
	}

	@Test
	void hasManagerRole_Admin_True() {
		boolean result = projectRoleHierarchy.hasManagerRole(ProjectRoleName.ADMIN);
		assertThat(result).isTrue();
	}

	@Test
	void hasManagerRole_Manager_True() {
		boolean result = projectRoleHierarchy.hasManagerRole(ProjectRoleName.MANAGER);
		assertThat(result).isTrue();
	}

	@Test
	void hasManagerRole_Member_False() {
		boolean result = projectRoleHierarchy.hasManagerRole(ProjectRoleName.MEMBER);
		assertThat(result).isFalse();
	}

	@Test
	void hasMemberRole_Admin_True() {
		boolean result = projectRoleHierarchy.hasMemberRole(ProjectRoleName.ADMIN);
		assertThat(result).isTrue();
	}

	@Test
	void hasMemberRole_Manager_True() {
		boolean result = projectRoleHierarchy.hasMemberRole(ProjectRoleName.MANAGER);
		assertThat(result).isTrue();
	}

	@Test
	void hasMemberRole_Member_True() {
		boolean result = projectRoleHierarchy.hasMemberRole(ProjectRoleName.MEMBER);
		assertThat(result).isTrue();
	}
}
