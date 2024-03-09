package com.kimenyu.ecommerce.service;

import java.util.List;

import com.kimenyu.ecommerce.dto.ProductDto;
import com.kimenyu.ecommerce.entity.Product;

public interface ProductService {

	void createProduct(ProductDto productDTO);

    List<ProductDto> getAllProducts();

    void updateProduct(Long productId, ProductDto productDTO);

    Product getProductById(Long productId);

    ProductDto getProductByName(String productName);

    void deleteProduct(Long productId);

    

}
