package com.standardkim.kanban.service.kanban;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import com.standardkim.kanban.domain.kanban.application.KanbanCreateService;
import com.standardkim.kanban.domain.kanban.dao.KanbanRepository;
import com.standardkim.kanban.domain.kanban.domain.Kanban;
import com.standardkim.kanban.domain.kanban.dto.CreateKanbanParam;
import com.standardkim.kanban.domain.project.application.ProjectFindService;
import com.standardkim.kanban.domain.project.domain.Project;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class KanbanCreateServiceTest {
	@Mock
	private ProjectFindService projectFindService;
	
	@Mock
	private KanbanRepository kanbanRepository;

	@InjectMocks
	private KanbanCreateService kanbanCreateService;

	@Test
	void create_ProjectIsExist_Save() {
		given(projectFindService.findById(1L)).willReturn(getProject("example", "example", 1L));

		kanbanCreateService.create(1L, getCreateKanbanParam("example", "example"));

		verify(kanbanRepository).save(any(Kanban.class));
	}

	private CreateKanbanParam getCreateKanbanParam(String name, String description) {
		return CreateKanbanParam.builder()
			.name(name)
			.description(description)
			.build();
	}

	private Project getProject(String name, String description, Long projectId) {
		return Project.builder()
			.name(name)
			.description(description)
			.id(projectId)
			.build();
	}
}
