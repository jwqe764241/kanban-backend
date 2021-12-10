package com.standardkim.kanban.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;

import com.standardkim.kanban.entity.common.BaseTimeEntity;
import com.standardkim.kanban.util.BooleanToYNConverter;

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
public class ProjectMember extends BaseTimeEntity {
	@EmbeddedId
	ProjectMemberKey id;

	@ManyToOne
	@MapsId("projectId")
	@JoinColumn(name = "project_id", nullable = false, insertable = false, updatable = false)
	private Project project;

	@ManyToOne
	@MapsId("userId")
	@JoinColumn(name = "user_id", nullable = false, insertable = false, updatable = false)
	private User user;

	@Builder.Default
	@Column(name = "is_register", nullable = false)
	@Convert(converter = BooleanToYNConverter.class)
	private boolean isRegister = false;

	@Builder.Default
	@OneToMany(mappedBy = "projectMember")
	private Set<ProjectInvitation> invitations = new HashSet<>();

	public static ProjectMember from(Project project, User user, boolean isRegister) {
		ProjectMemberKey id = ProjectMemberKey.from(project.getId(), user.getId());
		return ProjectMember.builder()
			.id(id)
			.project(project)
			.user(user)
			.isRegister(isRegister)
			.build();
	}
}
