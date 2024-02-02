package com.nhnacademy.shoppingmall.product.service.impl;

import com.nhnacademy.shoppingmall.product.domain.Product;
import com.nhnacademy.shoppingmall.product.repository.ProductRepository;
import com.nhnacademy.shoppingmall.product.service.ProductService;

import java.util.Optional;

public class ProductServiceImpl implements ProductService {
    private final ProductRepository productrepository;

    public ProductServiceImpl(ProductRepository productrepository) {
        this.productrepository = productrepository;
    }

    @Override
    public Optional<Product> getProduct(String field, String keyword) {
        return productrepository.findByEverything(field, keyword);
    }

    @Override
    public void saveProduct(Product product) {
        productrepository.save(product);
    }

    @Override
    public void updateProduct(Product product) {
        productrepository.update(product);
    }

    @Override
    public void deleteProduct(String productId) {
        productrepository.deleteByProductId(productId);
    }
}
