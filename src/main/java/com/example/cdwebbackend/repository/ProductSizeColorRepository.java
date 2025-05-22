package com.example.cdwebbackend.repository;

import com.example.cdwebbackend.entity.ProductSizeColorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface ProductSizeColorRepository extends JpaRepository<ProductSizeColorEntity, Long> {
    public ProductSizeColorEntity findOneById(long id);
    List<ProductSizeColorEntity> findByProduct_Id(Long productId);
    public ProductSizeColorEntity findByProductIdAndSizeIdAndProductColorId(long productId,long sizeId ,long colorId);
    @Query("SELECT new map(s.size as sizeName, c.name as colorName) " +
            "FROM ProductSizeColorEntity psc " +
            "JOIN psc.size s " +
            "JOIN psc.productColor pc " +
            "JOIN pc.color c " +
            "WHERE psc.id = :id")
    Optional<Map<String, String>> findSizeAndColorNamesById(@Param("id") Long id);
}
