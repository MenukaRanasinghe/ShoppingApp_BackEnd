package com.example.ShoppingApp_BackEnd.Controller;

import com.example.ShoppingApp_BackEnd.Data.Cart;
import com.example.ShoppingApp_BackEnd.Service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/carts")
public class CartController {

    private final CartService cartService;
    private static final Logger log = LoggerFactory.getLogger(CartController.class);

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public List<Cart> getAllCarts() {
        return cartService.getAllCarts();
    }

    @GetMapping("/{cartId}")
    public Cart getCartById(@PathVariable Long cartId) {
        return cartService.getCartById(cartId).orElse(null); // Handle not found scenario
    }

    @PostMapping
    public ResponseEntity<Cart> createCart(@RequestBody Map<String, Long> request) {
        try {
            Long userId = request.get("userId");
            if (userId == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            log.info("Received request to create cart. User ID: {}", userId);
            Cart createdCart = cartService.createCart(userId);
            return new ResponseEntity<>(createdCart, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            log.error("Error creating cart: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Unexpected error creating cart", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @PutMapping("/{cartId}")
    public Cart updateCart(@PathVariable Long cartId, @RequestBody Cart updatedCart) {
        return cartService.updateCart(cartId, updatedCart);
    }

    @DeleteMapping("/{cartId}")
    public void deleteCart(@PathVariable Long cartId) {
        cartService.deleteCart(cartId);
    }
}
