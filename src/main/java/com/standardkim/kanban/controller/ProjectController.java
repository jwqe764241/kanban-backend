package com.standardkim.kanban.controller;

import java.util.List;

import javax.validation.Valid;

import com.standardkim.kanban.dto.ProjectDto.CreateProjectParam;
import com.standardkim.kanban.dto.ProjectDto.ProjectDetail;
import com.standardkim.kanban.dto.ProjectInvitationDto.InviteProjectMemeberParam;
import com.standardkim.kanban.dto.ProjectInvitationDto.InvitedUserDetail;
import com.standardkim.kanban.dto.ProjectMemberDto.ProjectMemberDetail;
import com.standardkim.kanban.dto.UserDto.SuggestionUserDetail;
import com.standardkim.kanban.service.ProjectInvitationService;
import com.standardkim.kanban.service.ProjectMemberService;
import com.standardkim.kanban.service.ProjectService;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ProjectController {
	private final ProjectService projectService;

	private final ProjectMemberService projectMemberService;

	private final ProjectInvitationService projectInvitationService;

	@PostMapping("/projects")
	@ResponseStatus(HttpStatus.CREATED)
	public void createProject(@RequestBody @Valid CreateProjectParam createProjectParam) {
		projectService.createProject(createProjectParam);
	}

	@GetMapping("/projects")
	@ResponseStatus(HttpStatus.OK)
	public List<ProjectDetail> getMyProject() {
		return projectService.getMyProjectDetails();
	}

	@GetMapping("/projects/{projectId}")
	@ResponseStatus(HttpStatus.OK)
	@PreAuthorize("isProjectMember(#projectId)")
	public ProjectDetail getProject(@PathVariable Long projectId) {
		return projectService.getProjectDetailById(projectId);
	}

	@GetMapping("/projects/{projectId}/members")
	@ResponseStatus(HttpStatus.OK)
	@PreAuthorize("isProjectMember(#projectId)")
	public List<ProjectMemberDetail> getProjectMember(@PathVariable Long projectId) {
		return projectMemberService.getProjectMembersById(projectId);
	}

	@GetMapping("/projects/{projectId}/members/suggestions")
	@ResponseStatus(HttpStatus.OK)
	@PreAuthorize("isProjectOwner(#projectId)")
	public List<SuggestionUserDetail> getProjectMemberSuggestions(@PathVariable Long projectId, @RequestParam("q") String query) {
		return projectMemberService.getUserSuggestions(projectId, query);
	}

	@PostMapping("/projects/{projectId}/members")
	@ResponseStatus(HttpStatus.OK)
	@PreAuthorize("isProjectOwner(#projectId)")
	public InvitedUserDetail inviteProjectMember(@PathVariable Long projectId, @RequestBody @Valid InviteProjectMemeberParam inviteProjectMemeberParam) {
		return projectInvitationService.inviteUser(projectId, inviteProjectMemeberParam.getUserId());
	}

	@DeleteMapping("/projects/{projectId}/members/{userId}")
	@ResponseStatus(HttpStatus.OK)
	@PreAuthorize("isProjectOwner(#projectId)")
	public void removeProjectMember(@PathVariable Long projectId, @PathVariable Long userId) {
		projectMemberService.deleteProjectMember(projectId, userId);
	}

	@PostMapping("/projects/{projectId}/invitation")
	@ResponseStatus(HttpStatus.OK)
	public void acceptInvitation(@PathVariable Long projectId) {
		projectInvitationService.acceptInvite(projectId);
	}

	@GetMapping("/projects/{projectId}/invitations")
	@ResponseStatus(HttpStatus.OK)
	@PreAuthorize("isProjectOwner(#projectId)")
	public List<InvitedUserDetail> getInvitations(@PathVariable Long projectId) {
		return projectInvitationService.getInvitedUsers(projectId);
	}

	@DeleteMapping("/projects/{projectId}/invitations/{userId}")
	@ResponseStatus(HttpStatus.OK)
	@PreAuthorize("isProjectOwner(#projectId)")
	public void removeInvitation(@PathVariable Long projectId, @PathVariable Long userId) {
		projectInvitationService.deleteInvitation(projectId, userId);
	}
}
