package com.standardkim.kanban.service;

import java.util.List;

import com.standardkim.kanban.dto.MailDto.ProjectInvitationMailInfo;
import com.standardkim.kanban.dto.ProjectInvitationDto.InvitedUserInfo;
import com.standardkim.kanban.entity.Project;
import com.standardkim.kanban.entity.ProjectInvitation;
import com.standardkim.kanban.entity.ProjectInvitationKey;
import com.standardkim.kanban.entity.User;
import com.standardkim.kanban.exception.PermissionException;
import com.standardkim.kanban.exception.UserAlreadyInvitedException;
import com.standardkim.kanban.exception.UserNotInvitedException;
import com.standardkim.kanban.repository.ProjectInvitationRepository;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectInvitationService {
	private final ProjectInvitationRepository projectInvitationRepository;

	private final ProjectService projectService;

	private final ProjectMemberService projectMemberService;
	
	private final UserService userService;

	private final ModelMapper modelMapper;

	private final MailService mailService;

	@Transactional(readOnly = true)
	public boolean isInvitationExists(Long projectId, Long invitedUserId) {
		ProjectInvitationKey key = ProjectInvitationKey.builder()
			.projectId(projectId)
			.invitedUserId(invitedUserId)
			.build();
		return projectInvitationRepository.existsById(key);
	}

	@Transactional(readOnly = true)
	public List<ProjectInvitation> getInvitationsByProjectId(Long projectId) {
		return projectInvitationRepository.findByProjectId(projectId);
	}

	@Transactional(readOnly = true)
	public List<InvitedUserInfo> getInvitedUsers(Long projectId) {
		User user = userService.getAuthenticatedUser();
		if(!projectMemberService.isProjectOwner(projectId, user.getId())) {
			throw new PermissionException("no permission to access project [" + projectId + "]");
		}

		List<ProjectInvitation> invitations = getInvitationsByProjectId(projectId);
		List<InvitedUserInfo> invitedUsers = modelMapper.map(invitations, new TypeToken<List<InvitedUserInfo>>(){}.getType());
		return invitedUsers;
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

		User invitedUser = userService.getUserById(invitedUserId);
		if(isInvitationExists(projectId, invitedUser.getId())) {
			throw new UserAlreadyInvitedException("user already invited");
		}

		Project project = projectService.getProjectById(projectId);

		addProjectInvite(projectId, invitedUser.getId(), user);

		ProjectInvitationMailInfo info = ProjectInvitationMailInfo.builder()
			.inviteeMailAddress(invitedUser.getEmail())
			.projectId(project.getId())
			.projectName(project.getName())
			.inviteeLogin(invitedUser.getLogin())
			.inviterLogin(user.getLogin())
			.build();
		
		mailService.sendProjectInvitationMessage(info);
	}

	@Transactional(rollbackFor = Exception.class)
	public void deleteInvitation(Long projectId, Long invitedUserId) {
		if(!isInvitationExists(projectId, invitedUserId)) {
			return;
		}

		ProjectInvitationKey key = ProjectInvitationKey.builder()
			.projectId(projectId)
			.invitedUserId(invitedUserId)
			.build();
		projectInvitationRepository.deleteById(key);
	}

	@Transactional(rollbackFor = Exception.class)
	public void acceptInvite(Long projectId) {
		User user = userService.getAuthenticatedUser();
		if(!isInvitationExists(projectId, user.getId())) {
			throw new UserNotInvitedException("user not invited");
		}
		projectMemberService.addProjectMemeber(projectId, user.getId(), false);
		deleteInvitation(projectId, user.getId());
	}

	@Transactional(rollbackFor = Exception.class)
	public void cancelInvitation(Long projectId, Long invitedUserId) {
		User user = userService.getAuthenticatedUser();
		if(!projectMemberService.isProjectOwner(projectId, user.getId())) {
			throw new PermissionException("no permission to access project [" + projectId + "]");
		}
		deleteInvitation(projectId, user.getId());
	}
}
