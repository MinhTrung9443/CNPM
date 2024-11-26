package com.cnpm.service.impl;

import com.cnpm.entity.Cart;
import com.cnpm.entity.CartItem;
import com.cnpm.entity.Customer;
import com.cnpm.repository.CartItemRepository;
import com.cnpm.repository.CartRepository;
import com.cnpm.repository.UserRepository;
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
    @Autowired
    CartRepository cartRepository;
    @Autowired
    UserRepository userRepository;

    public Cart getCartByUserId(Long userId) {
//        neu k có thi tao moi va luu vao db
        return cartRepository.findByCustomerUserId(userId).orElseGet(() -> {
            Cart newCart = new Cart();
            Customer customer = (Customer) userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
            newCart.setCustomer(customer);
            return cartRepository.save(newCart);
        });
    }
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

    public void clearCustomerCart(Long userId) {
        Cart cart = cartRepository.findByCustomerUserId(userId).orElseThrow(() -> new RuntimeException("Cart not found"));
        cart.getCartItems().clear();
        cartRepository.save(cart);
    }

//    public double getTotalCost() {
//        return cartItems.stream().mapToDouble(item -> item.getCost() * item.getQuantity()).sum();
//    }
}
