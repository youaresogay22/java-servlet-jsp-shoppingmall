package com.nhnacademy.shoppingmall.product.repository;

import com.nhnacademy.shoppingmall.product.domain.Product;

import java.util.Optional;

public interface ProductRepository {
    int save(Product product);

    int update(Product product);

    int deleteByProductId(String productId);

    Optional<Product> findByEverything(String field, String keyword);
}
