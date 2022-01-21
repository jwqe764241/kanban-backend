package com.standardkim.kanban.service.projectmember;

import com.standardkim.kanban.domain.projectinvitation.application.ProjectInvitationFindService;
import com.standardkim.kanban.domain.projectmember.application.ProjectMemberAcceptService;
import com.standardkim.kanban.domain.projectmember.exception.InvitationNotFoundException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ProjectMemberAcceptServiceTest {
	@Mock
	private ProjectInvitationFindService projectInvitationFindService;

	@InjectMocks
	private ProjectMemberAcceptService projectMemberAcceptService;

	@Test
	void accept_ProjectInvitationIsNotExist_ThrowInvitationNotFoundException() {
		given(projectInvitationFindService.isExist(1L, 1L)).willReturn(false);

		assertThatThrownBy(() -> {
			projectMemberAcceptService.accept(1L, 1L);
		}).isInstanceOf(InvitationNotFoundException.class);
	}
}
