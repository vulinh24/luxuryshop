package com.luxuryshop.repositories;

import com.luxuryshop.entities.ShopLog;
import com.luxuryshop.model.Statistic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShopLogRepository extends JpaRepository<ShopLog, Integer> {

    @Query(value = "SELECT product_id FROM `shop_log` \n" +
            "WHERE owner_id = ?1\n" +
            "GROUP by product_id\n" +
            "order by sum(point) desc\n" +
            "LIMIT 10", nativeQuery = true)
    List<Integer> getSuggestProductIdForUserId(Integer userId);

    @Query(value = "SELECT product_id as `key`, count(*) as `value` FROM `shop_log` \n" +
            "WHERE action = 'view'\n" +
            "GROUP by product_id\n" +
            "order by value desc\n" +
            "LIMIT 20", nativeQuery = true)
    List<Statistic> getMostViewedProduct();
}
