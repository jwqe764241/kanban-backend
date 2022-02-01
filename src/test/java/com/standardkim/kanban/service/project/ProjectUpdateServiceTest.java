package com.standardkim.kanban.service.project;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import com.standardkim.kanban.domain.project.application.ProjectFindService;
import com.standardkim.kanban.domain.project.application.ProjectUpdateService;
import com.standardkim.kanban.domain.project.domain.Project;
import com.standardkim.kanban.domain.project.dto.UpdateProjectDescriptionParam;
import com.standardkim.kanban.domain.project.dto.UpdateProjectNameParam;
import com.standardkim.kanban.domain.project.exception.DuplicateProjectNameException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ProjectUpdateServiceTest {
	@Mock
	private ProjectFindService projectFindService;

	@InjectMocks
	private ProjectUpdateService projectUpdateService;

	@Test
	void updateName_ProjectNameIsNotExist_UpdatedProjectName() {
		given(projectFindService.isNameExist("updated")).willReturn(false);
		given(projectFindService.findById(1L)).willReturn(getProject(1L, "example", "example"));

		Project updatedProject = projectUpdateService.updateName(1L, getUpdateProjectNameParam("updated"));

		assertThat(updatedProject.getName()).isEqualTo("updated");
	}

	@Test
	void updateName_ProjectNameIsExist_ThrowDuplicateProjectNameException() {
		given(projectFindService.isNameExist("updated")).willReturn(true);

		assertThatThrownBy(() -> {
			projectUpdateService.updateName(1L, getUpdateProjectNameParam("updated"));
		}).isInstanceOf(DuplicateProjectNameException.class);
	}

	@Test
	void updateDescription_ProjectIsExist_UpdateProjectDescription() {
		given(projectFindService.findById(1L)).willReturn(getProject(1L, "example", "example"));

		Project updatedProject = projectUpdateService.updateDescription(1L, getUpdateProjectDescriptionParam("updated"));

		assertThat(updatedProject.getDescription()).isEqualTo("updated");
	}

	private Project getProject(Long id, String name, String description) {
		return Project.builder()
			.id(id)
			.name(name)
			.description(description)
			.build();
	}

	private UpdateProjectNameParam getUpdateProjectNameParam(String name) {
		return UpdateProjectNameParam.builder()
			.name(name)
			.build();
	}

	private UpdateProjectDescriptionParam getUpdateProjectDescriptionParam(String description) {
		return UpdateProjectDescriptionParam.builder()
			.description(description)
			.build();
	}
}
