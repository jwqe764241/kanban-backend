package com.standardkim.kanban.service.kanban;

import com.standardkim.kanban.domain.kanban.application.KanbanSequenceFindService;
import com.standardkim.kanban.domain.kanban.dao.KanbanSequenceRepository;
import com.standardkim.kanban.domain.kanban.domain.KanbanSequence;
import com.standardkim.kanban.domain.kanban.exception.KanbanNotFoundException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class KanbanSequenceFindServiceTest {
	@Mock
	KanbanSequenceRepository kanbanSequenceRepository;

	@InjectMocks
	KanbanSequenceFindService kanbanSequenceFindService;

	@Test
	void findByProjectId_KanbanSequenceIsExist_ListOfKanbanSequence() {
		given(kanbanSequenceRepository.findByProjectIdAndIsDeletedOrderBySequenceId(1L, false)).willReturn(getKanbanSequenceList(1L, 3));
		
		List<KanbanSequence> kanbanSequences = kanbanSequenceFindService.findByProjectId(1L);
		
		assertThat(kanbanSequences).hasSize(3);
	}

	@Test
	void findByProjectId_KanbanSequenceIsNotExist_EmptyList() {
		given(kanbanSequenceRepository.findByProjectIdAndIsDeletedOrderBySequenceId(1L, false)).willReturn(new ArrayList<>());
	
		List<KanbanSequence> kanbanSequences = kanbanSequenceFindService.findByProjectId(1L);

		assertThat(kanbanSequences).isEmpty();
	}

	@Test
	void findByProjectIdAndSequenceId_KanbanSequenceIsExist_KanbanSequence() {
		given(kanbanSequenceRepository.findByProjectIdAndSequenceId(1L, 1L)).willReturn(Optional.of(getKanbanSequence(1L, 1L, 1L)));

		KanbanSequence kanbanSequence = kanbanSequenceFindService.findByProjectIdAndSequenceId(1L, 1L);

		assertThat(kanbanSequence).isNotNull();
	}

	@Test
	void findByProjectIdAndSequenceId_KanbanSequenceIsNotExist_ThrowKanbanNotFoundException() {
		given(kanbanSequenceRepository.findByProjectIdAndSequenceId(1L, 1L)).willReturn(Optional.empty());

		assertThatThrownBy(() -> {
			kanbanSequenceFindService.findByProjectIdAndSequenceId(1L, 1L);
		}).isInstanceOf(KanbanNotFoundException.class);
	}

	@Test
	void findById_KanbanSequenceIsExist_KanbanSequence() {
		given(kanbanSequenceRepository.findById(1L)).willReturn(Optional.of(getKanbanSequence(1L, 1L, 1L)));

		KanbanSequence kanbanSequence = kanbanSequenceFindService.findById(1L);

		assertThat(kanbanSequence).isNotNull();
	}

	@Test
	void findById_KanbanSequenceIsNotExist_ThrowKanbanNotFoundException() {
		given(kanbanSequenceRepository.findById(1L)).willReturn(Optional.empty());

		assertThatThrownBy(() -> {
			kanbanSequenceFindService.findById(1L);
		}).isInstanceOf(KanbanNotFoundException.class);
	}

	private KanbanSequence getKanbanSequence(Long projectId, Long kanbanId, Long sequenceId) {
		return KanbanSequence.builder()
			.id(kanbanId)
			.sequenceId(sequenceId)
			.projectId(projectId)
			.build();
	}

	private List<KanbanSequence> getKanbanSequenceList(Long projectId, int size) {
		List<KanbanSequence> list = new ArrayList<>(size);
		for(int i = 1; i <= size; ++i) {
			list.add(getKanbanSequence(projectId, Long.valueOf(i), Long.valueOf(i)));
		}
		return list;
	}
}
