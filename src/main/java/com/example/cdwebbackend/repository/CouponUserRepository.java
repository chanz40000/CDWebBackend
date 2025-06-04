package com.example.cdwebbackend.repository;

import com.example.cdwebbackend.entity.CouponEntity;
import com.example.cdwebbackend.entity.CouponUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CouponUserRepository extends JpaRepository<CouponUserEntity, Long> {
    CouponUserEntity findOneById(long id);
    List<CouponUserEntity> findByUserId(long userId);
}
