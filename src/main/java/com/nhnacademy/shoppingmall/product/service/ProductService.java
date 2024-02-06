package com.nhnacademy.shoppingmall.product.service;

import com.nhnacademy.shoppingmall.common.page.Page;
import com.nhnacademy.shoppingmall.product.domain.Product;

import java.util.Optional;

public interface ProductService {
    public Product getProduct(String field, String keyword);

    public void saveProduct(Product product);

    public void updateProduct(Product product);

    public void deleteProduct(String productId);

    public int countAllProduct();

    public Page<Product> pageAll(int page, int pageSize);
}
