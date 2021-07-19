package com.bnext.agenda.data.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

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
public class ContactDTO extends BaseDTO{
	
	@JsonProperty(required = true)
	@NotEmpty
	@NotBlank
	private String contactName;
	
	@JsonProperty(required = true)
	@NotEmpty
	@NotBlank
	private String phone;
	
}
