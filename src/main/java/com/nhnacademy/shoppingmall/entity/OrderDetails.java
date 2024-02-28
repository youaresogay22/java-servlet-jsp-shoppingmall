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
public class OrderDetails {
    @EmbeddedId
    private PrimaryKey primaryKey;

    private int Quantity;

    private BigDecimal UnitCost;

    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    @Getter
    @Setter
    @Embeddable
    public static class PrimaryKey implements Serializable {
        @ManyToOne
        @JoinColumn(name = "OrderID")
        private Orders order;

        @ManyToOne
        @JoinColumn(name = "ProductID")
        private Products product;
    }
}
