package com.standardkim.kanban.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import com.standardkim.kanban.dto.ProjectDto.NewProjectRequest;
import com.standardkim.kanban.dto.ProjectDto.ProjectInfo;
import com.standardkim.kanban.dto.ProjectInvitationDto.InviteProjectMemeberRequest;
import com.standardkim.kanban.dto.ProjectInvitationDto.InvitedUserInfo;
import com.standardkim.kanban.dto.ProjectMemberDto.ProjectMemberInfo;
import com.standardkim.kanban.dto.UserDto.SuggestionUserInfo;
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
	public void createProject(@RequestBody @Valid NewProjectRequest newProjectRequest) {
		projectService.createProject(newProjectRequest.getName(), newProjectRequest.getDescription());
	}

	@GetMapping("/projects")
	@ResponseStatus(HttpStatus.OK)
	public ArrayList<ProjectInfo> getMyProject() {
		return projectService.getMyProjects();
	}

	@GetMapping("/projects/{projectId}")
	@ResponseStatus(HttpStatus.OK)
	@PreAuthorize("isProjectMember(#projectId)")
	public ProjectInfo getProject(@PathVariable Long projectId) {
		return projectService.getProjectInfoById(projectId);
	}

	@GetMapping("/projects/{projectId}/members")
	@ResponseStatus(HttpStatus.OK)
	@PreAuthorize("isProjectMember(#projectId)")
	public List<ProjectMemberInfo> getProjectMember(@PathVariable Long projectId) {
		return projectMemberService.getProjectMembersById(projectId);
	}

	@GetMapping("/projects/{projectId}/members/suggestions")
	@ResponseStatus(HttpStatus.OK)
	@PreAuthorize("isProjectOwner(#projectId)")
	public List<SuggestionUserInfo> getProjectMemberSuggestions(@PathVariable Long projectId, @RequestParam("q") String query) {
		return projectMemberService.getUserSuggestions(projectId, query);
	}

	@PostMapping("/projects/{projectId}/members")
	@ResponseStatus(HttpStatus.OK)
	@PreAuthorize("isProjectOwner(#projectId)")
	public InvitedUserInfo inviteProjectMember(@PathVariable Long projectId, @RequestBody @Valid InviteProjectMemeberRequest request) {
		return projectInvitationService.inviteUser(projectId, request.getUserId());
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
	public List<InvitedUserInfo> getInvitations(@PathVariable Long projectId) {
		return projectInvitationService.getInvitedUsers(projectId);
	}

	@DeleteMapping("/projects/{projectId}/invitations/{userId}")
	@ResponseStatus(HttpStatus.OK)
	@PreAuthorize("isProjectOwner(#projectId)")
	public void removeInvitation(@PathVariable Long projectId, @PathVariable Long userId) {
		projectInvitationService.deleteInvitation(projectId, userId);
	}
}
