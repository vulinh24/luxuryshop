package com.luxuryshop.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tag_search")
public class Tag {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "name" , length = 50)
	private String name;
	
	@Column(name="description",length = 100, nullable = true)
	private String description;
	
	public String upperName() {
		if (name != null)
			return name.toUpperCase();
		else return name;
	}
}
