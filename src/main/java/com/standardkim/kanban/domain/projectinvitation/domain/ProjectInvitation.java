package com.standardkim.kanban.domain.projectinvitation.domain;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.standardkim.kanban.domain.model.BaseTimeEntity;
import com.standardkim.kanban.domain.projectmember.domain.ProjectMember;
import com.standardkim.kanban.domain.user.domain.User;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
	name = "project_invitation",
	uniqueConstraints = {
		@UniqueConstraint(columnNames = {"project_id", "invited_user_id"})
	}
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ProjectInvitation extends BaseTimeEntity {
	@EmbeddedId
	private ProjectInvitationKey id;

	@ManyToOne
	@MapsId("projectMemberId")
	@JoinColumns({
		@JoinColumn(name = "project_id", referencedColumnName = "project_id"),
		@JoinColumn(name = "user_id", referencedColumnName = "user_id")
	})
	private ProjectMember projectMember;

	@ManyToOne
	@MapsId("invitedUserId")
	@JoinColumn(name = "invited_user_id", nullable = false, insertable = false)
	private User invitedUser;

	public static ProjectInvitation of(ProjectMember projectMember, User inviteeUser) {
		ProjectInvitationKey key = ProjectInvitationKey.builder()
			.projectMemberId(projectMember.getId())
			.invitedUserId(inviteeUser.getId())
			.build();
		return ProjectInvitation.builder()
			.id(key)
			.projectMember(projectMember)
			.invitedUser(inviteeUser)
			.build();
	}
}
