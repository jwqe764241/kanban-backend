package com.standardkim.kanban.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.standardkim.kanban.dto.KanbanDto.CreateKanbanParam;
import com.standardkim.kanban.dto.KanbanDto.UpdateKanbanParam;
import com.standardkim.kanban.entity.Kanban;
import com.standardkim.kanban.entity.KanbanSequence;
import com.standardkim.kanban.entity.Project;
import com.standardkim.kanban.exception.kanban.KanbanNotFoundException;
import com.standardkim.kanban.repository.KanbanRepository;
import com.standardkim.kanban.repository.KanbanSequenceRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration.AccessLevel;

import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class KanbanServiceTest {
	@Mock
	KanbanRepository kanbanRepository;

	@Mock
	KanbanSequenceRepository kanbanSequenceRepository;

	@Mock
	ProjectService projectService;

	@Spy
	ModelMapper modelMapper = new ModelMapper();

	@InjectMocks
	KanbanService kanbanService;

	@BeforeEach
	void init() {
		modelMapper.getConfiguration()
			.setFieldAccessLevel(AccessLevel.PRIVATE)
			.setFieldMatchingEnabled(true);
	}

	@Test
	void findByProjectId_KanbanSequenceIsExist_ListOfKanbanSequence() {
		given(kanbanSequenceRepository.findByProjectIdAndIsDeletedOrderBySequenceId(1L, false)).willReturn(getKanbanSequenceList(1L, 3));
		
		List<KanbanSequence> kanbanSequences = kanbanService.findByProjectId(1L);
		
		assertThat(kanbanSequences).hasSize(3);
	}

	@Test
	void findByProjectId_KanbanSequenceIsNotExist_EmptyList() {
		given(kanbanSequenceRepository.findByProjectIdAndIsDeletedOrderBySequenceId(1L, false)).willReturn(new ArrayList<>());
	
		List<KanbanSequence> kanbanSequences = kanbanService.findByProjectId(1L);

		assertThat(kanbanSequences).isEmpty();
	}

	@Test
	void findByProjectIdAndSequenceId_KanbanSequenceIsExist_KanbanSequence() {
		given(kanbanSequenceRepository.findByProjectIdAndSequenceId(1L, 1L)).willReturn(Optional.of(getKanbanSequence(1L, 1L, 1L)));

		KanbanSequence kanbanSequence = kanbanService.findByProjectIdAndSequenceId(1L, 1L);

		assertThat(kanbanSequence).isNotNull();
	}

	@Test
	void findByProjectIdAndSequenceId_KanbanSequenceIsNotExist_ThrowKanbanNotFoundException() {
		given(kanbanSequenceRepository.findByProjectIdAndSequenceId(1L, 1L)).willReturn(Optional.empty());

		assertThatThrownBy(() -> {
			kanbanService.findByProjectIdAndSequenceId(1L, 1L);
		}).isInstanceOf(KanbanNotFoundException.class);
	}

	@Test
	void findById_KanbanSequenceIsExist_KanbanSequence() {
		given(kanbanSequenceRepository.findById(1L)).willReturn(Optional.of(getKanbanSequence(1L, 1L, 1L)));

		KanbanSequence kanbanSequence = kanbanService.findById(1L);

		assertThat(kanbanSequence).isNotNull();
	}

	@Test
	void findById_KanbanSequenceIsNotExist_ThrowKanbanNotFoundException() {
		given(kanbanSequenceRepository.findById(1L)).willReturn(Optional.empty());

		assertThatThrownBy(() -> {
			kanbanService.findById(1L);
		}).isInstanceOf(KanbanNotFoundException.class);
	}

	@Test
	void create_KanabanSequenceIsNotExist_Save() {
		given(projectService.findById(1L)).willReturn(getProject(1L));

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

	private KanbanSequence getKanbanSequence(Long projectId, Long kanbanId, Long sequenceId) {
		return KanbanSequence.builder()
			.id(kanbanId)
			.sequenceId(sequenceId)
			.projectId(projectId)
			.build();
	}

	private Project getProject(Long projectId) {
		return Project.builder()
			.id(projectId)
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
