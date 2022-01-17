package com.standardkim.kanban.service;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import com.standardkim.kanban.domain.kanban.application.KanbanService;
import com.standardkim.kanban.domain.kanban.dao.KanbanRepository;
import com.standardkim.kanban.domain.kanban.domain.Kanban;
import com.standardkim.kanban.domain.kanban.dto.CreateKanbanParam;
import com.standardkim.kanban.domain.kanban.dto.UpdateKanbanParam;
import com.standardkim.kanban.domain.kanban.exception.KanbanNotFoundException;
import com.standardkim.kanban.domain.project.application.ProjectFindService;
import com.standardkim.kanban.domain.project.domain.Project;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class KanbanServiceTest {
	@Mock
	KanbanRepository kanbanRepository;

	@Mock
	ProjectFindService projectFindService;

	@InjectMocks
	KanbanService kanbanService;

	@Test
	void findByProjectIdAndSequenceId_KanbanIsNotExist_ThrowKanbanNotFoundException() {
		given(kanbanRepository.findByProjectIdAndSequenceId(1L, 1L)).willReturn(Optional.empty());

		assertThatThrownBy(() -> {
			kanbanService.findByProjectIdAndSequenceId(1L, 1L);
		}).isInstanceOf(KanbanNotFoundException.class);
	}

	@Test
	void create_KanabanNameIsExist_Save() {
		given(projectFindService.findById(1L)).willReturn(getProject(1L));

		kanbanService.create(1L, getCreateKanbanParam());

		verify(kanbanRepository).save(any(Kanban.class));
	}

	@Test
	void update_KanbanIsNotExist_ThrowKanbanNotFoundException() {
		given(kanbanRepository.findByProjectIdAndSequenceId(1L, 1L)).willReturn(Optional.empty());
	
		assertThatThrownBy(() -> {
			kanbanService.update(1L, 1L, getUpdateKanbanParam());
		}).isInstanceOf(KanbanNotFoundException.class);
	}

	@Test
	void delete_KanbanIsNotExist_DoesNotThrowAnyException() {
		given(kanbanRepository.findByProjectIdAndSequenceId(1L, 1L)).willReturn(Optional.empty());

		assertThatCode(() -> {
			kanbanService.delete(1L, 1L);
		}).doesNotThrowAnyException();
	}

	private CreateKanbanParam getCreateKanbanParam() {
		return CreateKanbanParam.builder()
			.name("example")
			.description("example")
			.build();
	}

	private UpdateKanbanParam getUpdateKanbanParam() {
		return UpdateKanbanParam.builder()
			.name("example")
			.description("example")
			.build();
	}

	private Project getProject(Long projectId) {
		return Project.builder()
			.id(projectId)
			.build();
	}
}
