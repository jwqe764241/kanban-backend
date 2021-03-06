package com.standardkim.kanban.domain.taskcolumn.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.standardkim.kanban.domain.kanban.domain.Kanban;
import com.standardkim.kanban.domain.model.BaseTimeEntity;
import com.standardkim.kanban.domain.task.domain.Task;

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
public class TaskColumn extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(updatable = false, nullable = false)
	private Long id;

	@Column(nullable = false)
	private String name;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "kanban_id", nullable = false)
	private Kanban kanban;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "prev_id", nullable = true)
	private TaskColumn prevTaskColumn;

	@Column(name = "prev_id", nullable = true, insertable = false, updatable = false)
	private Long prevId;

	@Builder.Default
	@OneToMany(mappedBy = "taskColumn")
	private Set<Task> tasks = new HashSet<>();

	public static TaskColumn of(String name, Kanban kanban) {
		return TaskColumn.builder()
			.name(name)
			.kanban(kanban)
			.build();
	}

	public static TaskColumn of(String name, Kanban kanban, TaskColumn prevTaskColumn) {
		return TaskColumn.builder()
			.name(name)
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
