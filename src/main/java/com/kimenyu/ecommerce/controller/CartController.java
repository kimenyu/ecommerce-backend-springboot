package com.kimenyu.ecommerce.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.kimenyu.ecommerce.dto.CartDto;
import com.kimenyu.ecommerce.dto.CartItemDto;
import com.kimenyu.ecommerce.dto.ProductDto;
import com.kimenyu.ecommerce.entity.Product;
import com.kimenyu.ecommerce.service.CartService;
import com.kimenyu.ecommerce.service.ProductService;

@RestController
@RequestMapping("/api/v1")
public class CartController {

    private final ProductService productService;
    private final CartService cartService;

    public CartController(ProductService productService, CartService cartService) {
        this.productService = productService;
        this.cartService = cartService;
    }

    @PostMapping("/products/add-to-cart/{productId}/{cartId}")
    public ResponseEntity<String> addProductToCart(@PathVariable Long productId, @PathVariable Long cartId) {
        Product productDto = productService.getProductById(productId);
        if (productDto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
        }

        cartService.addProductToCart(cartId, productDto);
        return ResponseEntity.ok("Product added to cart successfully");
    }

    @GetMapping("/carts/{cartId}")
    public ResponseEntity<List<CartItemDto>> getCartById(@PathVariable Long cartId) {
        List<CartItemDto> cartDto = cartService.getCartById(cartId);
        return ResponseEntity.ok(cartDto);
    }

    @GetMapping("/carts/{cartId}/items")
    public ResponseEntity<List<CartItemDto>> getCartItems(@PathVariable Long cartId) {
        List<CartItemDto> cartItems = cartService.getCartById(cartId);
        return ResponseEntity.ok(cartItems);
    }

    @PostMapping("/carts/{cartId}/items/{productId}")
    public ResponseEntity<String> addCartItem(@PathVariable Long cartId, @PathVariable Long productId) {
        cartService.addCartItem(cartId, productId);
        return ResponseEntity.ok("Cart item added successfully");
    }

    @PutMapping("/carts/{cartId}/items/{cartItemId}")
    public ResponseEntity<String> updateCartItem(@PathVariable Long cartId, @PathVariable Long cartItemId, @RequestBody CartItemDto cartItemDto) {
        cartService.updateCartItem(cartId, cartItemId, cartItemDto);
        return ResponseEntity.ok("Cart item updated successfully");
    }

    @DeleteMapping("/carts/{cartId}/items/{cartItemId}")
    public ResponseEntity<String> removeCartItem(@PathVariable Long cartId, @PathVariable Long cartItemId) {
        cartService.removeCartItem(cartId, cartItemId);
        return ResponseEntity.ok("Cart item removed successfully");
    }
}
