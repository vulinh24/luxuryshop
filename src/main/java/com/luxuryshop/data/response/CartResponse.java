package com.luxuryshop.data.response;

import com.luxuryshop.data.tables.pojos.Product;
import com.luxuryshop.data.tables.pojos.User;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CartResponse {
    private Integer userId;
    private Integer productId;
    private User user;
    private Product productCart;
    private Integer quantity;
}
