package com.nhnacademy.shoppingmall.product.execption;

public class ProductAlreadyExistsException extends RuntimeException {
    public ProductAlreadyExistsException(String ProductID) {
        super(String.format("product Already Exists:" + ProductID));
    }

}
