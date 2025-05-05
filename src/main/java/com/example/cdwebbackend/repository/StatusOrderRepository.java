package com.example.cdwebbackend.repository;

import com.example.cdwebbackend.entity.StatusOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StatusOrderRepository extends JpaRepository<StatusOrderEntity, Long> {
    StatusOrderEntity findOneById(long id);
    Optional<StatusOrderEntity> findByName(String name);
}
