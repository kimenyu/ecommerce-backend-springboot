package com.kimenyu.ecommerce.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class CartDto {
    private Long id;
    private Long userId;
    private List<CartItemDto> cartItems;
    private BigDecimal totalPrice;
}
