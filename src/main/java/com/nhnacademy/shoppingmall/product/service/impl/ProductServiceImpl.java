package com.nhnacademy.shoppingmall.product.service.impl;

import com.nhnacademy.shoppingmall.common.page.Page;
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
    public Product getProduct(String field, String keyword) {
        Optional<Product> product = productrepository.findByEverything(field, keyword);
        return product.orElse(null);
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

    @Override
    public int countAllProduct() {
        return productrepository.countAll();
    }

    @Override
    public Page<Product> pageAll(int page, int pageSize) {
        return productrepository.pageAll(page, pageSize);
    }
}
