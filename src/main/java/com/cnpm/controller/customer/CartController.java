package com.cnpm.controller.customer;

import com.cnpm.entity.Cart;
import com.cnpm.entity.CartItem;
import com.cnpm.entity.Customer;
import com.cnpm.entity.Product;
import com.cnpm.entity.User;
import com.cnpm.repository.CartRepository;
import com.cnpm.repository.CustomerRepository;
import com.cnpm.repository.ProductRepository;
import com.cnpm.service.ICartService;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashSet;
import java.util.Optional;

@Controller
@RequestMapping({ "", "*/" })
public class CartController {
	@Autowired
    private ICartService cartService; // Assume you have a service to handle cart logic

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @GetMapping("/cart.html")
    public String showCart(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        Optional<Cart> optionalCart = cartRepository.findByCustomerUserId(user.getUserId());
        if (optionalCart.isPresent()) {
            Cart cart = optionalCart.get();
            model.addAttribute("cart", cart);
            model.addAttribute("cartItems", cart.getCartItems());
            Double total = 0d;
            for (CartItem c : cart.getCartItems()) {
                total += c.getQuantity() * c.getProduct().getCost();
            }
            model.addAttribute("total", total);
            return "customer/cart";
        }
        return "redirect:/index";
    }

    @PostMapping("/add-to-cart")
    public String addProductToCart(@RequestParam Long productId, @RequestParam int quantity, HttpSession session) {
        User user = (User) session.getAttribute("user");

        Optional<Cart> optionalCart = cartRepository.findByCustomerUserId(user.getUserId());
        Cart cart;
        if (optionalCart.isPresent()) {
            cart = optionalCart.get();
        } else {
            cart = new Cart();
            Customer customer = customerRepository.findByUserId(user.getUserId());
            cart.setCustomer(customer);
            cart.setCartItems(new HashSet<>());
            cart = cartRepository.save(cart);
        }
        Optional<Product> productOptional = productRepository.findById(productId);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();

            if (quantity > 0) {
                Optional<CartItem> existingCartItem = cart.getCartItems().stream()
                        .filter(item -> item.getProduct().getProductId().equals(productId))
                        .findFirst();

                if (existingCartItem.isPresent()) {
                    CartItem cartItem = existingCartItem.get();
                    cartItem.setQuantity(cartItem.getQuantity() + quantity);
                } else {
                    CartItem cartItem = new CartItem();
                    cartItem.setProduct(product);
                    cartItem.setCart(cart);
                    cartItem.setQuantity(quantity);
                    cart.getCartItems().add(cartItem);
                }

                cartRepository.save(cart);

                return "redirect:/cart.html";
            } else {
                return "redirect:/product/" + productId + "?error=invalidQuantity";
            }
        }
        return "redirect:/index";
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
