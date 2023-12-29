package com.example.ShoppingApp_BackEnd.Repository;

import com.example.ShoppingApp_BackEnd.Data.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
    // Add custom query methods if needed
}
