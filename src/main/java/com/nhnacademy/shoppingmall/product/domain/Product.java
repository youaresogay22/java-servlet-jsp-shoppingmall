package com.nhnacademy.shoppingmall.product.domain;

import com.nhnacademy.shoppingmall.user.domain.User;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Blob;
import java.util.Objects;

@Setter
@Getter
public class Product {
    private String productId;
    private String modelNumber;
    private String modelName;
    private String productThumbNail;
    private String productImage;
    private BigDecimal unitCost;
    private int unitQuantity;
    private String description;

    public Product(String productId, String modelNumber, String modelName, String productThumbNail, String productImage, BigDecimal unitCost, int unitQuantity, String description) {
        this.productId = productId;
        this.modelNumber = modelNumber;
        this.modelName = modelName;
        this.productThumbNail = productThumbNail;
        this.productImage = productImage;
        this.unitCost = unitCost;
        this.unitQuantity = unitQuantity;
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return unitQuantity == product.unitQuantity && Objects.equals(productId, product.productId) && Objects.equals(modelNumber, product.modelNumber) && Objects.equals(modelName, product.modelName) && Objects.equals(productThumbNail, product.productThumbNail) && Objects.equals(productImage, product.productImage) && Objects.equals(unitCost, product.unitCost) && Objects.equals(description, product.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, modelNumber, modelName, productThumbNail, productImage, unitCost, unitQuantity, description);
    }

    @Override
    public String toString() {
        return "Product{" +
                "productId='" + productId + '\'' +
                ", modelNumber='" + modelNumber + '\'' +
                ", modelName='" + modelName + '\'' +
                ", productThumbNail=" + productThumbNail +
                ", productImage=" + productImage +
                ", unitCost=" + unitCost +
                ", unitQuantity=" + unitQuantity +
                ", description='" + description + '\'' +
                '}';
    }
}


