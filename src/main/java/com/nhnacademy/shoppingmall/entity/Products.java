package com.nhnacademy.shoppingmall.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "Products")
@NoArgsConstructor
@Getter
@Setter
public class Products {
    @Id
    private String ProductID;
    private String ModelNumber;
    private String ModelName;
    private String ProductThumbnail;
    private String ProductImage;
    private BigDecimal UnitCost;
    private int UnitQuantity;
    private String Description;

}
