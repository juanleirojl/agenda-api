package com.bnext.agenda.service;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import com.bnext.agenda.config.MapperUtil;
import com.bnext.agenda.data.dto.ContactCreateDTO;
import com.bnext.agenda.data.dto.ContactDTO;
import com.bnext.agenda.data.dto.UserCommonDTO;
import com.bnext.agenda.data.dto.UserCreateDTO;
import com.bnext.agenda.data.dto.UserDTO;
import com.bnext.agenda.data.model.Contact;
import com.bnext.agenda.data.model.User;
import com.bnext.agenda.exception.BusinessException;
import com.bnext.agenda.exception.ResourceAlreadyExist;
import com.bnext.agenda.exception.ResourceNotFoundException;
import com.bnext.agenda.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {

	private UserRepository userRepository;
	private ContactService contactService;
	private MessageSource messageSource;
	private MapperUtil mapperUtil;
	private NeutrinoService neutrinoService;

	public UserDTO create(final UserCreateDTO userCreateDTO) {
		this.validUser(userCreateDTO);
		User userCreated = userRepository.save(this.mapperUtil.map(userCreateDTO, User.class));
		return this.mapperUtil.map(userCreated, UserDTO.class);
	}


	public UserDTO updateUserContact(final Long idUser, final Set<ContactCreateDTO> contactListDTO) {
		User user = this.userRepository.findById(idUser).orElseThrow(ResourceNotFoundException::new);
		
		Set<Contact> contacts = this.contactService.saveContact(user,contactListDTO);
		user.setContacts(contacts);

		return this.mapperUtil.map(this.userRepository.save(user), UserDTO.class);

	}
	
	public Set<ContactDTO> findContactsByUser(final Long id){
		User user = this.userRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
		Set<ContactDTO> contacts = this.contactService.findByUser(user);
		return contacts;
	}
	
	private void validUser(final UserCreateDTO userCreateDTO) {
		if(this.neutrinoService.phoneIsNoValid(userCreateDTO.getPhone())) {
			throw new BusinessException(messageSource.getMessage("phone.is.not.valid", new Object[] {userCreateDTO.getPhone()}, LocaleContextHolder.getLocale()));
		}
		
		this.userRepository.findByNameAndLastNameAndPhone(userCreateDTO.getName(), userCreateDTO.getLastName(),
				userCreateDTO.getPhone()).ifPresent(u -> {
					throw new ResourceAlreadyExist(messageSource.getMessage("resource.already.exist", new Object[] {userCreateDTO.toString()}, LocaleContextHolder.getLocale()));
				});
	}


	public Set<ContactDTO> findContactInCommon(UserCommonDTO userCommonDTO) {
		Set<ContactDTO> contactOne = this.findContactsByUser(userCommonDTO.getUserId1());
		Set<ContactDTO> contactTwo = this.findContactsByUser(userCommonDTO.getUserId2());
		
		return contactOne.stream()
					.filter(contactTwo::contains)
					.collect(Collectors.toSet());
	}

}