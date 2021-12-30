package com.standardkim.kanban.controller;

import java.util.List;

import javax.validation.Valid;

import com.standardkim.kanban.dto.AuthenticationDto.SecurityUser;
import com.standardkim.kanban.dto.ProjectDto.CreateProjectParam;
import com.standardkim.kanban.dto.ProjectDto.ProjectDetail;
import com.standardkim.kanban.dto.ProjectDto.UpdateProjectParam;
import com.standardkim.kanban.dto.ProjectInvitationDto.InviteProjectMemeberParam;
import com.standardkim.kanban.dto.ProjectInvitationDto.InvitedUserDetail;
import com.standardkim.kanban.entity.Project;
import com.standardkim.kanban.entity.ProjectInvitation;
import com.standardkim.kanban.service.ProjectInvitationService;
import com.standardkim.kanban.service.ProjectService;
import com.standardkim.kanban.service.UserService;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ProjectController {
	private final ProjectService projectService;

	private final ProjectInvitationService projectInvitationService;

	private final UserService userService;

	private final ModelMapper modelMapper;

	@PostMapping("/projects")
	@ResponseStatus(HttpStatus.CREATED)
	public void createProject(@RequestBody @Valid CreateProjectParam createProjectParam) {
		SecurityUser securityUser = userService.getSecurityUser();
		projectService.create(securityUser.getId(), createProjectParam);
	}

	@GetMapping("/projects")
	@ResponseStatus(HttpStatus.OK)
	public List<ProjectDetail> getMyProject() {
		SecurityUser securityUser = userService.getSecurityUser();
		List<Project> projects = projectService.findByUserId(securityUser.getId());
		List<ProjectDetail> projectDetails = modelMapper.map(projects, new TypeToken<List<ProjectDetail>>(){}.getType());
		return projectDetails;
	}

	@GetMapping("/projects/{projectId}")
	@ResponseStatus(HttpStatus.OK)
	@PreAuthorize("isProjectMember(#projectId)")
	public ProjectDetail getProject(@PathVariable Long projectId) {
		Project project = projectService.findById(projectId);
		ProjectDetail projectDetail = modelMapper.map(project, ProjectDetail.class);
		return projectDetail;
	}

	@PatchMapping("/projects/{projectId}")
	@ResponseStatus(HttpStatus.OK)
	@PreAuthorize("isProjectOwner(#projectId)")
	public ProjectDetail updateProject(@PathVariable Long projectId, @RequestBody @Valid UpdateProjectParam updateProjectParam) {
		Project project = projectService.update(projectId, updateProjectParam);
		ProjectDetail projectDetail = modelMapper.map(project, ProjectDetail.class);
		return projectDetail;
	}

	@PostMapping("/projects/{projectId}/members")
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
