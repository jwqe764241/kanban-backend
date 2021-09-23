package com.standardkim.kanban.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ValidationError {
	private String fieldName;
	private Object rejectedValue;
	private String message;
}
