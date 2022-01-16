package com.standardkim.kanban.domain.taskcolumn.application;

import java.util.List;

import com.standardkim.kanban.domain.taskcolumn.dto.CreateColumnAction;
import com.standardkim.kanban.domain.taskcolumn.dto.DeleteColumnAction;
import com.standardkim.kanban.domain.taskcolumn.dto.ReorderColumnAction;
import com.standardkim.kanban.domain.taskcolumn.dto.TaskColumnDetail;
import com.standardkim.kanban.domain.taskcolumn.dto.UpdateColumnAction;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskColumnActionService {
	private final SimpMessagingTemplate simpMessagingTemplate;

	private String getDestination(Long projectId, Long sequenceId) {
		return "/topic/project/" + projectId + "/kanban/" + sequenceId;
	}

	public void sendCreateAction(Long projectId, Long sequenceId, TaskColumnDetail taskColumnDetail) {
		simpMessagingTemplate.convertAndSend(getDestination(projectId, sequenceId), 
			CreateColumnAction.from(taskColumnDetail));
	}

	public void sendUpdateAction(Long projectId, Long sequenceId, TaskColumnDetail taskColumnDetail) {
		simpMessagingTemplate.convertAndSend(getDestination(projectId, sequenceId),
			UpdateColumnAction.from(taskColumnDetail));
	}
	
	public void sendDeleteAction(Long projectId, Long sequenceId, Long columnId, TaskColumnDetail taskColumnDetail) {
		simpMessagingTemplate.convertAndSend(getDestination(projectId, sequenceId), 
			DeleteColumnAction.from(columnId, taskColumnDetail));
	}
	
	public void sendReorderAction(Long projectId, Long sequenceId, List<TaskColumnDetail> taskColumnDetails) {
		simpMessagingTemplate.convertAndSend(getDestination(projectId, sequenceId), 
			ReorderColumnAction.from(taskColumnDetails));
	}
}
