package com.standardkim.kanban.domain.task.application;

import java.util.ArrayList;
import java.util.List;

import com.standardkim.kanban.domain.kanban.application.KanbanFindService;
import com.standardkim.kanban.domain.kanban.domain.Kanban;
import com.standardkim.kanban.domain.task.dao.TaskRepository;
import com.standardkim.kanban.domain.task.domain.Task;
import com.standardkim.kanban.domain.task.dto.ReorderTaskParam;
import com.standardkim.kanban.domain.taskcolumn.application.TaskColumnFindService;
import com.standardkim.kanban.domain.taskcolumn.domain.TaskColumn;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskReorderService {
	private final TaskFindService taskFindService;

	private final TaskColumnFindService taskColumnFindService;

	private final KanbanFindService kanbanFindService;

	private final TaskRepository taskRepository;

	@Transactional(isolation = Isolation.SERIALIZABLE)
	public List<Task> reorder(Long projectId, Long kanbanSequenceId, Long columnId, ReorderTaskParam param) {
		Kanban kanban = kanbanFindService.findByProjectIdAndSequenceId(projectId, kanbanSequenceId);
		Task task = taskFindService.findById(param.getTaskId());
		TaskColumn srcColumn = task.getTaskColumn();
		TaskColumn destColumn = taskColumnFindService.findByIdAndKanbanId(columnId, kanban.getId());
		
		Task prevTask = task.getPrevTask();
		Task nextTask = taskRepository.findByPrevIdAndTaskColumnId(task.getId(), srcColumn.getId());
		Task firstDestTask = taskRepository.findByPrevIdAndTaskColumnId(null, destColumn.getId());

		boolean isSameColumn = srcColumn.getId() == destColumn.getId();

		//firstDestTask must not be a null when move in same column
		if(isSameColumn && firstDestTask == null) {
			throw new RuntimeException("task column is invalid state");
		}

		//reorder to same position
		if(isSameColumn && ((param.getPrevTaskId() == null && prevTask == null) || 
			(prevTask != null && prevTask.getId().equals(param.getPrevTaskId())))) {
			return new ArrayList<>();
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
			Task destTask = taskFindService.findById(param.getPrevTaskId());
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
}
