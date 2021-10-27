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
	@Column(name = "project_id")
	private Long projectId;

	@Column(name = "invited_user_id")
	private Long invitedUserId;

	public static ProjectInvitationKey from(Long projectId, Long invitedUserId) {
		return ProjectInvitationKey.builder()
			.projectId(projectId)
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

		return projectId.equals(key.getProjectId())
			&& invitedUserId.equals(key.getInvitedUserId());
	}
}