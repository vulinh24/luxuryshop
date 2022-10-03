package com.luxuryshop.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product_detail")
public class ProductDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

    @Column(name = "dimension",nullable = true)
	private String dimension;

    @Column(name = "origin")
	private String origin;

    @Column(name = "material")
	private String material;

    @Column(name = "insurance")
	private String insurance;

    @JsonIgnore
	@OneToOne
	@JoinColumn(name = "product_id" , referencedColumnName = "id")
	private Product product;
}
