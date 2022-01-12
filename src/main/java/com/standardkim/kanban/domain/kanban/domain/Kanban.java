package com.standardkim.kanban.domain.kanban.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.standardkim.kanban.domain.kanban.presentation.KanbanDto.CreateKanbanParam;
import com.standardkim.kanban.domain.kanban.presentation.KanbanDto.UpdateKanbanParam;
import com.standardkim.kanban.domain.project.domain.Project;
import com.standardkim.kanban.domain.taskcolumn.domain.TaskColumn;
import com.standardkim.kanban.global.entity.BaseTimeEntity;
import com.standardkim.kanban.global.util.BooleanToYNConverter;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Kanban extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(updatable = false, nullable = false)
	private Long id;

	@Column(length = 50, nullable = false)
	private String name;

	@Column(length = 200)
	private String description;

	@Builder.Default
	@Column(name = "is_deleted", nullable = false)
	@Convert(converter = BooleanToYNConverter.class)
	private boolean isDeleted = false;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "project_id", nullable = false)
	private Project project;

	@Builder.Default
	@OneToMany(mappedBy = "kanban")
	private Set<TaskColumn> taskColumns = new HashSet<>();

	public static Kanban from(CreateKanbanParam param, Project project) {
		Kanban kanban = Kanban.builder()
			.name(param.getName())
			.description(param.getDescription())
			.project(project)
			.build();
		return kanban;
	}

	public void updateToDeleted() {
		isDeleted = true;
	}

	public void updateKanban(UpdateKanbanParam updateKanbanParam) {
		name = updateKanbanParam.getName();
		description = updateKanbanParam.getDescription();
	}
}
