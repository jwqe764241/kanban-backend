package com.standardkim.kanban.service;

import java.util.List;
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
import org.modelmapper.TypeToken;
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
	public KanbanSequence getKanbanSequenceByKanbanId(Long kanbanId) {
		Optional<KanbanSequence> kanbanSequence = kanbanSequenceRepository.findById(kanbanId);
		return kanbanSequence.orElseThrow(() -> new ResourceNotFoundException("resource not found"));
	}

	@Transactional(readOnly = true)
	public KanbanSequence getKanbanSequenceBySequenceId(Long projectId, Long sequenceId) {
		Optional<KanbanSequence> kanbanSequence = kanbanSequenceRepository.findByProjectIdAndSequenceId(projectId, sequenceId);
		return kanbanSequence.orElseThrow(() -> new ResourceNotFoundException("resource not found"));
	}

	@Transactional(readOnly = true)
	public List<KanbanSequence> getKanbanSequencesByProjectId(Long projectId) {
		Optional<List<KanbanSequence>> kanbanSequences = kanbanSequenceRepository.findByProjectIdAndIsDeletedOrderBySequenceId(projectId, false);
		return kanbanSequences.orElseThrow(() -> new ResourceNotFoundException("resource not found"));
	}

	@Transactional(readOnly = true)
	public List<KanbanInfoDto> getKanbanInfosByProjectId(Long projectId) {
		List<KanbanSequence> kanbanSequences = getKanbanSequencesByProjectId(projectId);
		List<KanbanInfoDto> kanbanInfos = modelMapper.map(kanbanSequences, new TypeToken<List<KanbanInfoDto>>(){}.getType());
		return kanbanInfos;
	}

	@Transactional(readOnly = true)
	public KanbanInfoDto getKanbanInfoBySequenceId(Long projectId, Long sequenceId) {
		KanbanSequence kanbanSequence = getKanbanSequenceBySequenceId(projectId, sequenceId);
		KanbanInfoDto kanbanInfo = modelMapper.map(kanbanSequence, KanbanInfoDto.class);
		return kanbanInfo;
	}

	@Transactional(readOnly = true)
	public Kanban getKanbanBySequenceId(Long projectId, Long sequenceId) {
		KanbanSequence kanbanSequence = getKanbanSequenceBySequenceId(projectId, sequenceId);
		Optional<Kanban> kanban = kanbanRepository.findById(kanbanSequence.getId());
		return kanban.orElseThrow(() -> new ResourceNotFoundException("resource not found"));
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

		KanbanSequence sequence = getKanbanSequenceByKanbanId(kanban.getId());
		KanbanInfoDto info = modelMapper.map(sequence, KanbanInfoDto.class);
		return info;
	}

	@Transactional(rollbackFor = Exception.class)
	public void deleteKanban(Long projectId, Long sequenceId) {
		try {
			Kanban kanban = getKanbanBySequenceId(projectId, sequenceId);
			kanban.updateToDeleted();
		}
		catch (ResourceNotFoundException e) {
			return;
		}
	}
}