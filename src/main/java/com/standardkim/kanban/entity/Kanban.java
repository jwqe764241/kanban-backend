package com.standardkim.kanban.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.standardkim.kanban.dto.KanbanDto.UpdateKanbanDto;
import com.standardkim.kanban.util.BooleanToYNConverter;

import org.hibernate.annotations.CreationTimestamp;

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
public class Kanban {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(updatable = false, nullable = false)
	private Long id;

	@Column(length = 50, nullable = false)
	private String name;

	@Column(length = 200)
	private String description;

	@CreationTimestamp
	@Column(name = "register_date", nullable = false)
	private LocalDateTime registerDate;

	@Column(name = "is_deleted", nullable = false)
	@Convert(converter = BooleanToYNConverter.class)
	private boolean isDeleted = false;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "project_id", nullable = false)
	private Project project;

	public void updateToDeleted() {
		isDeleted = true;
	}

	public void updateKanbanInfo(UpdateKanbanDto updateKanbanDto) {
		name = updateKanbanDto.getName();
		description = updateKanbanDto.getDescription();
	}
}