package com.standardkim.kanban.domain.projectmember.api;

import java.util.List;

import com.standardkim.kanban.domain.auth.dto.AuthenticationDto.SecurityUser;
import com.standardkim.kanban.domain.projectmember.application.ProjectMemberService;
import com.standardkim.kanban.domain.projectmember.domain.ProjectMember;
import com.standardkim.kanban.domain.projectmember.dto.ProjectMemberDto.ProjectMemberDetail;
import com.standardkim.kanban.domain.user.application.UserService;
import com.standardkim.kanban.domain.user.domain.User;
import com.standardkim.kanban.domain.user.dto.UserDto.SuggestionUserDetail;
import com.standardkim.kanban.global.util.SecurityContextFacade;

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
public class ProjectMemberApi {
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

	@PostMapping("/projects/{projectId}/members/accept-invitation")
	@ResponseStatus(HttpStatus.OK)
	public void acceptInvitation(@PathVariable Long projectId) {
		SecurityUser securityUser = SecurityContextFacade.getSecurityUser();
		projectMemberService.accept(projectId, securityUser.getId());
	}
}
