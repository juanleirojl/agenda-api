package com.bnext.agenda.data.dto;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false, exclude = {"contacts"})
public class UserDTO extends BaseDTO {

	private String name;
	private String lastName;
	private String phone;
	private Set<ContactDTO> contacts;

}
