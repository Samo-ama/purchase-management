package com.example.purchase.management.service;

import com.example.purchase.management.entity.Product;
import java.util.List;

public interface ProductService {

    Product createProduct(Product product);

    Product updateProduct(Long id, Product product);

    void deleteProduct(Long id);

    List<Product> getAllProducts();

}
