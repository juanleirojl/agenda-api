package com.bnext.agenda.repository;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bnext.agenda.data.model.Contact;
import com.bnext.agenda.data.model.User;

public interface ContactRepository extends JpaRepository<Contact, Long>{
	
	Optional<Contact> findByContactNameAndPhoneAndUserId(String contactName, String phone, Long idUser);
	
	Set<Contact> findAllByUser(User user);

}
