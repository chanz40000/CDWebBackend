package com.example.cdwebbackend.repository;

import com.example.cdwebbackend.entity.CouponEntity;
import com.example.cdwebbackend.entity.CouponUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CouponUserRepository extends JpaRepository<CouponUserEntity, Long> {
    CouponUserEntity findOneById(long id);
    List<CouponUserEntity> findByUserId(long userId);
    @Query("SELECT cu FROM CouponUserEntity cu " +
            "JOIN cu.coupon c " +
            "WHERE cu.user.id = :userId " +
            "AND c.couponType.id IN :couponTypeIds " +
            "AND c.isActive = true")
    List<CouponUserEntity> findActiveCouponsByUserIdAndCouponTypeIds(
            @Param("userId") Long userId,
            @Param("couponTypeIds") List<Long> couponTypeIds);
    Optional<CouponUserEntity> findByUserIdAndCouponId(Long userId, Long couponId);
    boolean existsByUserIdAndCouponId(Long userId, Long couponId);

}
