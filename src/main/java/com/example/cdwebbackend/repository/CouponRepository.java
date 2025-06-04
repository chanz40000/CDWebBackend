package com.example.cdwebbackend.repository;

import com.example.cdwebbackend.entity.CouponEntity;
import com.example.cdwebbackend.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CouponRepository extends JpaRepository<CouponEntity, Long> {
    CouponEntity findOneById(long id);
    public Optional<CouponEntity> findByCode(String code);
}
