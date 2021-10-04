package com.standardkim.kanban.service;

import java.util.Optional;

import com.standardkim.kanban.dto.KanbanDto.CreateKanbanDTO;
import com.standardkim.kanban.dto.KanbanDto.KanbanInfoDto;
import com.standardkim.kanban.entity.Kanban;
import com.standardkim.kanban.entity.KanbanSequence;
import com.standardkim.kanban.entity.Project;
import com.standardkim.kanban.exception.ResourceNotFoundException;
import com.standardkim.kanban.repository.KanbanRepository;
import com.standardkim.kanban.repository.KanbanSequenceRepository;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KanbanService {
	private final KanbanRepository kanbanRepository;

	private final KanbanSequenceRepository kanbanSequenceRepository;

	private final ProjectService projectService;

	private final ModelMapper modelMapper;

	@Transactional(readOnly = true)
	public KanbanSequence getKanbanSequence(Long kanbanId) {
		Optional<KanbanSequence> kanbanSequence = kanbanSequenceRepository.findById(kanbanId);
		return kanbanSequence.orElseThrow(() -> new ResourceNotFoundException("resource not found"));
	}

	@Transactional(rollbackFor = Exception.class)
	public KanbanInfoDto createKanban(Long projectId, CreateKanbanDTO createKanbanDto) {
		Project project = projectService.getProjectById(projectId);
		Kanban kanban = Kanban.builder()
			.name(createKanbanDto.getName())
			.description(createKanbanDto.getDescription())
			.project(project)
			.build();
		kanbanRepository.save(kanban);

		KanbanSequence sequence = getKanbanSequence(kanban.getId());
		KanbanInfoDto info = modelMapper.map(sequence, KanbanInfoDto.class);
		return info;
	}
}