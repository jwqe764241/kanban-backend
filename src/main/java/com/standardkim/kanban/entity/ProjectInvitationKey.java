package com.standardkim.kanban.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ProjectInvitationKey implements Serializable {
	private ProjectMemberKey projectMemberId;

	@Column(name = "invited_user_id")
	private Long invitedUserId;

	public static ProjectInvitationKey from(ProjectMemberKey projectMemberId, Long invitedUserId) {
		return ProjectInvitationKey.builder()
			.projectMemberId(projectMemberId)
			.invitedUserId(invitedUserId)
			.build();
	}

	@Override
	public boolean equals(Object o) {
		if(this == o)
			return true;

		if(o == null || this.getClass() != o.getClass()) {
			return false;
		}

		ProjectInvitationKey key = (ProjectInvitationKey) o;

		return projectMemberId.equals(key.getProjectMemberId())
			&& invitedUserId.equals(key.getInvitedUserId());
	}

	@Override
	public int hashCode() {
		int result = projectMemberId.hashCode();
		result = 31 * result + invitedUserId.hashCode();
		return result;
	}
}