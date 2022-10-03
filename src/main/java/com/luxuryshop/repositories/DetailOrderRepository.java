package com.luxuryshop.repositories;

import com.luxuryshop.entities.Order;
import com.luxuryshop.entities.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetailOrderRepository extends JpaRepository<Order, Integer> {
    public List<Order> findByUser(User user, Sort sort);
}
