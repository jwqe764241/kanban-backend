package com.standardkim.kanban.global.error;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class FieldValidationError {
	private String fieldName;
	private Object rejectedValue;
	private String message;

	public static FieldValidationError of(String fieldName, Object rejectedValue, String message) {
		return FieldValidationError.builder()
			.fieldName(fieldName)
			.rejectedValue(rejectedValue)
			.message(message)
			.build();
	}
}
