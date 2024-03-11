package com.kimenyu.ecommerce.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.kimenyu.ecommerce.entity.OrderItem;

public interface OrderItemsRepository extends JpaRepository<OrderItem,Integer> {
}
