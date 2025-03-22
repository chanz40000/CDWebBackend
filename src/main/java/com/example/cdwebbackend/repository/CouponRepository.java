package com.example.cdwebbackend.repository;

import com.example.cdwebbackend.entity.CouponEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponRepository extends JpaRepository<CouponEntity, Long> {
    CouponEntity findOneById(long id);
}
