package com.example.cdwebbackend.repository;

import com.example.cdwebbackend.entity.ImportOrderEntity;
import com.example.cdwebbackend.entity.ImportOrderProductEntity;
import com.example.cdwebbackend.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;


    @Repository
    public interface ImportOrderProductRepository extends JpaRepository<ImportOrderProductEntity, Long> {
        //List<ImportOrderProductEntity> findByUser(UserEntity user); // Tìm các đơn nhập theo người dùng
        public Optional<ImportOrderProductEntity> findByImportOrderId(long id);
        public Optional<ImportOrderProductEntity> findOneByCreatedDate(Date createdDate);
    }

