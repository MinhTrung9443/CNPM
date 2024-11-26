package com.cnpm.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cnpm.entity.CartItem;

@Service
public interface ICartService {

	Optional<CartItem> getCartItemById(Long cartItemId);

	void updateCartItem(CartItem cartItem);

	void removeItemFromCart(CartItem cartItem);

	void removeFromCart(Long productId);

	List<CartItem> getCartItems();

	void addToCart(CartItem item);

}
