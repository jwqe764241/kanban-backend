package com.standardkim.kanban.domain.projectinvitation.api;

import java.util.List;

import javax.validation.Valid;

import com.standardkim.kanban.domain.projectinvitation.application.ProjectInvitationDeleteService;
import com.standardkim.kanban.domain.projectinvitation.application.ProjectInvitationFindService;
import com.standardkim.kanban.domain.projectinvitation.application.ProjectInviteService;
import com.standardkim.kanban.domain.projectinvitation.domain.ProjectInvitation;
import com.standardkim.kanban.domain.projectinvitation.dto.InviteProjectMemeberParam;
import com.standardkim.kanban.domain.projectinvitation.dto.InvitedUserDetail;
import com.standardkim.kanban.global.auth.dto.SecurityUser;
import com.standardkim.kanban.global.util.SecurityContextFacade;

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
public class ProjectInvitationApi {
	private final ProjectInvitationFindService projectInvitationFindService;

	private final ProjectInviteService projectInviteService;

	private final ProjectInvitationDeleteService projectInvitationDeleteService;

	private final ModelMapper modelMapper;

	@GetMapping("/projects/{projectId}/invitations")
	@ResponseStatus(HttpStatus.OK)
	@PreAuthorize("hasProjectRole(#projectId, 'MANAGER')")
	public List<InvitedUserDetail> getInvitations(@PathVariable Long projectId) {
		List<ProjectInvitation> projectInvitations = projectInvitationFindService.findByProjectId(projectId);
		List<InvitedUserDetail> invitedUserDetails = modelMapper.map(projectInvitations, new TypeToken<List<InvitedUserDetail>>(){}.getType());
		return invitedUserDetails;
	}

	@PostMapping("/projects/{projectId}/invitations")
	@ResponseStatus(HttpStatus.OK)
	@PreAuthorize("hasProjectRole(#projectId, 'MANAGER')")
	public InvitedUserDetail inviteProjectMember(@PathVariable Long projectId, 
		@RequestBody @Valid InviteProjectMemeberParam param) {
		SecurityUser securityUser = SecurityContextFacade.getSecurityUser();
		ProjectInvitation projectInvitation = projectInviteService.invite(projectId, securityUser.getId(), param.getUserId());
		InvitedUserDetail invitedUserDetail = modelMapper.map(projectInvitation, InvitedUserDetail.class);
		return invitedUserDetail;
	}

	@DeleteMapping("/projects/{projectId}/invitations/{userId}")
	@ResponseStatus(HttpStatus.OK)
	@PreAuthorize("hasProjectRole(#projectId, 'MANAGER')")
	public void removeInvitation(@PathVariable Long projectId, @PathVariable Long userId) {
		projectInvitationDeleteService.deleteByProjectIdAndInvitedUserId(projectId, userId);
	}
}
