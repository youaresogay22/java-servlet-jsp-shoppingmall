package com.nhnacademy.shoppingmall.product.execption;

public class CouldNotSaveProductException extends RuntimeException {
    public CouldNotSaveProductException(String ProductID) {
        super(String.format("product save Exception:" + ProductID));
    }

}
