package com.luxuryshop.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "blog")
public class Blog extends ParentEntity{

	@Column(name = "title", nullable = true)
	private String title;
	
	@Column(name = "image", length = 100 ,nullable = true)
	private String image;
	
	@Column(name = "description" , columnDefinition = "TEXT",nullable = true)
	private String description;
}
