package com.example.cdwebbackend.repository;

import com.example.cdwebbackend.entity.CouponEntity;
import com.example.cdwebbackend.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CouponRepository extends JpaRepository<CouponEntity, Long> {
    CouponEntity findOneById(long id);
    public Optional<CouponEntity> findByCode(String code);
    // Lọc theo 1 coupon_type_id và is_active = true
    List<CouponEntity> findByCouponTypeIdAndIsActiveTrue(Long couponTypeId);
//    List<CouponEntity> findByIsActiveTrue();

    // Lọc theo danh sách coupon_type_id và is_active = true
    List<CouponEntity> findByCouponTypeIdInAndIsActiveTrue(List<Long> couponTypeIds);
    @Query("SELECT c FROM CouponEntity c")
    Page<CouponEntity> findAllCoupons(Pageable pageable);

}
