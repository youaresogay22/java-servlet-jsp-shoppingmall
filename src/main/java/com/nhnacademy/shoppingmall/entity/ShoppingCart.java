package com.nhnacademy.shoppingmall.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ShoppingCart")
@NoArgsConstructor
@Getter
@Setter
public class ShoppingCart {
    @Id
    @Column(name = "RecordID")
    private int recordId;

    @Column(name = "CartID")
    private String cartId;

    @Column(name = "Quantity")
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "ProductID")
    private Products product;

    @Column(name = "DateCreateed")
    private LocalDateTime dateCreated;
}
