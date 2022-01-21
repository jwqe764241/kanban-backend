package com.standardkim.kanban.domain.task.application;

import com.standardkim.kanban.domain.kanban.application.KanbanFindService;
import com.standardkim.kanban.domain.kanban.domain.Kanban;
import com.standardkim.kanban.domain.task.dao.TaskRepository;
import com.standardkim.kanban.domain.task.domain.Task;
import com.standardkim.kanban.domain.task.dto.UpdateTaskParam;
import com.standardkim.kanban.domain.taskcolumn.application.TaskColumnFindService;
import com.standardkim.kanban.domain.taskcolumn.domain.TaskColumn;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskUpdateService {
	private final TaskColumnFindService taskColumnFindService;

	private final KanbanFindService kanbanFindService;

	private final TaskRepository taskRepository;
	
	@Transactional(isolation = Isolation.SERIALIZABLE)
	public Task update(Long projectId, Long kanbanSequenceId, Long columnId, Long taskId, UpdateTaskParam param) {
		Kanban kanban = kanbanFindService.findByProjectIdAndSequenceId(projectId, kanbanSequenceId);
		TaskColumn taskColumn = taskColumnFindService.findByIdAndKanbanId(columnId, kanban.getId());
		Task task = taskRepository.findByIdAndTaskColumnId(taskId, taskColumn.getId());

		if(task != null) {
			task.updateText(param.getText());
		}

		return task;
	}
}
