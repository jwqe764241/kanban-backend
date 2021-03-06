package com.standardkim.kanban.domain.project.api;

import java.util.List;

import javax.validation.Valid;

import com.standardkim.kanban.domain.project.application.ProjectCreateService;
import com.standardkim.kanban.domain.project.application.ProjectDeleteService;
import com.standardkim.kanban.domain.project.application.ProjectFindService;
import com.standardkim.kanban.domain.project.application.ProjectUpdateService;
import com.standardkim.kanban.domain.project.domain.Project;
import com.standardkim.kanban.domain.project.dto.CreateProjectParam;
import com.standardkim.kanban.domain.project.dto.ProjectDetail;
import com.standardkim.kanban.domain.project.dto.UpdateProjectDescriptionParam;
import com.standardkim.kanban.domain.project.dto.UpdateProjectNameParam;
import com.standardkim.kanban.global.auth.dto.SecurityUser;
import com.standardkim.kanban.global.util.SecurityContextFacade;

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
public class ProjectApi {
	private final ProjectFindService projectFindService;

	private final ProjectCreateService projectCreateService;

	private final ProjectUpdateService projectUpdateService;

	private final ProjectDeleteService projectDeleteService;

	private final ModelMapper modelMapper;

	@GetMapping("/projects")
	@ResponseStatus(HttpStatus.OK)
	public List<ProjectDetail> getMyProject() {
		SecurityUser securityUser = SecurityContextFacade.getSecurityUser();
		List<Project> projects = projectFindService.findByUserId(securityUser.getId());
		List<ProjectDetail> projectDetails = modelMapper.map(projects, new TypeToken<List<ProjectDetail>>(){}.getType());
		return projectDetails;
	}

	@GetMapping("/projects/{projectId}")
	@ResponseStatus(HttpStatus.OK)
	@PreAuthorize("hasProjectRole(#projectId, 'MEMBER')")
	public ProjectDetail getProject(@PathVariable Long projectId) {
		Project project = projectFindService.findById(projectId);
		ProjectDetail projectDetail = modelMapper.map(project, ProjectDetail.class);
		return projectDetail;
	}

	@PostMapping("/projects")
	@ResponseStatus(HttpStatus.CREATED)
	public ProjectDetail createProject(@RequestBody @Valid CreateProjectParam createProjectParam) {
		SecurityUser securityUser = SecurityContextFacade.getSecurityUser();
		Project project = projectCreateService.create(securityUser.getId(), createProjectParam);
		ProjectDetail projectDetail = modelMapper.map(project, ProjectDetail.class);
		return projectDetail;
	}

	@PatchMapping("/projects/{projectId}/name")
	@ResponseStatus(HttpStatus.OK)
	@PreAuthorize("hasProjectRole(#projectId, 'ADMIN')")
	public ProjectDetail updateProjectName(@PathVariable Long projectId, 
		@RequestBody @Valid UpdateProjectNameParam updateProjectNameParam) {
		Project project = projectUpdateService.updateName(projectId, updateProjectNameParam);
		ProjectDetail projectDetail = modelMapper.map(project, ProjectDetail.class);
		return projectDetail;
	}

	@PatchMapping("/projects/{projectId}/description")
	@ResponseStatus(HttpStatus.OK)
	@PreAuthorize("hasProjectRole(#projectId, 'ADMIN')")
	public ProjectDetail updateProjectDescription(@PathVariable Long projectId, 
		@RequestBody @Valid UpdateProjectDescriptionParam updateProjectDescriptionParam) {
		Project project = projectUpdateService.updateDescription(projectId, updateProjectDescriptionParam);
		ProjectDetail projectDetail = modelMapper.map(project, ProjectDetail.class);
		return projectDetail;
	}

	@DeleteMapping("/projects/{projectId}")
	@ResponseStatus(HttpStatus.OK)
	@PreAuthorize("hasProjectRole(#projectId, 'ADMIN')")
	public void removeProject(@PathVariable Long projectId) {
		projectDeleteService.delete(projectId);
	}
}
