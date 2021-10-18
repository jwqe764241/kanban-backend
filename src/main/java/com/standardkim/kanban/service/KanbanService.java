package com.standardkim.kanban.service;

import java.util.List;
import java.util.NoSuchElementException;

import com.standardkim.kanban.dto.KanbanDto.CreateKanbanParam;
import com.standardkim.kanban.dto.KanbanDto.KanbanDetail;
import com.standardkim.kanban.dto.KanbanDto.UpdateKanbanParam;
import com.standardkim.kanban.entity.Kanban;
import com.standardkim.kanban.entity.KanbanSequence;
import com.standardkim.kanban.entity.Project;
import com.standardkim.kanban.exception.ErrorCode;
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
	public List<KanbanDetail> findKanbanDetailByProjectId(Long projectId) {
		List<KanbanSequence> kanbanSequences = kanbanSequenceRepository.findByProjectIdAndIsDeletedOrderBySequenceId(projectId, false);
		List<KanbanDetail> kanbanDetails = modelMapper.map(kanbanSequences, new TypeToken<List<KanbanDetail>>(){}.getType());
		return kanbanDetails;
	}

	@Transactional(readOnly = true)
	public KanbanDetail findKanbanDetailByProjectIdAndSequenceId(Long projectId, Long sequenceId) {
		KanbanSequence kanbanSequence = kanbanSequenceRepository.findByProjectIdAndSequenceId(projectId, sequenceId)
			.orElseThrow(() -> new ResourceNotFoundException("kanban not found", ErrorCode.COMMON_RESOURCE_NOT_FOUND));
		KanbanDetail kanbanDetail = modelMapper.map(kanbanSequence, KanbanDetail.class);
		return kanbanDetail;
	}

	@Transactional(rollbackFor = Exception.class)
	public KanbanDetail create(Long projectId, CreateKanbanParam createKanbanParam) {
		Project project = projectService.findById(projectId);
		Kanban kanban = createKanbanParam.toEntity(project);
		kanbanRepository.save(kanban);

		KanbanSequence kanbanSequence = kanbanSequenceRepository.findById(kanban.getId())
			.orElseThrow(() -> new ResourceNotFoundException("kanban not found", ErrorCode.COMMON_RESOURCE_NOT_FOUND));
		KanbanDetail kanbanDetail = modelMapper.map(kanbanSequence, KanbanDetail.class);
		return kanbanDetail;
	}

	@Transactional(rollbackFor = Exception.class)
	public void update(Long projectId, Long sequenceId, UpdateKanbanParam updateKanbanParam) {
		Kanban kanban = kanbanRepository.findByProjectIdAndSequenceId(projectId, sequenceId)
			.orElseThrow(() -> new ResourceNotFoundException("kanban not found", ErrorCode.COMMON_RESOURCE_NOT_FOUND));
		kanban.updateKanban(updateKanbanParam);
	}

	@Transactional(rollbackFor = Exception.class)
	public void delete(Long projectId, Long sequenceId) {
		try {
			Kanban kanban = kanbanRepository.findByProjectIdAndSequenceId(projectId, sequenceId).get();
			kanban.updateToDeleted();
		}
		catch (NoSuchElementException e) {
			return;
		}
	}
}