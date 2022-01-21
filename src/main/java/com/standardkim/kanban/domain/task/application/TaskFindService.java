package com.standardkim.kanban.domain.task.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.standardkim.kanban.domain.kanban.application.KanbanFindService;
import com.standardkim.kanban.domain.kanban.domain.Kanban;
import com.standardkim.kanban.domain.task.dao.TaskRepository;
import com.standardkim.kanban.domain.task.domain.Task;
import com.standardkim.kanban.domain.task.exception.TaskNotFoundException;
import com.standardkim.kanban.domain.taskcolumn.domain.TaskColumn;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskFindService {
	private final KanbanFindService kanbanFindService;
	
	private final TaskRepository taskRepository;

	@Transactional(readOnly = true)
	public Task findById(Long id) {
		return taskRepository.findById(id)
			.orElseThrow(() -> new TaskNotFoundException("task not found"));
	}

	@Transactional(readOnly = true)
	public List<Task> findByProjectIdAndSequenceId(Long projectId, Long kanbanSequenceId) {
		Kanban kanban = kanbanFindService.findByProjectIdAndSequenceId(projectId, kanbanSequenceId);
		Set<TaskColumn> taskColumns = kanban.getTaskColumns();

		List<Task> results = new ArrayList<>();
		for (TaskColumn taskColumn : taskColumns) {
			List<Task> tasks = taskRepository.findByTaskColumnId(taskColumn.getId());
			results.addAll(tasks);
		}

		return results;
	}
}
