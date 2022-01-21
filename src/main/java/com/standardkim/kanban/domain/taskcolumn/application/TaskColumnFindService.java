package com.standardkim.kanban.domain.taskcolumn.application;

import java.util.List;

import com.standardkim.kanban.domain.kanban.application.KanbanFindService;
import com.standardkim.kanban.domain.kanban.domain.Kanban;
import com.standardkim.kanban.domain.taskcolumn.dao.TaskColumnRepository;
import com.standardkim.kanban.domain.taskcolumn.domain.TaskColumn;
import com.standardkim.kanban.domain.taskcolumn.exception.TaskColumnNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskColumnFindService {
	private final KanbanFindService kanbanFindService;

	private final TaskColumnRepository taskColumnRepository;

	@Transactional(readOnly = true)
	public boolean isNameExist(Long kanbanId, String name) {
		return taskColumnRepository.existsByKanbanIdAndName(kanbanId, name);
	}

	@Transactional(readOnly = true)
	public TaskColumn findById(Long columnId) {
		return taskColumnRepository.findById(columnId)
			.orElseThrow(() -> new TaskColumnNotFoundException("task column not found"));
	}

	@Transactional(readOnly = true)
	public TaskColumn findByIdAndKanbanId(Long id, Long kanbanId) {
		return taskColumnRepository.findByIdAndKanbanId(id, kanbanId)
			.orElseThrow(() -> new TaskColumnNotFoundException("task column not found"));
	}

	@Transactional(readOnly = true)
	public List<TaskColumn> findByProjectIdAndSequenceId(Long projectId, Long kanbanSequenceId) {
		Kanban kanban = kanbanFindService.findByProjectIdAndSequenceId(projectId, kanbanSequenceId);
		List<TaskColumn> taskColumns = taskColumnRepository.findByKanbanId(kanban.getId());
		return taskColumns;
	}
}
