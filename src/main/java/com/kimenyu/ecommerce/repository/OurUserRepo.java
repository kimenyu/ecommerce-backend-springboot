package com.kimenyu.ecommerce.repository;


import com.kimenyu.ecommerce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OurUserRepo extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);

}