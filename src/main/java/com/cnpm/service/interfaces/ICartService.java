package com.cnpm.service.interfaces;

import java.util.List;
import java.util.Optional;

import com.cnpm.entity.Cart;
import org.springframework.stereotype.Service;

import com.cnpm.entity.CartItem;

@Service
public interface ICartService {

	Optional<CartItem> getCartItemById(Long cartItemId);

	void updateCartItem(CartItem cartItem);

	void removeItemFromCart(CartItem cartItem);

	void removeFromCart(Long productId);

	List<CartItem> getCartItems();

	Cart getCartByUserId(Long userId);

	void addToCart(CartItem item);

}
