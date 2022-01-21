package com.standardkim.kanban.service.kanban;

import java.util.Optional;

import com.standardkim.kanban.domain.kanban.application.KanbanFindService;
import com.standardkim.kanban.domain.kanban.dao.KanbanRepository;
import com.standardkim.kanban.domain.kanban.exception.KanbanNotFoundException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class KanbanFindServiceTest {
	@Mock
	private KanbanRepository kanbanRepository;

	@InjectMocks
	private KanbanFindService kanbanFindService;

	@Test
	void findByProjectIdAndSequenceId_KanbanIsNotExist_ThrowKanbanNotFoundException() {
		given(kanbanRepository.findByProjectIdAndSequenceId(1L, 1L)).willReturn(Optional.empty());

		assertThatThrownBy(() -> {
			kanbanFindService.findByProjectIdAndSequenceId(1L, 1L);
		}).isInstanceOf(KanbanNotFoundException.class);
	}
}
