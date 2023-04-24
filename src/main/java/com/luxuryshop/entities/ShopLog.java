package com.luxuryshop.entities;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "shop_log")
public class ShopLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "owner_id", nullable = true)
    private Integer ownerId;

    @Column(name = "product_id")
    private Integer productId;

    @Column(name = "action")
    private String action;

    @Column(name = "point")
    private Integer point;

    @Column(name = "created_time")
    private LocalDateTime createdTime;

    public ShopLog() {
        this.createdTime = LocalDateTime.now();
    }
}
