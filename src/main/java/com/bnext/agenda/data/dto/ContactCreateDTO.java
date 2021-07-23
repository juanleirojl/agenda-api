package com.bnext.agenda.data.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

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
public class ContactCreateDTO{
	
	@Schema(description = "Name of the user.", example = "Paulo", required = true)
	@JsonProperty(required = true)
	@NotBlank
	private String contactName;
	
	@Schema(description = "Phone of the user.", example = "643172637", required = true)
	@Pattern(regexp = "^\\+?[0-9. ()-]{7,25}$", message = "Phone number")
	@JsonProperty(required = true)
	@NotBlank
	@Size(min = 9, max = 10)
	private String phone;
	
}
