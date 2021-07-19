package com.bnext.agenda.resource.doc;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

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
	public ResponseEntity<UserDTO> createOrder(@Parameter(description="User to create. Cannot null or empty.", 
            required=true, schema=@Schema(implementation = UserCreateDTO.class)) @Valid @RequestBody UserCreateDTO userCreateDTO);

}
