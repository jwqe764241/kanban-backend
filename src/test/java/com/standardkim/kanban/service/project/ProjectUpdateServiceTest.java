package com.standardkim.kanban.service.project;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import com.standardkim.kanban.domain.project.application.ProjectFindService;
import com.standardkim.kanban.domain.project.application.ProjectUpdateService;
import com.standardkim.kanban.domain.project.domain.Project;
import com.standardkim.kanban.domain.project.dto.UpdateProjectParam;
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
	void update_ProjectNameIsNotExist_UpdatedProject() {
		given(projectFindService.isNameExist("updated")).willReturn(false);
		given(projectFindService.findById(1L)).willReturn(getProject(1L, "example", "example"));

		Project updatedProject = projectUpdateService.update(1L, getUpdateProjectParam("updated"));

		assertThat(updatedProject.getName()).isEqualTo("updated");
	}

	@Test
	void update_ProjectNameIsExist_ThrowDuplicateProjectNameException() {
		given(projectFindService.isNameExist("updated")).willReturn(true);

		assertThatThrownBy(() -> {
			projectUpdateService.update(1L, getUpdateProjectParam("updated"));
		}).isInstanceOf(DuplicateProjectNameException.class);
	}

	private Project getProject(Long id, String name, String description) {
		return Project.builder()
			.id(id)
			.name(name)
			.description(description)
			.build();
	}

	private UpdateProjectParam getUpdateProjectParam(String name) {
		return UpdateProjectParam.builder()
			.name(name)
			.build();
	}
}
