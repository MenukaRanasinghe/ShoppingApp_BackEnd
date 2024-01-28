package com.example.ShoppingApp_BackEnd.Service;

import com.example.ShoppingApp_BackEnd.Data.Category;
import com.example.ShoppingApp_BackEnd.Data.Product;
import com.example.ShoppingApp_BackEnd.Repository.CategoryRepository;
import com.example.ShoppingApp_BackEnd.Repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Long getProductCount() {
        return productRepository.count();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));
    }

    @Value("${upload.path}")
    private String uploadPath;

    public Product createProductWithPhoto(Product product, MultipartFile photo) {
        if (photo != null && !photo.isEmpty()) {
            String fileName = StringUtils.cleanPath(photo.getOriginalFilename());
            product.setPhoto(fileName);

            try {
                Path targetLocation = Path.of(uploadPath).resolve(fileName);
                Files.copy(photo.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

                System.out.println("File successfully saved: " + fileName);
            } catch (IOException ex) {
                System.err.println("Failed to store file: " + fileName);
                throw new RuntimeException("Failed to store file: " + fileName, ex);
            }
        }

        return productRepository.save(product);
    }

    @Transactional
    public Product updateProduct(Long id, Product updatedProduct) {
        Product existingProduct = getProductById(id);


        existingProduct.setName(updatedProduct.getName());
        existingProduct.setDescription(updatedProduct.getDescription());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setQuantity(updatedProduct.getQuantity());
        existingProduct.setSizes(updatedProduct.getSizes());
        existingProduct.setColour(updatedProduct.getColour());
        existingProduct.setPhoto(updatedProduct.getPhoto());


        if (updatedProduct.getCategory() != null && updatedProduct.getCategory().getId() != null) {
            Category updatedCategory = categoryRepository.findById(updatedProduct.getCategory().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + updatedProduct.getCategory().getId()));
            existingProduct.setCategory(updatedCategory);
        } else {
            existingProduct.setCategory(null);
        }


        return productRepository.save(existingProduct);
    }

    public void deleteProduct(Long id) {
        Product product = getProductById(id);
        productRepository.delete(product);
    }
}
