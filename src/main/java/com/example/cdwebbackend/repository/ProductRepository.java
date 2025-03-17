package com.example.cdwebbackend.repository;

import com.example.cdwebbackend.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    public ProductEntity findOneById(long id);
}
