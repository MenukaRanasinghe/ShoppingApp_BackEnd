package com.example.ShoppingApp_BackEnd.Repository;

import com.example.ShoppingApp_BackEnd.Data.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
