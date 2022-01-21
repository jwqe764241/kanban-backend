package com.standardkim.kanban.service.projectinvitation;

import java.util.ArrayList;
import java.util.List;

import com.standardkim.kanban.domain.projectinvitation.application.ProjectInvitationFindService;
import com.standardkim.kanban.domain.projectinvitation.dao.ProjectInvitationRepository;
import com.standardkim.kanban.domain.projectinvitation.domain.ProjectInvitation;
import com.standardkim.kanban.domain.projectinvitation.domain.ProjectInvitationKey;
import com.standardkim.kanban.domain.projectmember.domain.ProjectMemberKey;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class ProjectInvitationFindServiceTest {
	@Mock
	private ProjectInvitationRepository projectInvitationRepository;

	@InjectMocks
	private ProjectInvitationFindService projectInvitationFindService;

	@Test
	void isExist_ProjectInvitationIsExist_True() {
		given(projectInvitationRepository.existsByProjectIdAndInvitedUserId(1L, 1L)).willReturn(true);

		boolean isExist = projectInvitationFindService.isExist(1L, 1L);

		assertThat(isExist).isTrue();
	}

	@Test
	void isExist_ProjectInvitationIsNotExist_False() {
		given(projectInvitationRepository.existsByProjectIdAndInvitedUserId(1L, 1L)).willReturn(false);

		boolean isExist = projectInvitationFindService.isExist(1L, 1L);

		assertThat(isExist).isFalse();
	}

	@Test
	void findByProjectId_ProjectInvitationIsExist_ListOfProjectInvitation() {
		given(projectInvitationRepository.findByProjectId(1L)).willReturn(getProjectInvitationList(1L, 1L, 3));

		List<ProjectInvitation> list = projectInvitationFindService.findByProjectId(1L);

		assertThat(list).hasSize(3);
	}

	@Test
	void findByProjectId_ProjectInvitationIsNotExist_EmptyList() {
		given(projectInvitationRepository.findByProjectId(1L)).willReturn(new ArrayList<>());
	
		List<ProjectInvitation> list = projectInvitationFindService.findByProjectId(1L);

		assertThat(list).isEmpty();
	}

	private ProjectInvitationKey getProjectInvitationKey(Long projectId, Long userId, Long invitedUserId) {
		ProjectMemberKey id = ProjectMemberKey.of(projectId, userId);
		return ProjectInvitationKey.of(id, invitedUserId);
	}

	private ProjectInvitation getProjectInvitation(Long projectId, Long userId, Long invitedUserId) {
		return ProjectInvitation.builder()
			.id(getProjectInvitationKey(projectId, userId, invitedUserId))
			.build();
	}

	private List<ProjectInvitation> getProjectInvitationList(Long projectId, Long userId, int size) {
		ArrayList<ProjectInvitation> list = new ArrayList<>(size);
		for(int i = 1; i <= size; ++i) {
			list.add(getProjectInvitation(projectId, userId, Long.valueOf(i)));
		}
		return list;
	}
}
