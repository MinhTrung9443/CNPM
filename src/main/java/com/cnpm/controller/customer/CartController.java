package com.cnpm.controller.customer;


import com.cnpm.entity.Cart;
import com.cnpm.entity.CartItem;
import com.cnpm.repository.CartRepository;
import com.cnpm.service.ICartService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequestMapping("")
public class CartController {
    @Autowired
    private ICartService cartService; // Assume you have a service to handle cart logic

    @Autowired
    private CartRepository cartRepository;

    @GetMapping("/cart.html")
    public String showCart(Model model) {
        Optional<Cart> optionalCart = cartRepository.findByCustomerUserId(4L);
        if(optionalCart.isPresent()) {
            Cart cart = optionalCart.get();
            model.addAttribute("cart", cart);
            model.addAttribute("cartItems", cart.getCartItems());
            Double total = 0d;
            for(CartItem c : cart.getCartItems()) {
                total += c.getQuantity() * c.getProduct().getCost();
            }
            model.addAttribute("total", total);
            return "cart/index";
        }
        return "redirect:/index";
    }

    @PostMapping("/add-to-cart")
    public ResponseEntity<String> addProductToCart(@RequestParam Long productId) {
        // Assuming CartItem has a constructor that takes productId and other necessary parameters
        CartItem cartItem = new CartItem();
        cartService.addToCart(cartItem); // Add the cart item to the cart
        return ResponseEntity.ok("Product added to cart successfully");
    }

    @PostMapping("/remove-item")
    public ResponseEntity<String> removeItemFromCart(@RequestParam Long cartItemId) {
        Optional<CartItem> cartItemOptional = cartService.getCartItemById(cartItemId);
        if (cartItemOptional.isPresent()) {
            CartItem cartItem = cartItemOptional.get();
            cartService.removeItemFromCart(cartItem);
            return ResponseEntity.ok("Xoá thành công");
        } else {
            return ResponseEntity.badRequest().body("K tìm thấy");
        }
    }

    @PostMapping("/update-quantity")
    public ResponseEntity<String> updateQuantity(@RequestParam Long cartItemId, @RequestParam int quantity) {
        Optional<CartItem> cartItemOptional = cartService.getCartItemById(cartItemId);
        if (cartItemOptional.isPresent()) {
            CartItem cartItem = cartItemOptional.get();
            cartItem.setQuantity(quantity);
            cartService.updateCartItem(cartItem);
            return ResponseEntity.ok("Cập nhật thành công");
        } else {
            return ResponseEntity.badRequest().body("K tìm thấy");
        }
    }
}
