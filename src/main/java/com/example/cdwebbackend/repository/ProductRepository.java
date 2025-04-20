package com.example.cdwebbackend.repository;

import com.example.cdwebbackend.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
//    public List<ProductEntity> findAll();
    public ProductEntity findOneById(long id);
}
