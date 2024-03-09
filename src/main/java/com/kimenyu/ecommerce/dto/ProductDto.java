package com.kimenyu.ecommerce.dto;

import lombok.Data;

@Data
public class ProductDto {
    private String name;
    private String description;
    private Double price;
    private String imageUrl;
    private Boolean isAvailable;
}

