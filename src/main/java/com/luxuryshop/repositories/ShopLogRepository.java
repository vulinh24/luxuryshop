package com.luxuryshop.repositories;

import com.luxuryshop.entities.ShopLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopLogRepository extends JpaRepository<ShopLog, Integer> {
}
