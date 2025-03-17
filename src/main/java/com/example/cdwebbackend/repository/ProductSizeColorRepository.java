package com.example.cdwebbackend.repository;

import com.example.cdwebbackend.entity.ProductSizeColorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductSizeColorRepository extends JpaRepository<ProductSizeColorEntity, Long> {
    public ProductSizeColorEntity findOneById(long id);

}
