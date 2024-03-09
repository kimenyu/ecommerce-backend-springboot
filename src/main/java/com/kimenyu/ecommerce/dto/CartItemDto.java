package com.kimenyu.ecommerce.dto;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class CartItemDto {
    private Long id;
    private Long productId;
    private Long cartId;
    private int quantity;
    private BigDecimal subTotal;
}