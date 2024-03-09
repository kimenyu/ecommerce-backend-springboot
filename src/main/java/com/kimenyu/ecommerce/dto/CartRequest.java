package com.kimenyu.ecommerce.dto;


import lombok.Data;

@Data
public class CartRequest {
    private Long productId;
    private int quantity;
    private Long userId;
}
