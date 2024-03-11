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
import com.kimenyu.ecommerce.service.AuthenticationService;
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
        private AuthenticationService authenticationService;

        @GetMapping("/{token}")
        public ResponseEntity<List<ProductDto>> getWishList(@PathVariable("token") String token) {
                int user_id = authenticationService.getUser(token).getId();
                List<WishList> body = wishListService.readWishList(user_id);
                List<ProductDto> products = new ArrayList<ProductDto>();
                for (WishList wishList : body) {
                        products.add(ProductService.getDtoFromProduct(wishList.getProduct()));
                }

                return new ResponseEntity<List<ProductDto>>(products, HttpStatus.OK);
        }

        @PostMapping("/add")
        public ResponseEntity<ApiResponse> addWishList(@RequestBody Product product, @RequestParam("token") String token) {
                authenticationService.authenticate(token);
                User user = authenticationService.getUser(token);
                WishList wishList = new WishList();
                wishListService.createWishlist(wishList);
                return new ResponseEntity<ApiResponse>(new ApiResponse(true, "Add to wishlist"), HttpStatus.CREATED);

        }


}
