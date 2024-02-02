package com.nhnacademy.shoppingmall.product.service;

import com.nhnacademy.shoppingmall.product.domain.Product;

import java.util.Optional;

public interface ProductService {
    Optional<Product> getProduct(String field, String keyword);

    void saveProduct(Product product);

    void updateProduct(Product product);

    void deleteProduct(String productId);
}
