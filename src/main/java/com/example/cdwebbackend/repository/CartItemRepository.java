package com.example.cdwebbackend.repository;

import com.example.cdwebbackend.entity.CartItemEntity;
import com.example.cdwebbackend.entity.ProductSizeColorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItemEntity, Long> {
    public CartItemEntity findOneById(long id);
//    public Optional<CartItemEntity> findByCartIdAndProductSizeColor(Long cartId, ProductSizeColorEntity productSizeColor);
@Query("SELECT c FROM CartItemEntity c WHERE c.cart.id = :cartId AND c.productSizeColor.id = :pscId")
Optional<CartItemEntity> findByCartIdAndProductSizeColorId(@Param("cartId") Long cartId, @Param("pscId") Long productSizeColorId);

}
