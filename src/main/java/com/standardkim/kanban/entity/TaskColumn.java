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
}
