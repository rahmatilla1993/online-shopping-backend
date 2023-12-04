package com.example.onlineshopping.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(
        name = "order_details",
        uniqueConstraints = {@UniqueConstraint(columnNames = {
                "product_id", "order_id"
        })}
)
public class OrderDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int count;

    private float totalSum;

    @ManyToOne(optional = false)
    private Product product;

    @ManyToOne(optional = false)
    private Order order;
}
