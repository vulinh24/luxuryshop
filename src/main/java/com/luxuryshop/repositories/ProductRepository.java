package com.luxuryshop.repositories;

import com.luxuryshop.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findByIsHotTrue();

    List<Product> findByIsNewTrue();

    List<Product> findByIsSaleTrue();

    List<Product> findBySeo(String seo);

    List<Product> findByTitleContainingIgnoreCase(String title);
}
