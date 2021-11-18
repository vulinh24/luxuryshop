package com.luxuryshop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.luxuryshop.entities.RateProduct;
import com.luxuryshop.entities.primarykey.PKOfCart;

@Repository
public interface RateRepository extends JpaRepository<RateProduct, PKOfCart>{
	
	@Query(value = "FROM RateProduct c WHERE c.pk.userId = ?1 AND c.pk.productId = ?2")
	public RateProduct findByUserProduct(Integer userId, Integer productId);
}
