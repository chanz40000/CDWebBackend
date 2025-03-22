package com.example.cdwebbackend.repository;

import com.example.cdwebbackend.entity.CouponUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponUserRepository extends JpaRepository<CouponUserEntity, Long> {
    CouponUserEntity findOneById(long id);
}
