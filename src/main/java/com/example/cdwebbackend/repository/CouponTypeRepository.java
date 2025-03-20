package com.example.cdwebbackend.repository;

import com.example.cdwebbackend.entity.CouponTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponTypeRepository extends JpaRepository<CouponTypeEntity, Long> {
    CouponTypeEntity findOneById(long id);
}
