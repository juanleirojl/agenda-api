package com.bnext.agenda.data.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name= "contacts")
@EqualsAndHashCode(callSuper = false)
public class Contact extends BaseEntity {
		
	@Column(name ="contact_name")
	private String contactName;
	
	@Column(name = "phone")
	private String phone;
	
	@JsonIgnore
	@ManyToOne(targetEntity = User.class)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;
	
}
