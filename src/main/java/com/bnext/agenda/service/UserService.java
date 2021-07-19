package com.bnext.agenda.service;

import java.util.function.Supplier;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import com.bnext.agenda.data.dto.UserCreateDTO;
import com.bnext.agenda.data.dto.UserDTO;
import com.bnext.agenda.data.model.User;
import com.bnext.agenda.exception.BusinessException;
import com.bnext.agenda.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {
	
    private MessageSource messageSource;
	private UserRepository userRepository;

	public UserDTO create(UserCreateDTO userCreateDTO) {
		
		this.userRepository.findByNameAndLastNameAndPhone(userCreateDTO.getName(), userCreateDTO.getLastName(), userCreateDTO.getPhone())
		.ifPresent(i-> new BusinessException(messageSource.getMessage("user.already.existent",null, LocaleContextHolder.getLocale())));
		
		 User userCreated = userRepository.save(User.toModel(userCreateDTO));
		 return UserDTO.toDTO(userCreated);
	}

	private Supplier<? extends User> teste() {
		
		return () -> new User();
	}
}
