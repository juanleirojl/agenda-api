package com.bnext.agenda.resource.doc;

import java.util.Set;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import com.bnext.agenda.data.dto.ContactCreateDTO;
import com.bnext.agenda.data.dto.ContactDTO;
import com.bnext.agenda.data.dto.UserCommonDTO;
import com.bnext.agenda.data.dto.UserCreateDTO;
import com.bnext.agenda.data.dto.UserDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "user", description = "the User API")
public interface UserResourceAPI {

	@Operation(summary = "Create a new user", description = "", tags = { "user" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "User created", content = @Content(schema = @Schema(implementation = UserDTO.class))),
			@ApiResponse(responseCode = "400", description = "Invalid input"),
			@ApiResponse(responseCode = "409", description = "User already exists") })
	public ResponseEntity<UserDTO> createUser(
			@Parameter(description = "User to create. Cannot null or empty.", required = true, schema = @Schema(implementation = UserCreateDTO.class)) @Valid @RequestBody UserCreateDTO userCreateDTO);

	@Operation(summary = "Update user contact by its id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "User contact updated", content = {@Content(schema = @Schema(implementation = UserDTO.class)) }),
			@ApiResponse(responseCode = "404", description = "User not found", content = @Content) })
	public ResponseEntity<UserDTO> updateUserContact(@PathVariable(value = "id") Long id,
			@Valid @RequestBody Set<ContactCreateDTO> contactListDTO);
	

	@Operation(summary = "Get contacts by a user")
	@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Found the contact list", content = {@Content(schema = @Schema(implementation = Long.class))}),
			@ApiResponse(responseCode = "404", description = "User not found", content = @Content)})
	public ResponseEntity<Set<ContactDTO>> findContactByUser(@PathVariable(value = "id") Long id);
	
	@Operation(summary = "Get contacts in common")
	@ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Found the contact list", content = {@Content(schema = @Schema(implementation = UserCommonDTO.class))}),
			@ApiResponse(responseCode = "404", description = "User not found", content = @Content)})
	public ResponseEntity<Set<ContactDTO>> findContactsInCommon(
			@Parameter(description = "Cannot null or empty.", required = true, schema = @Schema(implementation = UserCommonDTO.class)) @Valid UserCommonDTO userCommonDTO
			);
}
