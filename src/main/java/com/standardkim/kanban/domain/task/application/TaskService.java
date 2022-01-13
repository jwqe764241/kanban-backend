package com.standardkim.kanban.domain.task.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.standardkim.kanban.domain.kanban.application.KanbanService;
import com.standardkim.kanban.domain.kanban.domain.Kanban;
import com.standardkim.kanban.domain.task.dao.TaskRepository;
import com.standardkim.kanban.domain.task.domain.Task;
import com.standardkim.kanban.domain.task.dto.TaskDto.CreateTaskParam;
import com.standardkim.kanban.domain.task.dto.TaskDto.ReorderTaskParam;
import com.standardkim.kanban.domain.task.dto.TaskDto.UpdateTaskParam;
import com.standardkim.kanban.domain.task.exception.TaskNotFoundException;
import com.standardkim.kanban.domain.taskcolumn.application.TaskColumnService;
import com.standardkim.kanban.domain.taskcolumn.domain.TaskColumn;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskService {
	private final TaskRepository taskRepository;

	private final TaskColumnService taskColumnService;

	private final KanbanService kanbanService;

	@Transactional(readOnly = true)
	public Task findById(Long id) {
		return taskRepository.findById(id)
			.orElseThrow(() -> new TaskNotFoundException("task not found"));
	}

	@Transactional(readOnly = true)
	public List<Task> findByProjectIdAndSequenceId(Long projectId, Long kanbanSequenceId) {
		Kanban kanban = kanbanService.findByProjectIdAndSequenceId(projectId, kanbanSequenceId);
		Set<TaskColumn> taskColumns = kanban.getTaskColumns();

		List<Task> results = new ArrayList<>();
		for (TaskColumn taskColumn : taskColumns) {
			List<Task> tasks = taskRepository.findByTaskColumnId(taskColumn.getId());
			results.addAll(tasks);
		}

		return results;
	}

	@Transactional(isolation = Isolation.SERIALIZABLE)
	public List<Task> create(Long projectId, Long kanbanSequenceId, Long columnId, CreateTaskParam param) {
		Kanban kanban = kanbanService.findByProjectIdAndSequenceId(projectId, kanbanSequenceId);
		TaskColumn taskColumn = taskColumnService.findByIdAndKanbanId(columnId, kanban.getId());

		Task firstTask = taskRepository.findByPrevIdAndTaskColumnId(null, taskColumn.getId());
		Task task = Task.from(param, taskColumn);
		taskRepository.save(task);

		if(firstTask != null) {
			firstTask.updatePrevTask(task);
		}

		List<Task> updatedTasks = new ArrayList<>();
		updatedTasks.add(task);
		if(firstTask != null) {
			updatedTasks.add(firstTask);
		}

		return updatedTasks;
	}

	@Transactional(isolation = Isolation.SERIALIZABLE)
	public Task delete(Long projectId, Long kanbanSequenceId, Long columnId, Long taskId) {
		Kanban kanban = kanbanService.findByProjectIdAndSequenceId(projectId, kanbanSequenceId);
		TaskColumn taskColumn = taskColumnService.findByIdAndKanbanId(columnId, kanban.getId());
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

	@Transactional(isolation = Isolation.SERIALIZABLE)
	public List<Task> reorder(Long projectId, Long kanbanSequenceId, Long columnId, ReorderTaskParam param) {
		Kanban kanban = kanbanService.findByProjectIdAndSequenceId(projectId, kanbanSequenceId);
		Task task = findById(param.getTaskId());
		TaskColumn srcColumn = task.getTaskColumn();
		TaskColumn destColumn = taskColumnService.findByIdAndKanbanId(columnId, kanban.getId());
		
		Task prevTask = task.getPrevTask();
		Task nextTask = taskRepository.findByPrevIdAndTaskColumnId(task.getId(), srcColumn.getId());
		Task firstDestTask = taskRepository.findByPrevIdAndTaskColumnId(null, destColumn.getId());

		boolean isSameColumn = srcColumn.getId() == destColumn.getId();

		//firstDestTask must not be a null when move in same column
		if(isSameColumn && firstDestTask == null) {
			throw new RuntimeException("task column is invalid state");
		}

		List<Task> updatedTasks = new ArrayList<>();

		//update next task's previous task to current task(task that will be deleted)'s previous task
		task.updatePrevTask(null);
		if(nextTask != null) {
			nextTask.updatePrevTask(prevTask);
			updatedTasks.add(nextTask);
		}
		taskRepository.flush();

		//move task
		if(param.getPrevTaskId() != null) {
			Task destTask = findById(param.getPrevTaskId());
			Task destNextTask = taskRepository.findByPrevIdAndTaskColumnId(destTask.getId(), destColumn.getId());

			if(destNextTask != null) {
				destNextTask.updatePrevTask(task);
				updatedTasks.add(destNextTask);
			}
			taskRepository.flush();

			task.updatePrevTask(destTask);
		}
		//move task to first
		else {
			if(firstDestTask != null) {
				firstDestTask.updatePrevTask(task);
				updatedTasks.add(firstDestTask);
			}
		}

		//if column is not same, update task's column to destination column
		if(!isSameColumn) {
			task.updateTaskColumn(destColumn);
		}

		updatedTasks.add(task);
		return updatedTasks;
	}

	@Transactional(isolation = Isolation.SERIALIZABLE)
	public Task update(Long projectId, Long kanbanSequenceId, Long columnId, Long taskId, UpdateTaskParam param) {
		Kanban kanban = kanbanService.findByProjectIdAndSequenceId(projectId, kanbanSequenceId);
		TaskColumn taskColumn = taskColumnService.findByIdAndKanbanId(columnId, kanban.getId());
		Task task = taskRepository.findByIdAndTaskColumnId(taskId, taskColumn.getId());

		if(task != null) {
			task.updateText(param.getText());
		}

		return task;
	}
}
