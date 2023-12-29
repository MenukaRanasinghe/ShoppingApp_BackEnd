package com.example.ShoppingApp_BackEnd.Repository;

import com.example.ShoppingApp_BackEnd.Data.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
