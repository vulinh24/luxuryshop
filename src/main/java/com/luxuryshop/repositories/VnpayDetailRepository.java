package com.luxuryshop.repositories;

import com.luxuryshop.entities.VnpayDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VnpayDetailRepository extends JpaRepository<VnpayDetail, Integer> {
}
