package com.kimenyu.ecommerce.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kimenyu.ecommerce.entity.Cart;
import com.kimenyu.ecommerce.entity.Product;
import com.kimenyu.ecommerce.entity.User;
import com.kimenyu.ecommerce.exceptions.CartItemNotExistException;
import com.kimenyu.ecommerce.repository.CartRepository;
import com.kimenyu.ecommerce.dto.cart.AddToCartDto;
import com.kimenyu.ecommerce.dto.cart.CartDto;
import com.kimenyu.ecommerce.dto.cart.CartItemDto;

import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class CartService {

    @Autowired
    private  CartRepository cartRepository;

    public CartService(){}

    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public void addToCart(AddToCartDto addToCartDto, Product product, User user){
        Cart cart = new Cart(product, addToCartDto.getQuantity(), user);
        cartRepository.save(cart);
    }


    public CartDto listCartItems(User user) {
        List<Cart> cartList = cartRepository.findAllByUserOrderByCreatedDateDesc(user);
        List<CartItemDto> cartItems = new ArrayList<>();
        for (Cart cart:cartList){
            CartItemDto cartItemDto = getDtoFromCart(cart);
            cartItems.add(cartItemDto);
        }
        double totalCost = 0;
        for (CartItemDto cartItemDto :cartItems){
            totalCost += (cartItemDto.getProduct().getPrice()* cartItemDto.getQuantity());
        }
        return new CartDto(cartItems,totalCost);
    }


    public  CartItemDto getDtoFromCart(Cart cart) {
        CartItemDto cartItemDto = new CartItemDto();
        BeanUtils.copyProperties(cart, cartItemDto);
        return cartItemDto;
    }


    public void updateCartItem(AddToCartDto cartDto, User user,Product product){
        Cart cart = cartRepository.getOne(cartDto.getId());
        cart.setQuantity(cartDto.getQuantity());
        cart.setCreatedDate(new Date());
        cartRepository.save(cart);
    }

    public void deleteCartItem(int id,int userId) throws CartItemNotExistException {
        if (!cartRepository.existsById(id))
            throw new CartItemNotExistException("Cart id is invalid : " + id);
        cartRepository.deleteById(id);

    }

    public void deleteCartItems(int userId) {
        cartRepository.deleteAll();
    }


    public void deleteUserCartItems(User user) {
        cartRepository.deleteByUser(user);
    }
}


