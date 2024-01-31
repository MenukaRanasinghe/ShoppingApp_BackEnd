package com.example.ShoppingApp_BackEnd.Controller;

import com.example.ShoppingApp_BackEnd.Data.Product;
import com.example.ShoppingApp_BackEnd.Service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.Set;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @GetMapping("/count")
    public Long getProductCount() {
        return productService.getProductCount();
    }

    @PostMapping
    public ResponseEntity<?> saveProduct(@RequestBody Product product) {
        try {
            // Check if the product name is not empty
            if (StringUtils.isEmpty(product.getName())) {
                return new ResponseEntity<>("Product name cannot be empty", HttpStatus.BAD_REQUEST);
            }

            // Save the product with the photo
            Product savedProduct = productService.createProductWithPhoto(product);
            return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);

        } catch (Exception e) {
            System.err.println("Error saving product with photo: " + e.getMessage());
            return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @RequestBody Product updatedProduct) {
        try {
            Product updatedProductResult = productService.updateProduct(id, updatedProduct);
            return new ResponseEntity<>(updatedProductResult, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace(); // For debugging purposes, you can remove it in production
            String errorMessage = "Internal Server Error: " + e.getMessage();
            return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
