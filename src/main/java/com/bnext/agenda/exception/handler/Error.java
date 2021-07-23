package com.bnext.agenda.exception.handler;
import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Builder;
import lombok.Getter;

@JsonInclude(Include.NON_NULL)
@Getter
@Builder
public class Error {

	private Integer status;
	private Instant timestamp;
	private String type;
	private String title;
	private String path;
	private String detail;
	private String userMessage;
	private List<Detail> errors;
	
	@Getter
	@Builder
	public static class Detail {
		private String name;
		private String userMessage;
	}
}
