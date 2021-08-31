package com.standardkim.kanban.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

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
public class ProjectMember {
	@EmbeddedId
	ProjectMemberKey id;

	@ManyToOne
	@MapsId("project_id")
	@JoinColumn(name = "project_id", nullable = false, insertable = false, updatable = false)
	private Project project;

	@ManyToOne
	@MapsId("user_id")
	@JoinColumn(name = "user_id", nullable = false, insertable = false, updatable = false)
	private User user;

	@Column(name = "is_register", nullable = false)
	@Convert(converter = BooleanToYNConverter.class)
	private boolean isRegister = false;

	@CreationTimestamp
	@Column(name = "register_date", nullable = false)
	private LocalDateTime registerDate;
}
