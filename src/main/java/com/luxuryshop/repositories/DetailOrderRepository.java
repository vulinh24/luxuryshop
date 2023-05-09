package com.luxuryshop.repositories;

import com.luxuryshop.entities.Order;
import com.luxuryshop.entities.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface DetailOrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByUser(User user, Sort sort);

    Optional<Order> findById(Integer id);

    @Query(value = "SELECT sum(total_received) FROM order_detail \n" +
            "WHERE order_detail.status = 'Đang giao hàng' and date(updated_date) = ?1\n" , nativeQuery = true)
    Float revenueAtDate(Date date);
}
