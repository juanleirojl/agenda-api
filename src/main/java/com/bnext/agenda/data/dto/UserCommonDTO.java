package com.bnext.agenda.data.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class UserCommonDTO {

	@Schema(description = "Id of the user one", example = "1", required = true)
	@JsonProperty(required = true)
	@NotNull
	private Long userId1;

	@Schema(description = "Id of the user two.", example = "2", required = true)
	@JsonProperty(required = true)
	@NotNull
	private Long userId2;

}
