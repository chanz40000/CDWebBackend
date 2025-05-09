package com.example.cdwebbackend.repository;

import com.example.cdwebbackend.entity.ProductSizeColorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductSizeColorRepository extends JpaRepository<ProductSizeColorEntity, Long> {
    public ProductSizeColorEntity findOneById(long id);
    List<ProductSizeColorEntity> findByProduct_Id(Long productId);
    public ProductSizeColorEntity findByProductIdAndColorIdAndSizeId(long productId,long colorId,long sizeId);
}
