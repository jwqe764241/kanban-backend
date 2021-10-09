package com.standardkim.kanban.service;

import java.util.List;
import java.util.Optional;

import com.standardkim.kanban.dto.KanbanDto.CreateKanbanParam;
import com.standardkim.kanban.dto.KanbanDto.KanbanDetail;
import com.standardkim.kanban.dto.KanbanDto.UpdateKanbanParam;
import com.standardkim.kanban.entity.Kanban;
import com.standardkim.kanban.entity.KanbanSequence;
import com.standardkim.kanban.entity.Project;
import com.standardkim.kanban.exception.ResourceNotFoundException;
import com.standardkim.kanban.repository.KanbanRepository;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KanbanService {
	private final KanbanRepository kanbanRepository;

	private final KanbanSequenceService kanbanSequenceService;

	private final ProjectService projectService;

	private final ModelMapper modelMapper;

	@Transactional(readOnly = true)
	public List<KanbanDetail> getKanbanDetailsByProjectId(Long projectId) {
		List<KanbanSequence> kanbanSequences = kanbanSequenceService.findByProjectIdAndNotDeleted(projectId);
		List<KanbanDetail> kanbanDetails = modelMapper.map(kanbanSequences, new TypeToken<List<KanbanDetail>>(){}.getType());
		return kanbanDetails;
	}

	@Transactional(readOnly = true)
	public KanbanDetail getKanbanDetailBySequenceId(Long projectId, Long sequenceId) {
		KanbanSequence kanbanSequence = kanbanSequenceService.findByProjectIdAndSequenceId(projectId, sequenceId);
		KanbanDetail kanbanDetail = modelMapper.map(kanbanSequence, KanbanDetail.class);
		return kanbanDetail;
	}

	@Transactional(readOnly = true)
	public Kanban getKanbanBySequenceId(Long projectId, Long sequenceId) {
		KanbanSequence kanbanSequence = kanbanSequenceService.findByProjectIdAndSequenceId(projectId, sequenceId);
		Optional<Kanban> kanban = kanbanRepository.findById(kanbanSequence.getId());
		return kanban.orElseThrow(() -> new ResourceNotFoundException("resource not found"));
	}

	@Transactional(rollbackFor = Exception.class)
	public KanbanDetail createKanban(Long projectId, CreateKanbanParam createKanbanParam) {
		Project project = projectService.getProjectById(projectId);
		Kanban kanban = Kanban.builder()
			.name(createKanbanParam.getName())
			.description(createKanbanParam.getDescription())
			.project(project)
			.build();
		kanbanRepository.save(kanban);

		KanbanSequence kanbanSequence = kanbanSequenceService.findById(kanban.getId());
		KanbanDetail kanbanDetail = modelMapper.map(kanbanSequence, KanbanDetail.class);
		return kanbanDetail;
	}

	@Transactional(rollbackFor = Exception.class)
	public void updateKanban(Long projectId, Long sequenceId, UpdateKanbanParam updateKanbanParam) {
		Kanban kanban = getKanbanBySequenceId(projectId, sequenceId);
		kanban.updateKanban(updateKanbanParam);
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