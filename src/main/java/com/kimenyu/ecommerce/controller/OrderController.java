package com.kimenyu.ecommerce.controller;

import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.kimenyu.ecommerce.common.ApiResponse;
import com.kimenyu.ecommerce.dto.checkout.CheckoutItemDto;
import com.kimenyu.ecommerce.dto.checkout.StripeResponse;
import com.kimenyu.ecommerce.entity.Order;
import com.kimenyu.ecommerce.entity.User;
import com.kimenyu.ecommerce.exceptions.AuthenticationFailException;
import com.kimenyu.ecommerce.exceptions.OrderNotFoundException;
import com.kimenyu.ecommerce.service.AuthService; // Updated import
import com.kimenyu.ecommerce.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private AuthService authService;

    // stripe create session API
    @PostMapping("/create-checkout-session")
    public ResponseEntity<StripeResponse> checkoutList(@RequestBody List<CheckoutItemDto> checkoutItemDtoList) {
        try {
            // create the stripe session
            Session session = orderService.createSession(checkoutItemDtoList);
            StripeResponse stripeResponse = new StripeResponse(session.getId());
            // send the stripe session id in response
            return new ResponseEntity<>(stripeResponse, HttpStatus.OK);
        } catch (StripeException e) {
            // Handle Stripe exception
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // place order after checkout
    @PostMapping("/add")
    public ResponseEntity<ApiResponse> placeOrder(@RequestHeader("Authorization") String token,
                                                   @RequestParam("sessionId") String sessionId) {
        try {
            // Validate token
            User user = authService.getUserFromToken(token.substring(7));
            if (user == null) {
                return new ResponseEntity<>(new ApiResponse(false, "User not authenticated"), HttpStatus.UNAUTHORIZED);
            }

            // Place the order
            orderService.placeOrder(user, sessionId);
            return new ResponseEntity<>(new ApiResponse(true, "Order has been placed"), HttpStatus.CREATED);
        } catch (AuthenticationFailException e) {
            return new ResponseEntity<>(new ApiResponse(false, "Authentication failed"), HttpStatus.UNAUTHORIZED);
        }
    }

    // get all orders
    @GetMapping("/")
    public ResponseEntity<List<Order>> getAllOrders(@RequestHeader("Authorization") String token) {
        try {
            // Validate token
            User user = authService.getUserFromToken(token.substring(7));
            if (user == null) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            // Get orders
            List<Order> orderDtoList = orderService.listOrders(user);
            return new ResponseEntity<>(orderDtoList, HttpStatus.OK);
        } catch (AuthenticationFailException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    // get orderitems for an order
    @GetMapping("/{id}")
    public ResponseEntity<Object> getOrderById(@PathVariable("id") Integer id,
                                                @RequestHeader("Authorization") String token) {
        try {
            // Validate token
            User user = authService.getUserFromToken(token.substring(7));
            if (user == null) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            // Get order by ID
            Order order = orderService.getOrder(id);
            return new ResponseEntity<>(order, HttpStatus.OK);
        } catch (AuthenticationFailException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (OrderNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
