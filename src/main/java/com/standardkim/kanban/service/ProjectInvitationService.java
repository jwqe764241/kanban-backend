package com.standardkim.kanban.service;

import java.util.List;

import com.standardkim.kanban.dto.MailDto.InviteProjectMailParam;
import com.standardkim.kanban.entity.Project;
import com.standardkim.kanban.entity.ProjectInvitation;
import com.standardkim.kanban.entity.User;
import com.standardkim.kanban.exception.project.UserAlreadyInvitedException;
import com.standardkim.kanban.repository.ProjectInvitationRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectInvitationService {
	private final ProjectInvitationRepository projectInvitationRepository;

	private final ProjectService projectService;
	
	private final UserService userService;

	private final MailService mailService;

	@Transactional(readOnly = true)
	public boolean isExist(Long projectId, Long invitedUserId) {
		return projectInvitationRepository.existsByProjectIdAndInvitedUserId(projectId, invitedUserId);
	}

	@Transactional(readOnly = true)
	public List<ProjectInvitation> findByProjectId(Long projectId) {
		List<ProjectInvitation> invitations = projectInvitationRepository.findByProjectId(projectId);
		return invitations;
	}

	@Transactional(rollbackFor = Exception.class)
	private ProjectInvitation create(Project project, User inviteeUser, User inviterUser) {
		ProjectInvitation invitation = ProjectInvitation.from(project, inviteeUser, inviterUser);
		return projectInvitationRepository.save(invitation);
	}

	@Transactional(rollbackFor = Exception.class)
	public void deleteByProjectIdAndInvitedUserId(Long projectId, Long invitedUserId) {
		projectInvitationRepository.deleteByProjectIdAndInvitedUserId(projectId, invitedUserId);
	}

	@Transactional(rollbackFor = Exception.class)
	public void deleteByProjectIdAndUserId(Long projectId, Long userId) {
		projectInvitationRepository.deleteByProjectIdAndUserId(projectId, userId);
	}

	@Transactional(rollbackFor = Exception.class)
	public ProjectInvitation invite(Long projectId, Long inviterUserId, Long inviteeUserId) {
		User inviteeUser = userService.findById(inviteeUserId);
		if(isExist(projectId, inviteeUser.getId())) {
			throw new UserAlreadyInvitedException("user already invited");
		}

		User inviterUser = userService.findById(inviterUserId);
		Project project = projectService.findById(projectId);
		ProjectInvitation projectInvitation = create(project, inviteeUser, inviterUser);

		//TODO: 시간이 오래 걸리므로 큐에 넣어서 작업하도록 수정해야 함
		mailService.sendInviteProjectMail(InviteProjectMailParam.from(project, inviterUser, inviteeUser));

		return projectInvitation;
	}
}
