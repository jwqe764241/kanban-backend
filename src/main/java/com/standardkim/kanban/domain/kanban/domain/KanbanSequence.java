package com.standardkim.kanban.domain.kanban.domain;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Immutable
@Table(name = "v_kanban_sequence")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class KanbanSequence {
	@Id
	private Long id;

	private Long projectId;

	private Long sequenceId;

	private String name;
	
	private String description;

	private LocalDateTime createdAt;
}
