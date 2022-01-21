package com.standardkim.kanban.domain.projectmember.dto;

import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ProjectMemberDetail {
	private Long id;
	private String name;
	private String email;
	private LocalDateTime date;
}
