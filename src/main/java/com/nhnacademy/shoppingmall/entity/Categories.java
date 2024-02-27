package com.nhnacademy.shoppingmall.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "Categories")
@NoArgsConstructor
@Getter
@Setter
public class Categories {
    @Id
    private int CategoryID;

    private String CategoryName;

    @ManyToOne
    @JoinColumn(name = "ProductID")
    private Products product;
}
