package com.luxuryshop.service;

import com.luxuryshop.data.tables.pojos.Cart;
import com.luxuryshop.repository.JCartRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {

    private final JCartRepository cartRepository;

    public CartService(JCartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public List<Cart> getByUserName(String username) {
        return cartRepository.findByUserName(username);
    }
}
