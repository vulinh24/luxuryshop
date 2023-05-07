package com.luxuryshop.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "discount")
public class Discount extends ParentEntity {

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "discount", nullable = false)
    private Float discount;

    @Column(name = "remain", nullable = false)
    private Integer remain;
}
