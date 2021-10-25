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
public class ProjectMemberKey implements Serializable {
	@Column(name = "project_id")
	private Long projectId;

	@Column(name = "user_id")
	private Long userId;

	@Override
	public boolean equals(Object o) {
		if(this == o)
			return true;

		if (o == null || this.getClass() != o.getClass()) {
			return false;
		}

		ProjectMemberKey key = (ProjectMemberKey) o;

		return projectId.equals(key.getProjectId())
			&& userId.equals(key.getUserId());
	}
}
