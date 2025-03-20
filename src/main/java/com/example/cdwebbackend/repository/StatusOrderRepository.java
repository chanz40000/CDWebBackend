package com.example.cdwebbackend.repository;

import com.example.cdwebbackend.entity.StatusOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusOrderRepository extends JpaRepository<StatusOrderEntity, Long> {
    StatusOrderEntity findOneById(long id);
}
