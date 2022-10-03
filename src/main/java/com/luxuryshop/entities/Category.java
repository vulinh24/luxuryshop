package com.luxuryshop.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "category")
public class Category extends ParentEntity {

    @Column(name = "name", length = 100, nullable = false)
	private String name;

	@Lob
	@Column(name = "description", nullable = true, columnDefinition = "LONGTEXT")
	private String description;

	@Column(name = "seo", nullable = true)
	private String seo;

	@OneToMany(mappedBy = "category")
	List<Product> products;
	
	@PreRemove
	void setForeignKeyNull() {
		List<Product> pros = this.getProducts();
		for (Product p : pros) {
			p.setCategory(null);
		}
	}
}
