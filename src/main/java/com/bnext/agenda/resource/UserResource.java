package com.bnext.agenda.resource;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bnext.agenda.data.dto.UserCreateDTO;
import com.bnext.agenda.data.dto.UserDTO;
import com.bnext.agenda.resource.doc.UserResourceAPI;
import com.bnext.agenda.service.UserService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("api/v1/users")
@Log4j2
@AllArgsConstructor
public class UserResource implements UserResourceAPI{
	
	//private static final String ID = "orderId";
	private static final String NEW_ORDER_LOG = "New order was created id:{}";
	//private static final String ORDER_UPDATED_LOG = "Order:{} was updated";
	
	private UserService userService;
	
	@GetMapping
	public String teste() {
		return "teste";
	}
	
	@Override
	@PostMapping()
	public ResponseEntity<UserDTO> createOrder(@Valid @RequestBody UserCreateDTO userCreateDTO) {
		final UserDTO createdUser = userService.create(userCreateDTO);
		log.info(NEW_ORDER_LOG, createdUser.toString());
		return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
	}
	
	
	
	
}