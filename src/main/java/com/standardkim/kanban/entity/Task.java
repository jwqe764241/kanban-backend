package com.standardkim.kanban.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.standardkim.kanban.dto.TaskDto.CreateTaskParam;

import org.hibernate.annotations.CreationTimestamp;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
	uniqueConstraints = {
		@UniqueConstraint(columnNames = {"prev_id"})
	}
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Task {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(updatable = false, nullable = false)
	private Long id;

	@Column(nullable = false, length=2048)
	private String text;

	@CreationTimestamp
	@Column(name = "register_date", nullable = false)
	private LocalDateTime registerDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "task_column_id", nullable = false)
	private TaskColumn taskColumn;

	@Column(name = "task_column_id", nullable = true, insertable = false, updatable = false)
	private Long taskColumnId;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "prev_id", nullable = true)
	private Task prevTask;

	@Column(name = "prev_id", nullable = true, insertable = false, updatable = false)
	private Long prevId;

	public static Task from(CreateTaskParam param, TaskColumn taskColumn) {
		return Task.builder()
			.text(param.getText())
			.taskColumn(taskColumn)
			.taskColumnId(taskColumn.getId())
			.build();
	}
	
	public void updatePrevTask(Task prevTask) {
		if(prevTask == null) {
			this.prevTask = null;
			this.prevId = null;
		}
		else {
			this.prevTask = prevTask;
			this.prevId = prevTask.getId();
		}
	}
}
