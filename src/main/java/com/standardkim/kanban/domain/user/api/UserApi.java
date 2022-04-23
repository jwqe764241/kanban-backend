package com.standardkim.kanban.domain.user.api;

import java.util.List;

import javax.validation.Valid;

import com.standardkim.kanban.domain.user.application.UserCreateService;
import com.standardkim.kanban.domain.user.application.UserFindService;
import com.standardkim.kanban.domain.user.domain.User;
import com.standardkim.kanban.domain.user.dto.CreateUserParam;
import com.standardkim.kanban.domain.user.dto.SuggestionUserDetail;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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
public class UserApi {
	private final UserFindService userFindService;
	
	private final UserCreateService userCreateService;

	private final ModelMapper modelMapper;

	@GetMapping("/projects/{projectId}/members/suggestions")
	@ResponseStatus(HttpStatus.OK)
	@PreAuthorize("hasProjectRole(#projectId, 'MANAGER')")
	public List<SuggestionUserDetail> getProjectMemberSuggestions(@PathVariable Long projectId, @RequestParam("q") String query) {
		List<User> users = userFindService.findNotMemberOrNotInvitedUser(projectId, query);
		List<SuggestionUserDetail> suggestionUserDetails = modelMapper.map(users, new TypeToken<List<SuggestionUserDetail>>(){}.getType());
		return suggestionUserDetails;
	}

	@PostMapping("/users")
	@ResponseStatus(HttpStatus.CREATED)
	public void join(@RequestBody @Valid CreateUserParam createUserParam) {
		userCreateService.create(createUserParam);
	}
}
