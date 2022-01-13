package com.standardkim.kanban.global.config.mapper;

import com.standardkim.kanban.domain.task.domain.Task;
import com.standardkim.kanban.domain.task.dto.TaskDto.TaskDetail;

import org.modelmapper.Converter;
import org.springframework.stereotype.Component;
import org.modelmapper.AbstractConverter;

@Component
public class TaskToTaskDetailConverter extends ModelMapperConverter<Task, TaskDetail> {
	@Override
	protected Converter<Task, TaskDetail> converter() {
		return new AbstractConverter<Task, TaskDetail>() {
			@Override
			public TaskDetail convert(Task task) {
				TaskDetail taskDetail = TaskDetail.builder()
					.id(task.getId())
					.prevId(task.getPrevId())
					.taskColumnId(task.getTaskColumnId())
					.text(task.getText())
					.build();
				return taskDetail;
			}
		};
	}
}
