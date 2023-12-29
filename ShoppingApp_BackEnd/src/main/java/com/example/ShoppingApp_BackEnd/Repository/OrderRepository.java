package com.example.ShoppingApp_BackEnd.Repository;

import com.example.ShoppingApp_BackEnd.Data.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}

