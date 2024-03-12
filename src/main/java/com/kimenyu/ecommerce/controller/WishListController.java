package com.kimenyu.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.kimenyu.ecommerce.common.ApiResponse;
import com.kimenyu.ecommerce.dto.product.ProductDto;
import com.kimenyu.ecommerce.entity.Product;
import com.kimenyu.ecommerce.entity.User;
import com.kimenyu.ecommerce.entity.WishList;
import com.kimenyu.ecommerce.service.AuthService; // Updated import
import com.kimenyu.ecommerce.service.ProductService;
import com.kimenyu.ecommerce.service.WishListService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/wishlist")
public class WishListController {

    @Autowired
    private WishListService wishListService;

    @Autowired
    private AuthService authService; // Inject AuthService

    @GetMapping("/{token}")
    public ResponseEntity<List<ProductDto>> getWishList(@PathVariable("token") String token) {
        // Get user from token
        User user = authService.getUserFromToken(token);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        int userId = user.getId();
        List<WishList> body = wishListService.readWishList(userId);
        List<ProductDto> products = new ArrayList<>();
        for (WishList wishList : body) {
            products.add(ProductService.getDtoFromProduct(wishList.getProduct()));
        }

        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addWishList(@RequestBody Product product, @RequestParam("token") String token) {
        // Validate token
        User user = authService.getUserFromToken(token);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        // Create wishlist
        WishList wishList = new WishList();
        wishList.setUser(user); // Set the user
        wishList.setProduct(product); // Set the product
        wishListService.createWishlist(wishList);
        return new ResponseEntity<>(new ApiResponse(true, "Added to wishlist"), HttpStatus.CREATED);
    }
}
