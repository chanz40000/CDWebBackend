package com.example.cdwebbackend.repository;

import com.example.cdwebbackend.entity.OrderReasonEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderReasonRepository  extends JpaRepository<OrderReasonEntity, Long> {
    OrderReasonEntity findOneById(long id);
    List<OrderReasonEntity> findByReasonGroupIn(List<String> reasonGroups);
    List<OrderReasonEntity> findAllByReasonGroup(String reasonGroup);
}
