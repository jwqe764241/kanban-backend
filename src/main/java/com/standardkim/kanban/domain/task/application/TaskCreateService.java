package com.standardkim.kanban.domain.task.application;

import java.util.ArrayList;
import java.util.List;

import com.standardkim.kanban.domain.kanban.application.KanbanFindService;
import com.standardkim.kanban.domain.kanban.domain.Kanban;
import com.standardkim.kanban.domain.task.dao.TaskRepository;
import com.standardkim.kanban.domain.task.domain.Task;
import com.standardkim.kanban.domain.task.dto.CreateTaskParam;
import com.standardkim.kanban.domain.taskcolumn.application.TaskColumnFindService;
import com.standardkim.kanban.domain.taskcolumn.domain.TaskColumn;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskCreateService {
	private final TaskColumnFindService taskColumnFindService;

	private final KanbanFindService kanbanFindService;

	private final TaskRepository taskRepository;

	@Transactional(isolation = Isolation.SERIALIZABLE)
	public List<Task> create(Long projectId, Long kanbanSequenceId, Long columnId, CreateTaskParam param) {
		Kanban kanban = kanbanFindService.findByProjectIdAndSequenceId(projectId, kanbanSequenceId);
		TaskColumn taskColumn = taskColumnFindService.findByIdAndKanbanId(columnId, kanban.getId());

		Task firstTask = taskRepository.findByPrevIdAndTaskColumnId(null, taskColumn.getId());
		Task task = Task.of(param.getText(), taskColumn);
		Task newTask = taskRepository.save(task);

		if(firstTask != null) {
			firstTask.updatePrevTask(newTask);
		}

		List<Task> updatedTasks = new ArrayList<>();
		updatedTasks.add(newTask);
		if(firstTask != null) {
			updatedTasks.add(firstTask);
		}

		return updatedTasks;
	}
}
