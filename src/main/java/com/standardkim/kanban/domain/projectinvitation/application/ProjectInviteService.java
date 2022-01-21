package com.standardkim.kanban.domain.projectinvitation.application;

import com.standardkim.kanban.domain.project.application.ProjectFindService;
import com.standardkim.kanban.domain.project.domain.Project;
import com.standardkim.kanban.domain.projectinvitation.dao.ProjectInvitationRepository;
import com.standardkim.kanban.domain.projectinvitation.domain.ProjectInvitation;
import com.standardkim.kanban.domain.projectinvitation.exception.UserAlreadyInvitedException;
import com.standardkim.kanban.domain.user.application.UserFindService;
import com.standardkim.kanban.domain.user.domain.User;
import com.standardkim.kanban.infra.mail.application.InviteProjectMailSendService;
import com.standardkim.kanban.infra.mail.dto.InviteProjectMailParam;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectInviteService {
	private final ProjectInvitationFindService projectInvitationFindService;

	private final ProjectFindService projectFindService;

	private final UserFindService userFindService;

	private final ProjectInvitationRepository projectInvitationRepository;

	private final InviteProjectMailSendService inviteProjectMailSendService;

	@Transactional(rollbackFor = Exception.class)
	private ProjectInvitation create(Project project, User inviteeUser, User inviterUser) {
		ProjectInvitation invitation = ProjectInvitation.of(project, inviteeUser, inviterUser);
		return projectInvitationRepository.save(invitation);
	}

	@Transactional(rollbackFor = Exception.class)
	public ProjectInvitation invite(Long projectId, Long inviterUserId, Long inviteeUserId) {
		User inviteeUser = userFindService.findById(inviteeUserId);
		if(projectInvitationFindService.isExist(projectId, inviteeUser.getId())) {
			throw new UserAlreadyInvitedException("user already invited");
		}

		User inviterUser = userFindService.findById(inviterUserId);
		Project project = projectFindService.findById(projectId);
		ProjectInvitation projectInvitation = create(project, inviteeUser, inviterUser);

		//TODO: 시간이 오래 걸리므로 큐에 넣어서 작업하도록 수정해야 함
		inviteProjectMailSendService.send(InviteProjectMailParam.of(project, inviterUser, inviteeUser));

		return projectInvitation;
	}
}
