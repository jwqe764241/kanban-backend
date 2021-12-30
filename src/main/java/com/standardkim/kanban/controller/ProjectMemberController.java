package com.standardkim.kanban.controller;

import java.util.List;

import com.standardkim.kanban.dto.AuthenticationDto.SecurityUser;
import com.standardkim.kanban.dto.ProjectMemberDto.ProjectMemberDetail;
import com.standardkim.kanban.dto.UserDto.SuggestionUserDetail;
import com.standardkim.kanban.entity.ProjectMember;
import com.standardkim.kanban.entity.User;
import com.standardkim.kanban.service.ProjectMemberService;
import com.standardkim.kanban.service.UserService;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ProjectMemberController {
	private final ProjectMemberService projectMemberService;

	private final UserService userService;

	private final ModelMapper modelMapper;

	@GetMapping("/projects/{projectId}/members")
	@ResponseStatus(HttpStatus.OK)
	@PreAuthorize("isProjectMember(#projectId)")
	public List<ProjectMemberDetail> getProjectMember(@PathVariable Long projectId) {
		List<ProjectMember> projectMembers = projectMemberService.findByProjectId(projectId);
		List<ProjectMemberDetail> projectMemberDetails = modelMapper.map(projectMembers, new TypeToken<List<ProjectMemberDetail>>(){}.getType());
		return projectMemberDetails;
	}

	@GetMapping("/projects/{projectId}/members/suggestions")
	@ResponseStatus(HttpStatus.OK)
	@PreAuthorize("isProjectOwner(#projectId)")
	public List<SuggestionUserDetail> getProjectMemberSuggestions(@PathVariable Long projectId, @RequestParam("q") String query) {
		List<User> users = userService.findNotMemberOrNotInvitedUser(projectId, query);
		List<SuggestionUserDetail> suggestionUserDetails = modelMapper.map(users, new TypeToken<List<SuggestionUserDetail>>(){}.getType());
		return suggestionUserDetails;
	}

	@DeleteMapping("/projects/{projectId}/members/{userId}")
	@ResponseStatus(HttpStatus.OK)
	@PreAuthorize("isProjectOwner(#projectId)")
	public void removeProjectMember(@PathVariable Long projectId, @PathVariable Long userId) {
		projectMemberService.delete(projectId, userId);
	}

	@PostMapping("/projects/{projectId}/invitation")
	@ResponseStatus(HttpStatus.OK)
	public void acceptInvitation(@PathVariable Long projectId) {
		SecurityUser securityUser = userService.getSecurityUser();
		projectMemberService.accept(projectId, securityUser.getId());
	}
}
