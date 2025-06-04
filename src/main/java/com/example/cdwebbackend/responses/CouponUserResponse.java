package com.example.cdwebbackend.responses;

import com.example.cdwebbackend.entity.CouponUserEntity;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CouponUserResponse {
    private Long id;                // id của CouponUserEntity (thường là BaseEntity id)
    private Long couponId;
    private String code;
    private String couponType;      // từ CouponEntity.couponType.couponType
    private int discountValue;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Integer minOrderValue;
    private int quantity;
    private int maxUsesPerUser;
    private Integer minProductQuantity;
    private boolean isActive;

    private boolean isUsed;         // từ CouponUserEntity

    public static CouponUserResponse fromEntity(CouponUserEntity entity) {
        return CouponUserResponse.builder()
                .id(entity.getId())
                .couponId(entity.getCoupon().getId())
                .code(entity.getCoupon().getCode())
                .couponType(entity.getCoupon().getCouponType().getCouponType())
                .discountValue(entity.getCoupon().getDiscountValue())
                .startDate(entity.getCoupon().getStartDate())
                .endDate(entity.getCoupon().getEndDate())
                .minOrderValue(entity.getCoupon().getMinOrderValue())
                .quantity(entity.getCoupon().getQuantity())
                .maxUsesPerUser(entity.getCoupon().getMaxUsesPerUser())
                .minProductQuantity(entity.getCoupon().getMinProductQuantity())
                .isActive(entity.getCoupon().isActive())
                .isUsed(entity.isUsed())
                .build();
    }
}