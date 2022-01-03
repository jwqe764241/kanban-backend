package com.standardkim.kanban.controller;

import java.util.List;

import javax.validation.Valid;

import com.standardkim.kanban.dto.AuthenticationDto.SecurityUser;
import com.standardkim.kanban.dto.ProjectInvitationDto.InviteProjectMemeberParam;
import com.standardkim.kanban.dto.ProjectInvitationDto.InvitedUserDetail;
import com.standardkim.kanban.entity.ProjectInvitation;
import com.standardkim.kanban.service.ProjectInvitationService;
import com.standardkim.kanban.service.UserService;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ProjectInvitationController {
	private final ProjectInvitationService projectInvitationService;

	private final UserService userService;

	private final ModelMapper modelMapper;

	@PostMapping("/projects/{projectId}/invitations")
	@ResponseStatus(HttpStatus.OK)
	@PreAuthorize("isProjectOwner(#projectId)")
	public InvitedUserDetail inviteProjectMember(@PathVariable Long projectId, 
		@RequestBody @Valid InviteProjectMemeberParam param) {
		SecurityUser securityUser = userService.getSecurityUser();
		ProjectInvitation projectInvitation = projectInvitationService.invite(projectId, securityUser.getId(), param.getUserId());
		InvitedUserDetail invitedUserDetail = modelMapper.map(projectInvitation, InvitedUserDetail.class);
		return invitedUserDetail;
	}

	@GetMapping("/projects/{projectId}/invitations")
	@ResponseStatus(HttpStatus.OK)
	@PreAuthorize("isProjectOwner(#projectId)")
	public List<InvitedUserDetail> getInvitations(@PathVariable Long projectId) {
		List<ProjectInvitation> projectInvitations =projectInvitationService.findByProjectId(projectId);
		List<InvitedUserDetail> invitedUserDetails = modelMapper.map(projectInvitations, new TypeToken<List<InvitedUserDetail>>(){}.getType());
		return invitedUserDetails;
	}

	@DeleteMapping("/projects/{projectId}/invitations/{userId}")
	@ResponseStatus(HttpStatus.OK)
	@PreAuthorize("isProjectOwner(#projectId)")
	public void removeInvitation(@PathVariable Long projectId, @PathVariable Long userId) {
		projectInvitationService.deleteByProjectIdAndInvitedUserId(projectId, userId);
	}
}
