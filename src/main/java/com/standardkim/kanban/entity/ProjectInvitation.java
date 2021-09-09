package com.standardkim.kanban.entity;

import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

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
public class ProjectInvitation {
	@EmbeddedId
	private ProjectInvitationKey key;

	@ManyToOne
	@MapsId("project_id")
	@JoinColumn(name = "project_id", nullable = false, insertable = false)
	private Project project;

	@ManyToOne
	@MapsId("invited_user_id")
	@JoinColumn(name = "invited_user_id", nullable = false, insertable = false)
	private User invitedUser;

	@ManyToOne(cascade = CascadeType.DETACH, optional = false)
	@JoinColumn(name = "register_user_id", referencedColumnName = "id", nullable = false, updatable = false)
	private User registerUser;

	@CreationTimestamp
	@Column(name = "register_date", nullable = false)
	private LocalDateTime registerDate;
}
