package com.bnext.agenda.data.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.Valid;

import com.bnext.agenda.data.dto.UserCreateDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name= "users")
@EqualsAndHashCode(callSuper = false)
public class User extends BaseEntity {
		
	@Column(name ="name")
	private String name;
	
	@Column(name = "last_name")
	private String lastName;
	
	@Column(name = "phone")
	private String phone;
	
	@Builder.Default
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)
	private Set<Contact> contacts = new HashSet<>();
	
	public static User toModel(@Valid UserCreateDTO user) {
		return User.builder()
				.name(user.getName())
				.lastName(user.getLastName())
				.phone(user.getPhone())
				.build();
	} 
	
}
