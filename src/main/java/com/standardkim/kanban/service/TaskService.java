package com.standardkim.kanban.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.standardkim.kanban.dto.TaskDto.TaskDetail;
import com.standardkim.kanban.entity.Kanban;
import com.standardkim.kanban.entity.Task;
import com.standardkim.kanban.entity.TaskColumn;
import com.standardkim.kanban.exception.kanban.KanbanNotFoundException;
import com.standardkim.kanban.repository.KanbanRepository;
import com.standardkim.kanban.repository.TaskRepository;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskService {
	private final TaskRepository taskRepository;
		
	private final KanbanRepository kanbanRepository;

	private final ModelMapper modelMapper;

	@Transactional(readOnly = true)
	public Map<Long, List<TaskDetail>> findTasksByKanbanSequence(Long projectId, Long kanbanSequenceId) {
		Kanban kanban = kanbanRepository.findByProjectIdAndSequenceId(projectId, kanbanSequenceId)
			.orElseThrow(() -> new KanbanNotFoundException("kanban not found exception"));
		Set<TaskColumn> taskColumns = kanban.getTaskColumns();

		Map<Long, List<TaskDetail>> result = new HashMap<>();
		for (TaskColumn taskColumn : taskColumns) {
			List<Task> tasks = taskRepository.findByTaskColumnId(taskColumn.getId());
			List<TaskDetail> taskDetails = modelMapper.map(tasks, new TypeToken<List<TaskDetail>>(){}.getType());
			result.put(taskColumn.getId(), taskDetails);
		}

		return result;
	}
}
