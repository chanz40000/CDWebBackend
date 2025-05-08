package com.example.cdwebbackend.repository;

import com.example.cdwebbackend.entity.CartEntity;
import com.example.cdwebbackend.entity.ShippingAddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ShippingAddressRePository extends JpaRepository<ShippingAddressEntity, Long> {
//    public Optional<ShippingAddressEntity> findOneById(long id);
    ShippingAddressEntity findOneById(Long id);
    ShippingAddressEntity findByUserId(long userId);
    List<ShippingAddressEntity> findAllByUserId(Long userId);
    Optional<ShippingAddressEntity> findByUserIdAndIsDefaultTrue(Long userId);
    Optional<ShippingAddressEntity> findByIdAndUserId(Long id, Long userId);

}
