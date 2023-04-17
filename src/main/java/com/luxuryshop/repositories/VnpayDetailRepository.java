package com.luxuryshop.repositories;

import com.luxuryshop.entities.User;
import com.luxuryshop.entities.VnpayDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface VnpayDetailRepository extends JpaRepository<VnpayDetail, Integer> {


    @Query(value = "SELECT * FROM vnpay_detail WHERE order_id = ?1", nativeQuery = true)
    VnpayDetail findByOrderId(Integer orderId);
}
