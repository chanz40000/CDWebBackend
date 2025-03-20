package com.example.cdwebbackend.repository;

import com.example.cdwebbackend.entity.OrderDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetailEntity, Long> {
    OrderDetailEntity findOneById(long id);
}