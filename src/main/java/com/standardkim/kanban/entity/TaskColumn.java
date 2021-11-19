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

import com.standardkim.kanban.dto.TaskColumnDto.CreateTaskColumnParam;

import org.hibernate.annotations.CreationTimestamp;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
	uniqueConstraints = {
		@UniqueConstraint(columnNames = {"kanban_id", "prev_id"}),
		@UniqueConstraint(columnNames = {"kanban_id", "name"})
	}
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class TaskColumn {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(updatable = false, nullable = false)
	private Long id;

	@Column(nullable = false)
	private String name;

	@CreationTimestamp
	@Column(name = "register_date", nullable = false)
	private LocalDateTime registerDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "kanban_id", nullable = false)
	private Kanban kanban;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "prev_id", nullable = true)
	private TaskColumn prevTaskColumn;

	@Column(name = "prev_id", nullable = true, insertable = false, updatable = false)
	private Long prevId;

	public static TaskColumn from(CreateTaskColumnParam param, Kanban kanban) {
		return TaskColumn.builder()
			.name(param.getName())
			.kanban(kanban)
			.build();
	}

	public static TaskColumn from(CreateTaskColumnParam param, Kanban kanban, TaskColumn prevTaskColumn) {
		return TaskColumn.builder()
			.name(param.getName())
			.kanban(kanban)
			.prevTaskColumn(prevTaskColumn)
			.prevId(prevTaskColumn.getId())
			.build();
	}

	public void updatePrevColumn(TaskColumn prevTaskColumn) {
		if(prevTaskColumn == null) {
			this.prevId = null;
			this.prevTaskColumn = null;
		}
		else {
			this.prevId = prevTaskColumn.getId();
			this.prevTaskColumn = prevTaskColumn;
		}
	}

	public void updateName(String name) {
		this.name = name;
	}
}
