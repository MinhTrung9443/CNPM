package com.cnpm.service.impl;

import com.cnpm.entity.CartItem;
import com.cnpm.repository.CartItemRepository;
import com.cnpm.service.ICartService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartService implements ICartService {
	@Autowired
    private CartItemRepository cartItemRepository;
    private List<CartItem> cartItems = new ArrayList<>();

    @Override
	public void addToCart(CartItem item) {
        cartItems.add(item);
    }

    @Override
	public List<CartItem> getCartItems() {
        return cartItems;
    }

    @Override
	public void removeFromCart(Long productId) {
        cartItems.removeIf(item -> item.getProduct().getProductId().equals(productId));
    }

    @Override
	public void removeItemFromCart(CartItem cartItem) {
        cartItemRepository.delete(cartItem);
    }

    // Update cart item quantity
    @Override
	public void updateCartItem(CartItem cartItem) {
        cartItemRepository.save(cartItem);
    }

    // Get a cart item by ID
    @Override
	public Optional<CartItem> getCartItemById(Long cartItemId) {
        return cartItemRepository.findById(cartItemId);
    }

//    public double getTotalCost() {
//        return cartItems.stream().mapToDouble(item -> item.getCost() * item.getQuantity()).sum();
//    }
}
