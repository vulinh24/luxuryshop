package com.luxuryshop.repositories;

import java.sql.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.luxuryshop.entities.SaledProduct;

public interface ProductsOrderRepository extends JpaRepository<SaledProduct, Integer>{
	
	@Query(value = "SELECT SUM(p.productPrice * p.quantity) FROM SaledProduct p WHERE p.selledDate = ?1")
	public Float countByDate(Date date);
	
	
}
