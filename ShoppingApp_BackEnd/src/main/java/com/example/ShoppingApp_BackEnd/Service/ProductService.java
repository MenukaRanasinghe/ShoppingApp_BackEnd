package com.example.ShoppingApp_BackEnd.Service;

import com.example.ShoppingApp_BackEnd.Data.Product;
import com.example.ShoppingApp_BackEnd.Repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    @Value("${upload.path}")
    private String uploadPath;

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

    @Transactional
    public Product createProductWithPhoto(Product product) {
        if (product.getPhoto() != null && product.getPhoto().length > 0) {
            return productRepository.save(product);
        } else {
            throw new IllegalArgumentException("Photo is required");
        }
    }

    @Transactional
    public Product updateProduct(Long id, Product updatedProduct) {
        Product existingProduct = getProductById(id);

        // Manually set properties excluding id
        existingProduct.setName(updatedProduct.getName());
        existingProduct.setDescription(updatedProduct.getDescription());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setQuantity(updatedProduct.getQuantity());
        existingProduct.setSizes(updatedProduct.getSizes());
        existingProduct.setColour(updatedProduct.getColour());

        if (updatedProduct.getPhoto() != null && updatedProduct.getPhoto().length > 0) {
            // Update photo only if provided
            existingProduct.setPhoto(updatedProduct.getPhoto());
        }

        try {
            BeanUtils.copyProperties(updatedProduct, existingProduct, getNullPropertyNames(updatedProduct));
        } catch (IllegalArgumentException e) {
            // Handle the case where an empty value is encountered during copy
            throw new IllegalArgumentException("Empty values are not allowed", e);
        }

        return productRepository.save(existingProduct);
    }

    private String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }

        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    public void deleteProduct(Long id) {
        Product product = getProductById(id);
        productRepository.delete(product);
    }
}
