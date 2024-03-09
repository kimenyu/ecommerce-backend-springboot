package com.kimenyu.ecommerce.entity;

import lombok.Data;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "cart_items")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    

    @Column(nullable = false)
    private int quantity;

    // Calculate subtotal based on quantity and price
    public BigDecimal calculateSubTotal() {
        BigDecimal price = BigDecimal.valueOf(product.getPrice());
        return price.multiply(BigDecimal.valueOf(quantity));
    }

    //isempty
    public boolean isEmpty() {
        return quantity == 0;
    }
}


    

    

