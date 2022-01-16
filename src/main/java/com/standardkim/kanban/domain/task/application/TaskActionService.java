package com.standardkim.kanban.domain.task.application;

import java.util.List;

import com.standardkim.kanban.domain.task.dto.CreateTaskAction;
import com.standardkim.kanban.domain.task.dto.DeleteTaskAction;
import com.standardkim.kanban.domain.task.dto.ReorderTaskAction;
import com.standardkim.kanban.domain.task.dto.TaskDetail;
import com.standardkim.kanban.domain.task.dto.UpdateTaskAction;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskActionService {
	private final SimpMessagingTemplate simpMessagingTemplate;

	private String getDestination(Long projectId, Long sequenceId) {
		return "/topic/project/" + projectId + "/kanban/" + sequenceId;
	}

	public void sendCreateAction(Long projectId, Long sequenceId, List<TaskDetail> taskDetails) {
		simpMessagingTemplate.convertAndSend(getDestination(projectId, sequenceId), 
			CreateTaskAction.from(taskDetails));
	}

	public void sendUpdateAction(Long projectId, Long sequenceId, TaskDetail taskDetail) {
		simpMessagingTemplate.convertAndSend(getDestination(projectId, sequenceId), 
			UpdateTaskAction.from(taskDetail));
	}

	public void sendDeleteAction(Long projectId, Long sequenceId, Long taskId, TaskDetail taskDetail) {
		simpMessagingTemplate.convertAndSend(getDestination(projectId, sequenceId), 
			DeleteTaskAction.from(taskId, taskDetail));
	}
	
	public void sendReorderAction(Long projectId, Long sequenceId, List<TaskDetail> taskDetails) {
		simpMessagingTemplate.convertAndSend(getDestination(projectId, sequenceId), 
			ReorderTaskAction.from(taskDetails));
	}
}
