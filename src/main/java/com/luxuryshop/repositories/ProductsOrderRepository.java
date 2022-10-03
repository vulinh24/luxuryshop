package com.luxuryshop.repositories;

import com.luxuryshop.entities.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Date;

public interface ProductsOrderRepository extends JpaRepository<OrderProduct, Integer> {

	@Query(value = "SELECT SUM(p.productPrice * p.quantity) FROM OrderProduct p WHERE p.selledDate = ?1")
	public Float countByDate(Date date);


}
