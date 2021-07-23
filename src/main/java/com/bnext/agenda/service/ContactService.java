package com.bnext.agenda.service;

import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import com.bnext.agenda.config.MapperUtil;
import com.bnext.agenda.data.dto.ContactCreateDTO;
import com.bnext.agenda.data.dto.ContactDTO;
import com.bnext.agenda.data.model.Contact;
import com.bnext.agenda.data.model.User;
import com.bnext.agenda.exception.BusinessException;
import com.bnext.agenda.repository.ContactRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ContactService {
	
	private ContactRepository contactRepository;
    private MessageSource messageSource;
    private MapperUtil mapperUtil;
    private NeutrinoService neutrinoService;
	
	public Set<Contact> saveContact(final User user, final Set<ContactCreateDTO> contactListDTO) {
		return contactListDTO.stream().map(contactDTO->{
			
			if(this.neutrinoService.phoneIsNoValid(contactDTO.getPhone())) {
				throw new BusinessException(messageSource.getMessage("phone.is.not.valid", new Object[] {contactDTO.getPhone()}, LocaleContextHolder.getLocale()));
			}
			
			Contact contact = this.contactRepository.findByContactNameAndPhoneAndUserId(contactDTO.getContactName(), contactDTO.getPhone(), user.getId())
			.map(contactUpdate-> this.update(contactUpdate,contactDTO))
			.orElseGet(this.save(contactDTO,user));
			
			return contact;
		}).collect(Collectors.toSet());
		
		
		
	}
	
	private Supplier<? extends Contact> save(ContactCreateDTO contactCreateDTO, User user) {
		Contact contact = this.mapperUtil.map(contactCreateDTO, Contact.class);
		contact.setUser(user);
		return () -> this.contactRepository.save(contact);
	}

	public Contact update(Contact contactUpdated, ContactCreateDTO contact) {
		contactUpdated.setContactName(contact.getContactName());
		contactUpdated.setPhone(contact.getPhone());
		return this.contactRepository.save(contactUpdated);
	}
	
	public Set<ContactDTO> findByUser(final User user){
		return this.mapperUtil.toSet(this.contactRepository.findAllByUser(user), ContactDTO.class);
	}
}