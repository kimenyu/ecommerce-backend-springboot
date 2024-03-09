package com.kimenyu.ecommerce.service;

import java.util.List;

import com.kimenyu.ecommerce.dto.CartDto;
import com.kimenyu.ecommerce.dto.CartItemDto;
import com.kimenyu.ecommerce.dto.ProductDto;
import com.kimenyu.ecommerce.entity.Product;

public interface CartService {

    List<CartItemDto> getCartById(Long cartId);

    void addCartItem(Long cartId, Long productId);

    void updateCartItem(Long cartId, Long cartItemId, CartItemDto cartItemDto);

    void removeCartItem(Long cartId, Long cartItemId);

    List <CartItemDto> getCartItems(Long cartId);

	void addProductToCart(Long cartId, Product productDto);
}
