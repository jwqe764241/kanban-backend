package com.standardkim.kanban.service.project;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.standardkim.kanban.domain.project.application.ProjectCreateService;
import com.standardkim.kanban.domain.project.application.ProjectFindService;
import com.standardkim.kanban.domain.project.dao.ProjectRepository;
import com.standardkim.kanban.domain.project.domain.Project;
import com.standardkim.kanban.domain.project.dto.CreateProjectParam;
import com.standardkim.kanban.domain.project.exception.DuplicateProjectNameException;
import com.standardkim.kanban.domain.user.application.UserFindService;
import com.standardkim.kanban.domain.user.domain.User;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ProjectCreateServiceTest {
	@Mock
	ProjectFindService projectFindService;

	@Mock
	UserFindService userFindService;

	@Mock
	ProjectRepository projectRepository;

	@InjectMocks
	ProjectCreateService projectCreateService;

	@Test
	void create_ProjectNameIsNotExist_Project() {
		given(projectFindService.isNameExist("example")).willReturn(false);
		given(userFindService.findById(1L)).willReturn(getUser(1L));
		given(projectRepository.save(any(Project.class))).willReturn(getProject(1L, "example", "example"));

		Project project = projectCreateService.create(1L, getCreateProjectParam("example", "example"));

		assertThat(project).isNotNull();
	}

	@Test
	void create_NameIsExist_ThrownDuplicateProjectNameException() {
		given(projectFindService.isNameExist("example")).willReturn(true);

		assertThatThrownBy(() -> {
			projectCreateService.create(1L, getCreateProjectParam("example"));
		}).isInstanceOf(DuplicateProjectNameException.class);
	}

	private CreateProjectParam getCreateProjectParam(String name) {
		return CreateProjectParam.builder()
			.name(name)
			.build();
	}

	private CreateProjectParam getCreateProjectParam(String name, String description) {
		return CreateProjectParam.builder()
			.name(name)
			.description(description)
			.build();
	}

	private Project getProject(Long id, String name, String description) {
		return Project.builder()
			.id(id)
			.name(name)
			.description(description)
			.build();
	}

	private User getUser(Long id) {
		return User.builder()
			.id(id)
			.build();
	}
}
