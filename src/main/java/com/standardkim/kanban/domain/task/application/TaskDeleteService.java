package com.standardkim.kanban.domain.task.application;

import com.standardkim.kanban.domain.kanban.application.KanbanFindService;
import com.standardkim.kanban.domain.kanban.domain.Kanban;
import com.standardkim.kanban.domain.task.dao.TaskRepository;
import com.standardkim.kanban.domain.task.domain.Task;
import com.standardkim.kanban.domain.taskcolumn.application.TaskColumnFindService;
import com.standardkim.kanban.domain.taskcolumn.domain.TaskColumn;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskDeleteService {
	private final TaskColumnFindService taskColumnFindService;

	private final KanbanFindService kanbanFindService;

	private final TaskRepository taskRepository;

	@Transactional(isolation = Isolation.SERIALIZABLE)
	public Task delete(Long projectId, Long kanbanSequenceId, Long columnId, Long taskId) {
		Kanban kanban = kanbanFindService.findByProjectIdAndSequenceId(projectId, kanbanSequenceId);
		TaskColumn taskColumn = taskColumnFindService.findByIdAndKanbanId(columnId, kanban.getId());
		Task task = taskRepository.findByIdAndTaskColumnId(taskId, taskColumn.getId());
		Task nextTask = taskRepository.findByPrevIdAndTaskColumnId(task.getId(), taskColumn.getId());

		//task is last task or there's no other task
		if(nextTask == null) {
			taskRepository.delete(task);
			return null;
		}
		
		//update next task to be first task
		if(task.getPrevId() == null) {
			nextTask.updatePrevTask(null);
			taskRepository.delete(task);
			return nextTask;
		}
		//update next task's previous task to current task(task that will be deleted)'s previous task
		else {
			Task prevTask = task.getPrevTask();
			nextTask.updatePrevTask(null);
			taskRepository.delete(task);
			taskRepository.flush();
			nextTask.updatePrevTask(prevTask);
			return nextTask;
		}
	}
}
