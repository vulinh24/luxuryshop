package com.luxuryshop.data.mapper;

import com.luxuryshop.data.response.CartResponse;
import com.luxuryshop.data.tables.pojos.Cart;
import com.luxuryshop.data.tables.pojos.Product;
import com.luxuryshop.data.tables.pojos.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class CartMapper extends AbsMapper<Cart, Cart, Cart> {

    public abstract CartResponse toResponse(Cart cart, User user, Product productCart);
}
