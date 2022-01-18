package com.standardkim.kanban.service.projectinvitation;

import com.standardkim.kanban.domain.project.application.ProjectFindService;
import com.standardkim.kanban.domain.projectinvitation.application.ProjectInvitationFindService;
import com.standardkim.kanban.domain.projectinvitation.application.ProjectInviteService;
import com.standardkim.kanban.domain.projectinvitation.dao.ProjectInvitationRepository;
import com.standardkim.kanban.domain.projectinvitation.exception.UserAlreadyInvitedException;
import com.standardkim.kanban.domain.user.application.UserFindService;
import com.standardkim.kanban.domain.user.domain.User;
import com.standardkim.kanban.infra.mail.MailService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ProjectInviteServiceTest {
	@Mock
	private ProjectInvitationFindService projectInvitationFindService;

	@Mock
	private ProjectFindService projectFindService;

	@Mock
	private UserFindService userFindService;

	@Mock
	private ProjectInvitationRepository projectInvitationRepository;

	@Mock
	private MailService mailService;

	@InjectMocks
	private ProjectInviteService projectInviteService;

	@Test
	void invite_ProjectInvitationIsExist_ThrowUserAlreadyInvitedException() {
		given(userFindService.findById(2L)).willReturn(getUser(2L));
		given(projectInvitationFindService.isExist(1L, 2L)).willReturn(true);

		assertThatThrownBy(() -> {
			projectInviteService.invite(1L, 1L, 2L);
		}).isInstanceOf(UserAlreadyInvitedException.class);
	}

	private User getUser(Long userId) {
		return User.builder()
			.id(userId)
			.build();
	}
}
