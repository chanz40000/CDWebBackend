package com.example.cdwebbackend.repository;

import com.example.cdwebbackend.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    OrderEntity findOneById(long id);
    List<OrderEntity> findAllByCreatedDate(Date createDate);
     List<OrderEntity> findAllByCreatedDateBetween(Date start,Date end);
    List<OrderEntity> findByUserId(long userId);
    OrderEntity findByUserIdAndId(long userId, long id);
    List<OrderEntity> findByUserIdAndStatusOrderId(long userId, long status);
}