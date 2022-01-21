package com.standardkim.kanban.service.projectmember;

import static org.mockito.ArgumentMatchers.eq;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.standardkim.kanban.domain.projectmember.application.ProjectMemberFindService;
import com.standardkim.kanban.domain.projectmember.dao.ProjectMemberRepository;
import com.standardkim.kanban.domain.projectmember.domain.ProjectMember;
import com.standardkim.kanban.domain.projectmember.domain.ProjectMemberKey;
import com.standardkim.kanban.domain.projectmember.exception.ProjectMemberNotFoundException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ProjectMemberFindServiceTest {
	@Mock
	private ProjectMemberRepository projectMemberRepository;

	@InjectMocks
	private ProjectMemberFindService projectMemberFindService;

	@Test
	void isExist_ProjectMemberIsExist_True() {
		given(projectMemberRepository.existsById(eq(getProjectMemberKey(1L, 1L)))).willReturn(true);

		boolean isExist = projectMemberFindService.isExist(1L, 1L);
		
		assertThat(isExist).isTrue();
	}

	@Test
	void isExist_ProjectMemberIsNotExist_False() {
		given(projectMemberRepository.existsById(eq(getProjectMemberKey(1L, 1L)))).willReturn(false);

		boolean isExist = projectMemberFindService.isExist(1L, 1L);

		assertThat(isExist).isFalse();
	}

	@Test
	void isProjectOwner_UserIsProjectOwner_True() {
		given(projectMemberRepository.findById(getProjectMemberKey(1L, 1L))).willReturn(Optional.of(getProjectMember(1L, 1L, true)));
	
		boolean isProjectOwner = projectMemberFindService.isProjectOwner(1L, 1L);

		assertThat(isProjectOwner).isTrue();
	}

	@Test
	void isProjectOwner_UserIsNotProjectOwner_False() {
		given(projectMemberRepository.findById(getProjectMemberKey(1L, 1L))).willReturn(Optional.of(getProjectMember(1L, 1L, false)));
	
		boolean isProjectOwner = projectMemberFindService.isProjectOwner(1L, 1L);

		assertThat(isProjectOwner).isFalse();
	}

	@Test
	void isProjectOwner_ProjectMemberIsNotExist_ThrowProjectMemberNotFoundException() {
		given(projectMemberRepository.findById(getProjectMemberKey(1L, 1L))).willReturn(Optional.empty());
	
		assertThatThrownBy(() -> {
			projectMemberFindService.isProjectOwner(1L, 1L);
		}).isInstanceOf(ProjectMemberNotFoundException.class);
	}

	@Test
	void findById_ProjectMemberIsExist_ProjectMember() {
		given(projectMemberRepository.findById(getProjectMemberKey(1L, 1L))).willReturn(Optional.of(getProjectMember(1L, 1L)));

		ProjectMember projectMember = projectMemberFindService.findById(1L, 1L);

		assertThat(projectMember).isNotNull();
	}

	@Test
	void findById_ProjectMemberIsNotExist_ThrowProjectMemberNotFoundException() {
		given(projectMemberRepository.findById(getProjectMemberKey(1L, 1L))).willReturn(Optional.empty());

		assertThatThrownBy(() -> {
			projectMemberFindService.findById(1L, 1L);
		}).isInstanceOf(ProjectMemberNotFoundException.class);
	}

	@Test
	void findByProjectId_ProjectMemberIsExist_ListOfProjectMember() {
		given(projectMemberRepository.findByProjectIdOrderByCreatedAtAsc(1L)).willReturn(getProjectMembers(1L, 3));

		List<ProjectMember> projectMembers = projectMemberFindService.findByProjectId(1L);

		assertThat(projectMembers).hasSize(3);
	}
	
	@Test
	void findByProjectId_ProjectMemberIsNotExist_EmptyList() {
		given(projectMemberRepository.findByProjectIdOrderByCreatedAtAsc(1L)).willReturn(new ArrayList<ProjectMember>());

		List<ProjectMember> projectMembers = projectMemberFindService.findByProjectId(1L);

		assertThat(projectMembers).isEmpty();
	}

	private ProjectMemberKey getProjectMemberKey(Long projectId, Long userId) {
		return ProjectMemberKey.builder()
			.projectId(projectId)
			.userId(userId)
			.build();
	}

	private ProjectMember getProjectMember(Long projectId, Long userId) {
		ProjectMemberKey id = getProjectMemberKey(projectId, userId);
		return ProjectMember.builder()
			.id(id)
			.build();
	}

	private ProjectMember getProjectMember(Long projectId, Long userId, boolean isRegister) {
		ProjectMemberKey id = getProjectMemberKey(projectId, userId);
		return ProjectMember.builder()
			.id(id)
			.isRegister(isRegister)
			.build();
	}

	private List<ProjectMember> getProjectMembers(Long projectId, int size) {
		ArrayList<ProjectMember> list = new ArrayList<>(size);
		for(int i = 1; i <= size; ++i) {
			list.add(getProjectMember(projectId, Long.valueOf(i)));
		}
		return list;
	}
}
