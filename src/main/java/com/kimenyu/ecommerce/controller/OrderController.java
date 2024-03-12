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
    private AuthService authService; // Inject AuthService

    // stripe create session API
    @PostMapping("/create-checkout-session")
    public ResponseEntity<StripeResponse> checkoutList(@RequestBody List<CheckoutItemDto> checkoutItemDtoList) throws StripeException {
        // create the stripe session
        Session session = orderService.createSession(checkoutItemDtoList);
        StripeResponse stripeResponse = new StripeResponse(session.getId());
        // send the stripe session id in response
        return new ResponseEntity<>(stripeResponse, HttpStatus.OK);
    }

    // place order after checkout
    @PostMapping("/add")
    public ResponseEntity<ApiResponse> placeOrder(@RequestParam("token") String token, @RequestParam("sessionId") String sessionId)
            throws AuthenticationFailException {
        // validate token
        User user = authService.getUserFromToken(token); // Use AuthService to get user
        // place the order
        orderService.placeOrder(user, sessionId);
        return new ResponseEntity<>(new ApiResponse(true, "Order has been placed"), HttpStatus.CREATED);
    }

    // get all orders
    @GetMapping("/")
    public ResponseEntity<List<Order>> getAllOrders(@RequestParam("token") String token) throws AuthenticationFailException {
        // validate token
        User user = authService.getUserFromToken(token); // Use AuthService to get user
        // get orders
        List<Order> orderDtoList = orderService.listOrders(user);
        return new ResponseEntity<>(orderDtoList, HttpStatus.OK);
    }

    // get orderitems for an order
    @GetMapping("/{id}")
    public ResponseEntity<Object> getOrderById(@PathVariable("id") Integer id, @RequestParam("token") String token)
            throws AuthenticationFailException {
        // Get user from token
        User user = authService.getUserFromToken(token);
        if (user == null) {
            throw new AuthenticationFailException("Invalid or expired token");
        }

        try {
            Order order = orderService.getOrder(id);
            return new ResponseEntity<>(order, HttpStatus.OK);
        } catch (OrderNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

}
