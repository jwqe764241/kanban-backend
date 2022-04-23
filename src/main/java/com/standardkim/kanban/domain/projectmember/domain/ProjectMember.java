package com.standardkim.kanban.domain.projectmember.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;

import com.standardkim.kanban.domain.model.BaseTimeEntity;
import com.standardkim.kanban.domain.project.domain.Project;
import com.standardkim.kanban.domain.projectinvitation.domain.ProjectInvitation;
import com.standardkim.kanban.domain.user.domain.User;

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
	@OneToMany(mappedBy = "projectMember")
	private Set<ProjectInvitation> invitations = new HashSet<>();

	@ManyToOne(cascade = {CascadeType.MERGE})
	@JoinColumn(name = "role_id", referencedColumnName = "id", nullable = false)
	private ProjectRole projectRole;

	public static ProjectMember of(Project project, User user, ProjectRole projectRole) {
		ProjectMemberKey id = ProjectMemberKey.of(project.getId(), user.getId());
		return ProjectMember.builder()
			.id(id)
			.project(project)
			.user(user)
			.projectRole(projectRole)
			.build();
	}
}
