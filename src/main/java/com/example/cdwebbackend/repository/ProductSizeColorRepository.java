package com.example.cdwebbackend.repository;

import com.example.cdwebbackend.entity.ProductSizeColorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductSizeColorRepository extends JpaRepository<ProductSizeColorEntity, Long> {
    public ProductSizeColorEntity findOneById(long id);
    public ProductSizeColorEntity findByProductIdAndSizeIdAndProductColorId(Long productId, Long sizeId, Long productColorId);
    public List<ProductSizeColorEntity> findByProductId(long productId);
}
