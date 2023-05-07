package com.luxuryshop.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "discount")
public class Discount extends ParentEntity implements Serializable {

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "discount", nullable = false)
    private float discount;

    @Column(name = "remain", nullable = false)
    private int remain;
}
