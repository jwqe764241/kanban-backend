package com.standardkim.kanban.domain.projectmember.exception;

import com.standardkim.kanban.global.error.ErrorCode;
import com.standardkim.kanban.global.error.exception.BusinessException;

public class CannotDeleteAdminMemberException extends BusinessException{
	public CannotDeleteAdminMemberException(String message) {
		super(message, ErrorCode.CANNOT_DELETE_ADMIN_MEMBER);
	}
}
