package com.luxuryshop.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "collection")
public class Collection extends ParentEntity {

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "description", nullable = true, columnDefinition = "TEXT")
    private String description;

    @Column(name = "seo", nullable = true)
    private String seo;

    @OneToMany(mappedBy = "collection")
    List<Product> products;
	
	@PreRemove
	void setForeignKeyNull() {
		List<Product> pros = this.getProducts();
		for (Product p : pros) {
			p.setCollection(null);
		}
	}
	
}
