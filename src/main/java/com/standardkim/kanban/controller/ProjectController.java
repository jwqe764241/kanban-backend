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
import org.springframework.security.core.Authentication;
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

	@GetMapping("/projects/{id}")
	@ResponseStatus(HttpStatus.OK)
	@PreAuthorize("isProjectMember(#id)")
	public ProjectInfo getProject(@PathVariable Long id, Authentication authentication) {
		return projectService.getProjectInfoById(id);
	}

	@GetMapping("/projects/{id}/members")
	@ResponseStatus(HttpStatus.OK)
	@PreAuthorize("isProjectMember(#id)")
	public List<ProjectMemberInfo> getProjectMember(@PathVariable Long id) {
		return projectMemberService.getProjectMembersById(id);
	}

	@GetMapping("/projects/{id}/members/suggestions")
	@ResponseStatus(HttpStatus.OK)
	@PreAuthorize("isProjectOwner(#id)")
	public List<SuggestionUserInfo> getProjectMemberSuggestions(@PathVariable Long id, @RequestParam("q") String query) {
		return projectMemberService.getUserSuggestions(id, query);
	}

	@PostMapping("/projects/{id}/members")
	@ResponseStatus(HttpStatus.OK)
	@PreAuthorize("isProjectOwner(#id)")
	public InvitedUserInfo inviteProjectMember(@PathVariable Long id, @RequestBody @Valid InviteProjectMemeberRequest request) {
		return projectInvitationService.inviteUser(id, request.getUserId());
	}

	@DeleteMapping("/projects/{id}/members/{userId}")
	@ResponseStatus(HttpStatus.OK)
	@PreAuthorize("isProjectOwner(#id)")
	public void removeProjectMember(@PathVariable Long id, @PathVariable Long userId) {
		projectMemberService.deleteProjectMember(id, userId);
	}

	@PostMapping("/projects/{id}/invitation")
	@ResponseStatus(HttpStatus.OK)
	public void acceptInvitation(@PathVariable Long id) {
		projectInvitationService.acceptInvite(id);
	}

	@GetMapping("/projects/{id}/invitations")
	@ResponseStatus(HttpStatus.OK)
	@PreAuthorize("isProjectOwner(#id)")
	public List<InvitedUserInfo> getInvitations(@PathVariable Long id) {
		return projectInvitationService.getInvitedUsers(id);
	}

	@DeleteMapping("/projects/{id}/invitations/{userId}")
	@ResponseStatus(HttpStatus.OK)
	@PreAuthorize("isProjectOwner(#id)")
	public void removeInvitation(@PathVariable Long id, @PathVariable Long userId) {
		projectInvitationService.deleteInvitation(id, userId);
	}
}
