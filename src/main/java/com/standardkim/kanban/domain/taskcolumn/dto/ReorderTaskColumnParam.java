package com.standardkim.kanban.domain.taskcolumn.dto;

import javax.validation.constraints.NotNull;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ReorderTaskColumnParam {
	@NotNull
	Long columnId;
	Long prevColumnId;
}
