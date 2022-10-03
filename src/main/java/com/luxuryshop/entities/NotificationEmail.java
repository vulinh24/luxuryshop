package com.luxuryshop.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "mail_register")
public class NotificationEmail extends ParentEntity {


	@Column(name = "email", length = 45, nullable = false)
	private String email;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	
}
