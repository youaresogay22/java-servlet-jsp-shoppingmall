package com.nhnacademy.shoppingmall.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "OrderDetails")
@NoArgsConstructor
@Getter
@Setter
@IdClass(OrderDetails.PrimaryKey.class)
public class OrderDetails {
    @Id
    @ManyToOne
    @JoinColumn(name = "OrderID")
    private Orders order;

    @Id
    @ManyToOne
    @JoinColumn(name = "ProductID")
    private Products product;

    private int Quantity;

    private BigDecimal UnitCost;

    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    @Getter
    @Setter
    public static class PrimaryKey implements Serializable {
        private int OrderID;
        private String ProductID;
    }
}
