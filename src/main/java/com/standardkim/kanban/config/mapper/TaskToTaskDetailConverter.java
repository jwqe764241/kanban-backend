package com.standardkim.kanban.config.mapper;

import com.standardkim.kanban.dto.TaskDto.TaskDetail;
import com.standardkim.kanban.entity.Task;

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
