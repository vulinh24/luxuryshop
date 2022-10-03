package com.luxuryshop.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;

/**
 * bảng lưu những sản phẩm đã bán trong hóa đơn
 *
 * @author LinhVu
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "order_product")
public class OrderProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "product_title", nullable = false)
    private String productTitle;

    @Column(name = "product_price", nullable = false)
    private Float productPrice;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "sold_date")
    private Date selledDate;

    @Column(name = "product_id")
    private Integer productId;

    @ManyToOne
    @JoinColumn(name = "order_id", columnDefinition = "integer default -1")
    private Order order;
}
