package com.standardkim.kanban.domain.project.api;

import java.util.List;

import javax.validation.Valid;

import com.standardkim.kanban.domain.auth.dto.SecurityUser;
import com.standardkim.kanban.domain.project.application.ProjectService;
import com.standardkim.kanban.domain.project.domain.Project;
import com.standardkim.kanban.domain.project.dto.CreateProjectParam;
import com.standardkim.kanban.domain.project.dto.ProjectDetail;
import com.standardkim.kanban.domain.project.dto.UpdateProjectParam;
import com.standardkim.kanban.global.util.SecurityContextFacade;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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
public class ProjectApi {
	private final ProjectService projectService;

	private final ModelMapper modelMapper;

	@PostMapping("/projects")
	@ResponseStatus(HttpStatus.CREATED)
	public void createProject(@RequestBody @Valid CreateProjectParam createProjectParam) {
		SecurityUser securityUser = SecurityContextFacade.getSecurityUser();
		projectService.create(securityUser.getId(), createProjectParam);
	}

	@GetMapping("/projects")
	@ResponseStatus(HttpStatus.OK)
	public List<ProjectDetail> getMyProject() {
		SecurityUser securityUser = SecurityContextFacade.getSecurityUser();
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
}
