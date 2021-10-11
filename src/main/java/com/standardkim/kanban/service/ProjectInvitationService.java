package com.standardkim.kanban.service;

import java.util.List;
import java.util.Optional;

import com.standardkim.kanban.dto.MailDto.InviteProjectMailParam;
import com.standardkim.kanban.dto.ProjectInvitationDto.InvitedUserDetail;
import com.standardkim.kanban.entity.Project;
import com.standardkim.kanban.entity.ProjectInvitation;
import com.standardkim.kanban.entity.ProjectInvitationKey;
import com.standardkim.kanban.entity.User;
import com.standardkim.kanban.exception.ResourceNotFoundException;
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
	public boolean isExists(Long projectId, Long invitedUserId) {
		ProjectInvitationKey key = ProjectInvitationKey.builder()
			.projectId(projectId)
			.invitedUserId(invitedUserId)
			.build();
		return projectInvitationRepository.existsById(key);
	}

	@Transactional(readOnly = true)
	public ProjectInvitation findById(ProjectInvitationKey projectInvitationKey) {
		Optional<ProjectInvitation> projectInvitation = projectInvitationRepository.findById(projectInvitationKey);
		return projectInvitation.orElseThrow(() -> new ResourceNotFoundException("resource not found"));
	}

	@Transactional(readOnly = true)
	public List<ProjectInvitation> findByProjectId(Long projectId) {
		return projectInvitationRepository.findByProjectId(projectId);
	}

	@Transactional(readOnly = true)
	public List<InvitedUserDetail> findInvitedUserDetailByProjectId(Long projectId) {
		List<ProjectInvitation> invitations = findByProjectId(projectId);
		List<InvitedUserDetail> invitedUserDetails = modelMapper.map(invitations, new TypeToken<List<InvitedUserDetail>>(){}.getType());
		return invitedUserDetails;
	}

	@Transactional(rollbackFor = Exception.class)
	private ProjectInvitation create(Project project, User invitedUser, User registerUser) {
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
	public void delete(Long projectId, Long invitedUserId) {
		if(!isExists(projectId, invitedUserId)) {
			return;
		}
		ProjectInvitationKey key = ProjectInvitationKey.builder()
			.projectId(projectId)
			.invitedUserId(invitedUserId)
			.build();
		projectInvitationRepository.deleteById(key);
	}

	@Transactional(rollbackFor = Exception.class)
	public InvitedUserDetail invite(Long projectId, Long invitedUserId) {
		User invitedUser = userService.findById(invitedUserId);
		if(isExists(projectId, invitedUser.getId())) {
			throw new UserAlreadyInvitedException("user already invited");
		}

		User user = userService.findBySecurityUser();
		Project project = projectService.findById(projectId);
		ProjectInvitation projectInvitation = create(project, invitedUser, user);

		//TODO: 시간이 오래 걸리므로 큐에 넣어서 작업하도록 수정해야 함
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
	public void accept(Long projectId) {
		User user = userService.findBySecurityUser();
		if(!isExists(projectId, user.getId())) {
			throw new UserNotInvitedException("user not invited");
		}
		projectMemberService.create(projectId, user.getId(), false);
		delete(projectId, user.getId());
	}
}
