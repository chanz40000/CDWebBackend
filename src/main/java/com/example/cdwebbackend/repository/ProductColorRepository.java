package com.example.cdwebbackend.repository;

import com.example.cdwebbackend.entity.ProductColorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductColorRepository  extends JpaRepository<ProductColorEntity, Long> {
    public ProductColorEntity findOneById(long id);
    public ProductColorEntity findByColorIdAndProductId(Long color, Long productId);
    public ProductColorEntity findByIdAndProductId(Long id, Long productId);
    public List<ProductColorEntity> findByProductId(Long productId);
    ProductColorEntity findByProductIdAndColorId(Long productId, Long colorId);

}
