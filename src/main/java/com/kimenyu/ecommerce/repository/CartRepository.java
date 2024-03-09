package com.kimenyu.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kimenyu.ecommerce.entity.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long>{

}
