package com.standardkim.kanban.service.kanban;

import com.standardkim.kanban.domain.kanban.application.KanbanDeleteService;
import com.standardkim.kanban.domain.kanban.application.KanbanFindService;
import com.standardkim.kanban.domain.kanban.domain.Kanban;
import com.standardkim.kanban.domain.kanban.exception.KanbanNotFoundException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class KanbanDeleteServiceTest {
	@Mock
	private KanbanFindService kanbanFindService;

	@InjectMocks
	private KanbanDeleteService kanbanDeleteService;

	@Test
	void delete_KanbanIsNotExist_DoesNotThrowAnyException() {
		given(kanbanFindService.findByProjectIdAndSequenceId(1L, 1L)).willThrow(new KanbanNotFoundException("not found"));

		assertThatCode(() -> {
			kanbanDeleteService.delete(1L, 1L);
		}).doesNotThrowAnyException();
	}

	@Test
	void delete_KanbanIsExist_UpdateIsDeletedToTrue() {
		Kanban kanban = getKanban(false);
		given(kanbanFindService.findByProjectIdAndSequenceId(1L, 1L)).willReturn(kanban);

		kanbanDeleteService.delete(1L, 1L);

		assertThat(kanban.isDeleted()).isTrue();
	}

	private Kanban getKanban(boolean isDeleted) {
		return Kanban.builder()
			.isDeleted(isDeleted)
			.build();
	}
}
