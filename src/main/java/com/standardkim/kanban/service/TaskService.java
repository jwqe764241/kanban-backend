package com.standardkim.kanban.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.standardkim.kanban.dto.TaskDto.CreateTaskParam;
import com.standardkim.kanban.dto.TaskDto.TaskDetail;
import com.standardkim.kanban.entity.Kanban;
import com.standardkim.kanban.entity.Task;
import com.standardkim.kanban.entity.TaskColumn;
import com.standardkim.kanban.exception.kanban.KanbanNotFoundException;
import com.standardkim.kanban.exception.taskcolumn.TaskColumnNotFoundException;
import com.standardkim.kanban.repository.KanbanRepository;
import com.standardkim.kanban.repository.TaskColumnRepository;
import com.standardkim.kanban.repository.TaskRepository;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskService {
	private final TaskRepository taskRepository;

	private final TaskColumnRepository taskColumnRepository;
		
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

	@Transactional(isolation = Isolation.SERIALIZABLE)
	public List<Task> create(Long projectId, Long kanbanSequenceId, Long columnId, CreateTaskParam param) {
		Kanban kanban = kanbanRepository.findByProjectIdAndSequenceId(projectId, kanbanSequenceId)
			.orElseThrow(() -> new KanbanNotFoundException("kanban not found exception"));
		TaskColumn taskColumn = taskColumnRepository.findByIdAndKanbanId(columnId, kanban.getId())
			.orElseThrow(() -> new TaskColumnNotFoundException("task column not found"));

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
		Kanban kanban = kanbanRepository.findByProjectIdAndSequenceId(projectId, kanbanSequenceId)
			.orElseThrow(() -> new KanbanNotFoundException("kanban not found exception"));
		TaskColumn taskColumn = taskColumnRepository.findByIdAndKanbanId(columnId, kanban.getId())
			.orElseThrow(() -> new TaskColumnNotFoundException("task column not found"));
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
