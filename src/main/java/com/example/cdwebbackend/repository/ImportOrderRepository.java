package com.example.cdwebbackend.repository;

import com.example.cdwebbackend.entity.ImportOrderEntity;
import com.example.cdwebbackend.entity.UserEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ImportOrderRepository extends JpaRepository<ImportOrderEntity, Long> {
    List<ImportOrderEntity> findByUser(UserEntity user); // Tìm các đơn nhập theo người dùng
    public Optional<ImportOrderEntity> findOneById(long id);
    public Optional<ImportOrderEntity> findOneByCreatedDate(Date createdDate);
}

