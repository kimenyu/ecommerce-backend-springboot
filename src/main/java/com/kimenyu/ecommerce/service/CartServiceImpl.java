package com.kimenyu.ecommerce.service;

import org.springframework.security.access.method.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kimenyu.ecommerce.dto.CartDto;
import com.kimenyu.ecommerce.dto.CartItemDto;
import com.kimenyu.ecommerce.entity.Cart;
import com.kimenyu.ecommerce.entity.CartItem;
import com.kimenyu.ecommerce.entity.Product;
import com.kimenyu.ecommerce.repository.CartRepository;
import com.kimenyu.ecommerce.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CartServiceImpl implements CartService{

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    public CartServiceImpl(CartRepository cartRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }


    @Override
    public void addProductToCart(Long cartId, Product productDto) {
        Cart cart = cartRepository.findById(cartId)
                                  .orElseThrow(() -> new IllegalArgumentException("Cart not found"));
        Product product = productRepository.findById(productDto.getProductId())
                                            .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setQuantity(1); // Default quantity
        cart.getCartItems().add(cartItem);
        cartRepository.save(cart);
    }

    @Override
    public List<CartItemDto> getCartById(Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                                  .orElseThrow(() -> new IllegalArgumentException("Cart not found"));
        return convertToDto(cart);
    }

    @Override
    public List<CartItemDto> getCartItems(Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                                  .orElseThrow(() -> new IllegalArgumentException("Cart not found"));
        return cart.getCartItems().stream()
                                   .map(this::convertToDto)
                                   .collect(Collectors.toList());
    }

    @Override
    public void addCartItem(Long cartId, Long productId) {
        Cart cart = cartRepository.findById(cartId)
                                  .orElseThrow(() -> new IllegalArgumentException("Cart not found"));

        Product product = productRepository.findById(productId)
                                            .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setQuantity(1); // Default quantity

        cart.getCartItems().add(cartItem);
        cartRepository.save(cart);
    }

    @Override
    public void updateCartItem(Long cartId, Long cartItemId, CartItemDto cartItemDto) {
        // Add logic to update a cart item
        Cart cart = cartRepository.findById(cartId)
                                  .orElseThrow(() -> new IllegalArgumentException("Cart not found"));

        CartItem cartItem = cart.getCartItems().stream()
                .filter(item -> item.getId().equals(cartItemId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Cart item not found"));

        cartItem.setQuantity(cartItemDto.getQuantity());

        cartRepository.save(cart);
    }

    @Override
    public void removeCartItem(Long cartId, Long cartItemId) {
        // Add logic to remove a cart item
        Cart cart = cartRepository.findById(cartId)
                                  .orElseThrow(() -> new IllegalArgumentException("Cart not found"));

        cart.getCartItems().removeIf(cartItem -> cartItem.getId().equals(cartItemId));

        cartRepository.save(cart);
    }

    private List<CartItemDto> convertToDto(Cart cart) {
        List<CartItemDto> cartItemDtos = new ArrayList<>();
        // Populate cartItemDtos with cart item details
        return cartItemDtos;
    }

    private CartItemDto convertToDto(CartItem cartItem) {
        CartItemDto cartItemDto = new CartItemDto();
        // Populate cartItemDto with cart item details
        return cartItemDto;
    }

    
    
}
