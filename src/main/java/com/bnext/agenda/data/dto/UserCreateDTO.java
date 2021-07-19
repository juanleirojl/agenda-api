package com.bnext.agenda.data.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.bnext.agenda.data.model.User;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class UserCreateDTO {

	@Schema(description = "Name of the user.", example = "Paulo", required = true)
	@JsonProperty(required = true)
	@NotEmpty
	@NotBlank
	private String name;

	@Schema(description = "Last name of the user.", example = "do Fardan", required = true)
	@JsonProperty(required = true)
	@NotEmpty
	@NotBlank
	private String lastName;

	@Schema(description = "Phone of the user.", example = "643172637", required = true)
	@Pattern(regexp = "^\\+?[0-9. ()-]{7,25}$", message = "Phone number")
	@JsonProperty(required = true)
	@NotEmpty
	@NotBlank
	@Size(min = 9, max = 10)
	private String phone;

	public static UserCreateDTO toDTO(@Valid User user) {
		return UserCreateDTO.builder().name(user.getName()).lastName(user.getLastName())
				.phone(user.getPhone()).build();
	}

}
