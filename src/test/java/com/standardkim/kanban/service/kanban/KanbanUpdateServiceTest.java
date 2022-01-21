package com.standardkim.kanban.service.kanban;

import com.standardkim.kanban.domain.kanban.application.KanbanFindService;
import com.standardkim.kanban.domain.kanban.application.KanbanUpdateService;
import com.standardkim.kanban.domain.kanban.domain.Kanban;
import com.standardkim.kanban.domain.kanban.dto.UpdateKanbanParam;

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
	void update_KanbanIsExist_UpdateKanban() {
		given(kanbanFindService.findByProjectIdAndSequenceId(1L, 1L)).willReturn(getKanban("example", "example"));
	
		Kanban kanban = kanbanUpdateService.update(1L, 1L, getUpdateKanbanParam("updated", "updated"));

		assertThat(kanban.getName()).isEqualTo("updated");
		assertThat(kanban.getDescription()).isEqualTo("updated");
	}

	private UpdateKanbanParam getUpdateKanbanParam(String name, String description) {
		return UpdateKanbanParam.builder()
			.name(name)
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
