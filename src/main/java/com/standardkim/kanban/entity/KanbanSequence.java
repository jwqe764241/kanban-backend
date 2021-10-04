package com.standardkim.kanban.entity;

import java.time.LocalDateTime;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.standardkim.kanban.util.BooleanToYNConverter;

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

	private LocalDateTime registerDate;

	@Convert(converter = BooleanToYNConverter.class)
	private boolean isDeleted;
}
