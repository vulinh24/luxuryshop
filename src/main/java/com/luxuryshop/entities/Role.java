package com.luxuryshop.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "role")
public class Role extends ParentEntity implements Serializable {

	@Column(name = "name", length = 45, nullable = false)
	private String name;

	@Column(name = "description", length = 45, nullable = true)
	private String description;

	@ManyToMany(mappedBy = "roles", fetch = FetchType.EAGER)
	List<User> users;
	
}
