package com.nhnacademy.shoppingmall.product.repository;

import com.nhnacademy.shoppingmall.common.page.Page;
import com.nhnacademy.shoppingmall.product.domain.Product;

import java.util.Optional;

public interface ProductRepository {
    public int save(Product product);

    public int update(Product product);

    public int updateByProductId(String productId);

    public int deleteByProductId(String productId);

    public Optional<Product> findByEverything(String field, String keyword);

    public Page<Product> pageAll(int page, int pageSize);

    public int countAll();

    public int countByProductId(String userId);
}
