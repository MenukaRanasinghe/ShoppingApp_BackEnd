package com.example.ShoppingApp_BackEnd.Repository;

import com.example.ShoppingApp_BackEnd.Data.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

}
