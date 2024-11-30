package com.cnpm.service.impl;

import com.cnpm.entity.CartItem;
import com.cnpm.repository.CartItemRepository;
import com.cnpm.service.interfaces.ICartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartItemService implements ICartItemService {
    @Autowired
    private CartItemRepository cartItemRepository;

    @Override
    public CartItem getCartItemById(Long id) {
        return cartItemRepository.findById(id).orElseThrow(() -> new RuntimeException("Cart item not found"));
    }
}
