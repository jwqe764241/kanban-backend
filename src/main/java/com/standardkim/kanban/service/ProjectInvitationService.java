package com.standardkim.kanban.service;

import java.util.List;

import com.standardkim.kanban.dto.MailDto.InviteProjectMailParam;
import com.standardkim.kanban.dto.ProjectInvitationDto.InvitedUserDetail;
import com.standardkim.kanban.entity.Project;
import com.standardkim.kanban.entity.ProjectInvitation;
import com.standardkim.kanban.entity.ProjectInvitationKey;
import com.standardkim.kanban.entity.User;
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
	public List<ProjectInvitation> getProjectInvitationsByProjectId(Long projectId) {
		return projectInvitationRepository.findByProjectId(projectId);
	}

	@Transactional(readOnly = true)
	public List<InvitedUserDetail> getInvitedUsers(Long projectId) {
		List<ProjectInvitation> invitations = getProjectInvitationsByProjectId(projectId);
		List<InvitedUserDetail> invitedUserDetails = modelMapper.map(invitations, new TypeToken<List<InvitedUserDetail>>(){}.getType());
		return invitedUserDetails;
	}

	@Transactional(rollbackFor = Exception.class)
	private ProjectInvitation addProjectInvitation(Project project, User invitedUser, User registerUser) {
		ProjectInvitationKey key = ProjectInvitationKey.builder()
			.projectId(project.getId())
			.invitedUserId(invitedUser.getId())
			.build();
		ProjectInvitation invitation = ProjectInvitation.builder()
			.id(key)
			.project(project)
			.invitedUser(invitedUser)
			.registerUser(registerUser)
			.build();

		ProjectInvitation newInvitation = projectInvitationRepository.saveAndFlush(invitation);
		return newInvitation;
	}

	@Transactional(rollbackFor = Exception.class)
	public InvitedUserDetail inviteUser(Long projectId, Long invitedUserId) {
		User invitedUser = userService.getUserById(invitedUserId);
		if(isInvitationExists(projectId, invitedUser.getId())) {
			throw new UserAlreadyInvitedException("user already invited");
		}

		User user = userService.getAuthenticatedUser();
		Project project = projectService.getProjectById(projectId);
		ProjectInvitation projectInvitation = addProjectInvitation(project, invitedUser, user);

		InviteProjectMailParam inviteProjectParam = InviteProjectMailParam.builder()
			.inviteeMailAddress(invitedUser.getEmail())
			.projectId(project.getId())
			.projectName(project.getName())
			.inviteeLogin(invitedUser.getLogin())
			.inviterLogin(user.getLogin())
			.build();
		
		mailService.sendProjectInvitationMessage(inviteProjectParam);

		InvitedUserDetail invitedUserDetail = modelMapper.map(projectInvitation, InvitedUserDetail.class);
		return invitedUserDetail;
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
}
