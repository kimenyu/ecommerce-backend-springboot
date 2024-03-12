package com.kimenyu.ecommerce.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.kimenyu.ecommerce.entity.OrderItem;
import org.springframework.stereotype.Repository;
@Repository
public interface OrderItemsRepository extends JpaRepository<OrderItem,Integer> {
}
