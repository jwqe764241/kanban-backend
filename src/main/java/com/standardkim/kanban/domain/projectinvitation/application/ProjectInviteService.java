package com.standardkim.kanban.domain.projectinvitation.application;

import com.standardkim.kanban.domain.projectinvitation.dao.ProjectInvitationRepository;
import com.standardkim.kanban.domain.projectinvitation.domain.ProjectInvitation;
import com.standardkim.kanban.domain.projectinvitation.exception.UserAlreadyInvitedException;
import com.standardkim.kanban.domain.projectmember.application.ProjectMemberFindService;
import com.standardkim.kanban.domain.projectmember.domain.ProjectMember;
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

	private final ProjectMemberFindService projectMemberFindService;

	private final UserFindService userFindService;

	private final ProjectInvitationRepository projectInvitationRepository;

	private final InviteProjectMailSendService inviteProjectMailSendService;

	@Transactional(rollbackFor = Exception.class)
	private ProjectInvitation create(ProjectMember projectMember, User inviteeUser) {
		ProjectInvitation invitation = ProjectInvitation.of(projectMember, inviteeUser);
		return projectInvitationRepository.save(invitation);
	}

	@Transactional(rollbackFor = Exception.class)
	public ProjectInvitation invite(Long projectId, Long inviterUserId, Long inviteeUserId) {
		User inviteeUser = userFindService.findById(inviteeUserId);
		if(projectInvitationFindService.isExist(projectId, inviteeUser.getId())) {
			throw new UserAlreadyInvitedException("user already invited");
		}

		ProjectMember projectMember = projectMemberFindService.findById(projectId, inviterUserId);
		ProjectInvitation projectInvitation = create(projectMember, inviteeUser);

		//TODO: 시간이 오래 걸리므로 큐에 넣어서 작업하도록 수정해야 함
		inviteProjectMailSendService.send(InviteProjectMailParam.of(projectMember, inviteeUser));

		return projectInvitation;
	}
}
