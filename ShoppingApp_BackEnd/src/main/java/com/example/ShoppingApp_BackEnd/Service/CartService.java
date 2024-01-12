package com.example.ShoppingApp_BackEnd.Service;

import com.example.ShoppingApp_BackEnd.Data.Cart;
import com.example.ShoppingApp_BackEnd.Data.User;
import com.example.ShoppingApp_BackEnd.Repository.CartRepository;
import com.example.ShoppingApp_BackEnd.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;

    @Autowired
    public CartService(CartRepository cartRepository, UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
    }

    public List<Cart> getAllCarts() {
        return cartRepository.findAll();
    }

    public Optional<Cart> getCartById(Long cartId) {
        return cartRepository.findById(cartId);
    }

    private static final Logger log = LoggerFactory.getLogger(CartService.class);
    public Cart createCart(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID must be provided for creating a cart");
        }

        try {
            // Assuming you have a method to retrieve the user by ID
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

            Cart cart = new Cart();
            cart.setUser(user);

            log.info("Received request to create cart. User details: {}", user);
            // rest of the code to create the cart
            // ...

            // Assuming you have a method to save the cart and get the saved cart
            Cart savedCart = cartRepository.save(cart);
            return savedCart;
        } catch (Exception e) {
            log.error("Unexpected error creating cart", e);
            throw new RuntimeException("Unexpected error creating cart", e);
        }
    }

    public Cart updateCart(Long cartId, Cart updatedCart) {
        if (cartRepository.existsById(cartId)) {
            updatedCart.setId(cartId);
            return cartRepository.save(updatedCart);
        }
        return null; // Handle not found scenario
    }

    public void deleteCart(Long cartId) {
        cartRepository.deleteById(cartId);
    }


}
