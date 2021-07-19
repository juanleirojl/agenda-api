package com.bnext.agenda.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bnext.agenda.data.model.User;

public interface UserRepository extends JpaRepository<User, Long>{
	
	Optional<User> findByNameAndLastNameAndPhone(String name, String lastName, String phone);

}
