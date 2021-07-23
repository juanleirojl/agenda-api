package com.bnext.agenda.resource;

import java.util.Set;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bnext.agenda.data.dto.ContactCreateDTO;
import com.bnext.agenda.data.dto.ContactDTO;
import com.bnext.agenda.data.dto.UserCommonDTO;
import com.bnext.agenda.data.dto.UserCreateDTO;
import com.bnext.agenda.data.dto.UserDTO;
import com.bnext.agenda.resource.doc.UserResourceAPI;
import com.bnext.agenda.service.UserService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("v1/users")
@Log4j2
@AllArgsConstructor
public class UserResource implements UserResourceAPI{
	
	private static final String NEW_USER_LOG = "New User was created id:{}";
	private static final String USER_UPDATED_LOG = "User:{} was updated";
	private static final String CONTACT_FOUND_LOG = "Contacts :{} was found";
	
	private UserService userService;
	
	@GetMapping
	public ResponseEntity<String> teste(){
		return ResponseEntity.ok("Ok");
	}
	
	
	@Override
	@PostMapping
	public ResponseEntity<UserDTO> createUser(@Valid @RequestBody final UserCreateDTO userCreateDTO) {
		final UserDTO createdUser = this.userService.create(userCreateDTO);
		log.info(NEW_USER_LOG, createdUser.toString());
		return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
	}


	@Override
	@PutMapping("/{id}/contact")
	public ResponseEntity<UserDTO> updateUserContact(@PathVariable(value = "id") final Long id, @RequestBody @Valid final Set<ContactCreateDTO> contactListDTO) {
		final UserDTO userUpdatedContact = this.userService.updateUserContact(id, contactListDTO);
		log.info(USER_UPDATED_LOG, userUpdatedContact.toString());
		return ResponseEntity.ok(userUpdatedContact);
	}


	@Override
	@GetMapping("/{id}/contact")
	public ResponseEntity<Set<ContactDTO>> findContactByUser(@PathVariable(value = "id") final Long id) {
		final Set<ContactDTO> listContactFound = this.userService.findContactsByUser(id);
		log.info(CONTACT_FOUND_LOG, listContactFound.toString());
		return ResponseEntity.ok(listContactFound);
	}
	
	@Override
	@GetMapping("/contact-commons")
	public ResponseEntity<Set<ContactDTO>> findContactsInCommon(@Valid final UserCommonDTO userCommonDTO) {
		final Set<ContactDTO> listContactInCommons = this.userService.findContactInCommon(userCommonDTO);
		log.info(CONTACT_FOUND_LOG, listContactInCommons.toString());
		return ResponseEntity.ok(listContactInCommons);

	}
	
}