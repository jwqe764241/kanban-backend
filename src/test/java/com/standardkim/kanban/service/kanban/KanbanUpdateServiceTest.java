package com.standardkim.kanban.service.kanban;

import com.standardkim.kanban.domain.kanban.application.KanbanFindService;
import com.standardkim.kanban.domain.kanban.application.KanbanUpdateService;
import com.standardkim.kanban.domain.kanban.domain.Kanban;
import com.standardkim.kanban.domain.kanban.dto.UpdateKanbanDescriptionParam;
import com.standardkim.kanban.domain.kanban.dto.UpdateKanbanNameParam;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class KanbanUpdateServiceTest {
	@Mock
	private KanbanFindService kanbanFindService;

	@InjectMocks
	private KanbanUpdateService kanbanUpdateService;

	@Test
	void update_KanbanIsExist_UpdateKanbanName() {
		given(kanbanFindService.findByProjectIdAndSequenceId(1L, 1L)).willReturn(getKanban("example", "example"));
	
		Kanban kanban = kanbanUpdateService.updateName(1L, 1L, getUpdateKanbanNameParam("updated"));

		assertThat(kanban.getName()).isEqualTo("updated");
	}

	@Test
	void update_KanbanIsExist_UpdateKanbanDescription() {
		given(kanbanFindService.findByProjectIdAndSequenceId(1L, 1L)).willReturn(getKanban("example", "example"));
	
		Kanban kanban = kanbanUpdateService.updateDescription(1L, 1L, getUpdateKanbanDescriptionParam("updated"));

		assertThat(kanban.getDescription()).isEqualTo("updated");
	}

	private UpdateKanbanNameParam getUpdateKanbanNameParam(String name) {
		return UpdateKanbanNameParam.builder()
			.name(name)
			.build();
	}

	private UpdateKanbanDescriptionParam getUpdateKanbanDescriptionParam(String description) {
		return UpdateKanbanDescriptionParam.builder()
			.description(description)
			.build();
	}

	private Kanban getKanban(String name, String description) {
		return Kanban.builder()
			.name(name)
			.description(description)
			.build();
	}
}
