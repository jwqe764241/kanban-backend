package com.standardkim.kanban.service;

import com.standardkim.kanban.entity.ProjectInvitation;
import com.standardkim.kanban.entity.ProjectInvitationKey;
import com.standardkim.kanban.entity.User;
import com.standardkim.kanban.exception.PermissionException;
import com.standardkim.kanban.exception.UserAlreadyInvitedException;
import com.standardkim.kanban.repository.ProjectInvitationRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectInvitationService {
	private final ProjectInvitationRepository projectInvitationRepository;

	private final ProjectMemberService projectMemberService;
	
	private final UserService userService;

	@Transactional(readOnly = true)
	public boolean isInvitationExists(Long projectId, Long invitedUserId) {
		ProjectInvitationKey key = ProjectInvitationKey.builder()
			.projectId(projectId)
			.invitedUserId(invitedUserId)
			.build();
		return projectInvitationRepository.existsById(key);
	}

	@Transactional(rollbackFor = Exception.class)
	private ProjectInvitation addProjectInvite(Long projectId, Long invitedUserId, User registerUser) {
		ProjectInvitationKey key = ProjectInvitationKey.builder()
			.projectId(projectId)
			.invitedUserId(invitedUserId)
			.build();
		ProjectInvitation invitation = ProjectInvitation.builder()
			.id(key)
			.registerUser(registerUser)
			.build();
		invitation = projectInvitationRepository.save(invitation);
		return invitation;
	}

	@Transactional(rollbackFor = Exception.class)
	public void inviteUser(Long projectId, Long invitedUserId) {
		User user = userService.getAuthenticatedUser();
		if(!projectMemberService.isProjectOwner(projectId, user.getId())) {
			throw new PermissionException("no permission to access project [" + projectId + "]");
		}

		if(isInvitationExists(projectId, invitedUserId)) {
			throw new UserAlreadyInvitedException("user already invited");
		}

		addProjectInvite(projectId, invitedUserId, user);
	}

	public void acceptInvite(Long projectId) {

	}
}
