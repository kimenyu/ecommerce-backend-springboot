package com.kimenyu.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.kimenyu.ecommerce.common.ApiResponse;
import com.kimenyu.ecommerce.service.AuthService;
import com.kimenyu.ecommerce.service.CartService;
import com.kimenyu.ecommerce.service.ProductService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import com.kimenyu.ecommerce.dto.cart.AddToCartDto;
import com.kimenyu.ecommerce.dto.cart.CartDto;
import com.kimenyu.ecommerce.entity.User;
import com.kimenyu.ecommerce.entity.Product;
import com.kimenyu.ecommerce.exceptions.AuthenticationFailException;
import com.kimenyu.ecommerce.exceptions.CartItemNotExistException;
import com.kimenyu.ecommerce.exceptions.ProductNotExistException;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

    @Autowired
    private AuthService authService;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addToCart(@RequestBody AddToCartDto addToCartDto,
                                                 @RequestHeader("Authorization") String token) {
        try {
            token = token.substring(7); // Remove "Bearer " prefix from token
            User user = authService.getUserFromToken(token);
            if (user == null) {
                return new ResponseEntity<>(new ApiResponse(false, "User not authenticated"), HttpStatus.UNAUTHORIZED);
            }

            Product product = productService.getProductById(addToCartDto.getProductId());
            cartService.addToCart(addToCartDto, product, user);
            return new ResponseEntity<>(new ApiResponse(true, "Added to cart"), HttpStatus.CREATED);
        } catch (ProductNotExistException e) {
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/")
    public ResponseEntity<CartDto> getCartItems(@RequestHeader("Authorization") String token) {
        try {
            token = token.substring(7);
            User user = authService.getUserFromToken(token);
            CartDto cartDto = cartService.listCartItems(user);
            return new ResponseEntity<>(cartDto, HttpStatus.OK);
        } catch (AuthenticationFailException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @PutMapping("/update/{cartItemId}")
    public ResponseEntity<ApiResponse> updateCartItem(@RequestBody AddToCartDto cartDto,
                                                      @PathVariable("cartItemId") int cartItemId,
                                                      @RequestHeader("Authorization") String token) {
        try {
            token = token.substring(7);
            User user = authService.getUserFromToken(token);
            Product product = productService.getProductById(cartDto.getProductId());
            cartService.updateCartItem(cartDto, user, product);
            return new ResponseEntity<>(new ApiResponse(true, "Product has been updated"), HttpStatus.OK);
        } catch (AuthenticationFailException | ProductNotExistException | CartItemNotExistException e) {
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), HttpStatus.UNAUTHORIZED);
        }
    }

    @DeleteMapping("/delete/{cartItemId}")
    public ResponseEntity<ApiResponse> deleteCartItem(@PathVariable("cartItemId") int cartItemId,
                                                      @RequestHeader("Authorization") String token) {
        try {
            token = token.substring(7);
            User user = authService.getUserFromToken(token);
            cartService.deleteCartItem(cartItemId, user.getId());
            return new ResponseEntity<>(new ApiResponse(true, "Item has been removed"), HttpStatus.OK);
        } catch (AuthenticationFailException | CartItemNotExistException e) {
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), HttpStatus.UNAUTHORIZED);
        }
    }
}
