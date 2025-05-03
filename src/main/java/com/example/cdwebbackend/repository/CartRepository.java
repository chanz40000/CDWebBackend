package com.example.cdwebbackend.repository;

import com.example.cdwebbackend.entity.CartEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<CartEntity, Long> {
    public Optional<CartEntity> findOneById(long id);
    public Optional<CartEntity> findByUserId(long userId);
}
