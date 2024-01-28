package com.example.ShoppingApp_BackEnd.Controller;

import com.example.ShoppingApp_BackEnd.Data.Category;
import com.example.ShoppingApp_BackEnd.Data.Product;
import com.example.ShoppingApp_BackEnd.Service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PersistenceContext
    private EntityManager entityManager;


    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        if (product == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @GetMapping("/count")
    public Long getProductCount() {
        return productService.getProductCount();
    }

    @PostMapping
    public ResponseEntity<Product> saveProduct(@RequestPart("product") Product product, @RequestPart("photo") MultipartFile photo) {
        try {
            Product savedProduct = productService.createProductWithPhoto(product, photo);
            return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
        } catch (Exception e) {
            System.err.println("Error saving product with photo: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product updatedProduct) {
        Product existingProduct = productService.getProductById(id);


        BeanUtils.copyProperties(updatedProduct, existingProduct, "id", "category");


        if (updatedProduct.getCategory() != null && updatedProduct.getCategory().getId() != null) {

            Category existingCategory = entityManager.createQuery(
                            "SELECT c FROM Category c WHERE c.id = :categoryId", Category.class)
                    .setParameter("categoryId", updatedProduct.getCategory().getId())
                    .getSingleResult();


            existingProduct.setCategory(existingCategory);
        }

        return new ResponseEntity<>(productService.updateProduct(id, existingProduct), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

