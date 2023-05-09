package com.luxuryshop.repositories;

import com.luxuryshop.entities.OrderProduct;
import com.luxuryshop.model.Statistic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Date;
import java.util.List;

public interface ProductsOrderRepository extends JpaRepository<OrderProduct, Integer> {

	@Query(value = "SELECT SUM(p.productPrice * p.quantity) FROM OrderProduct p WHERE p.selledDate = ?1 and p.order.status = 'Đang giao hàng'")
	public Float countByDate(Date date);

	@Query(value = "SELECT product_id as `key`, SUM(quantity) as `value` FROM order_product join order_detail on order_product.order_id = order_detail.id \n" +
			"WHERE order_detail.status = 'Đang giao hàng'\n" +
			"GROUP by order_product.product_id\n" +
			"order by sum(quantity) DESC LIMIT ?1", nativeQuery = true)
	List<Statistic> statisticBestSellingProduct(int limit);

	@Query(value = "SELECT sum(quantity) FROM order_product join order_detail on order_product.order_id = order_detail.id \n" +
			"WHERE order_detail.status = 'Đang giao hàng' and order_product.sold_date = ?1\n" , nativeQuery = true)
	Integer countSoldProductAtDate(Date date);
}
