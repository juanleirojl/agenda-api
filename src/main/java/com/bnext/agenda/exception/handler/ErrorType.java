package com.bnext.agenda.exception.handler;

import lombok.Getter;

@Getter
public enum ErrorType {

	INVALID_DATA("Invalid data"),
	ACCESS_DENIED("Access Denied"),
	SYSTEM_ERROR("System Error"),
	INVALID_PARAMETER("Invalid Parameter"),
	INCOMPREHENSIBLE_MESSAGE("Incomprehensible message"),
	RESOURCE_NOT_FOUND("Resource not found"),
	ENTIDADE_EM_USO("Entity in use"),
	BUSINESS_EXCEPTION_VIOLATION("Business exception violation");
	
	private String title;
	
	ErrorType(String title) {
		this.title = title;
	}
}
