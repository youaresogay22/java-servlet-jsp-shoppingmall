package com.nhnacademy.shoppingmall.product.execption;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(String ProductID) {
        super(String.format("product not found:" + ProductID));
    }
}
