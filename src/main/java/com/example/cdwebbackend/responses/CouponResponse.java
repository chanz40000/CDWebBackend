package com.example.cdwebbackend.responses;

import com.example.cdwebbackend.entity.CouponEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CouponResponse {
    private Long id;
    private String code;
    private String couponType; // Lấy từ couponEntity.getCouponType().getCouponType()
    private int discountValue;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private LocalDateTime startDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private LocalDateTime endDate;
    private Integer minOrderValue;
    private Integer maxDiscountAmount;
    private int quantity;
    private int maxUsesPerUser;
    private Integer minProductQuantity;
    private boolean isActive;
    private boolean isUsed; // Lấy từ CouponUserEntity (nếu có)

    public static CouponResponse fromEntity(CouponEntity couponEntity) {
        return CouponResponse.builder()
                .id(couponEntity.getId())
                .code(couponEntity.getCode())
                .couponType(couponEntity.getCouponType().getCouponType())
                .discountValue(couponEntity.getDiscountValue())
                .maxDiscountAmount(couponEntity.getMaxDiscountAmount())
                .startDate(couponEntity.getStartDate())
                .endDate(couponEntity.getEndDate())
                .minOrderValue(couponEntity.getMinOrderValue())
                .quantity(couponEntity.getQuantity())
                .maxUsesPerUser(couponEntity.getMaxUsesPerUser())
                .minProductQuantity(couponEntity.getMinProductQuantity())
                .isActive(couponEntity.isActive())
//                .isUsed(couponEntity.u)
                .build();
    }
}
