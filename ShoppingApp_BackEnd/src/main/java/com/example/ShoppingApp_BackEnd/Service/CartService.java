package com.example.ShoppingApp_BackEnd.Service;

import com.example.ShoppingApp_BackEnd.Data.Cart;
import com.example.ShoppingApp_BackEnd.Data.User;
import com.example.ShoppingApp_BackEnd.Repository.CartRepository;
import com.example.ShoppingApp_BackEnd.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public Cart createCart(Cart cart) {
        // Fetch the user by id from the database
        Optional<User> userOptional = userRepository.findById(cart.getUser().getId());

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            cart.setUser(user);  // Set the user for the cart
            return cartRepository.save(cart);
        } else {
            // Handle the case where no user is found with the provided id
            throw new RuntimeException("User not found with id: " + cart.getUser().getId());
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

    // Add additional methods if needed
}
