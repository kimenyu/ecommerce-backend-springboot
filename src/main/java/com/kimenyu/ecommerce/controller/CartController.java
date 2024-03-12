package com.kimenyu.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.kimenyu.ecommerce.common.ApiResponse;
import com.kimenyu.ecommerce.service.AuthService; // Updated import
import com.kimenyu.ecommerce.service.CartService;
import com.kimenyu.ecommerce.service.ProductService;
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
    private AuthService authService; // Inject AuthService

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addToCart(@RequestBody AddToCartDto addToCartDto,
                                                 @RequestParam("token") String token) throws AuthenticationFailException, ProductNotExistException {
        User user = authService.getUserFromToken(token); // Use AuthService to get user
        Product product = productService.getProductById(addToCartDto.getProductId());
        System.out.println("product to add: " + product.getName());
        cartService.addToCart(addToCartDto, product, user);
        return new ResponseEntity<>(new ApiResponse(true, "Added to cart"), HttpStatus.CREATED);
    }

    @GetMapping("/")
    public ResponseEntity<CartDto> getCartItems(@RequestParam("token") String token) throws AuthenticationFailException {
        User user = authService.getUserFromToken(token); // Use AuthService to get user
        CartDto cartDto = cartService.listCartItems(user);
        return new ResponseEntity<>(cartDto, HttpStatus.OK);
    }

    @PutMapping("/update/{cartItemId}")
    public ResponseEntity<ApiResponse> updateCartItem(@RequestBody AddToCartDto cartDto,
                                                      @RequestParam("token") String token) throws AuthenticationFailException, ProductNotExistException {
        User user = authService.getUserFromToken(token); // Use AuthService to get user
        Product product = productService.getProductById(cartDto.getProductId());
        cartService.updateCartItem(cartDto, user, product);
        return new ResponseEntity<>(new ApiResponse(true, "Product has been updated"), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{cartItemId}")
    public ResponseEntity<ApiResponse> deleteCartItem(@PathVariable("cartItemId") int itemID, @RequestParam("token") String token) throws AuthenticationFailException, CartItemNotExistException {
        User user = authService.getUserFromToken(token); // Use AuthService to get user
        cartService.deleteCartItem(itemID, user.getId());
        return new ResponseEntity<>(new ApiResponse(true, "Item has been removed"), HttpStatus.OK);
    }
}

