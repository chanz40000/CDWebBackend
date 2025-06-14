package com.example.cdwebbackend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.LocalDateTime;

public class CouponDTO extends AbstractDTO<CouponDTO> {
    @JsonProperty("id")
    private Long id;

    @NotBlank(message = "Code không được để trống")
    @JsonProperty("code")
    private String code;

    @NotNull(message = "Coupon type id không được để trống")
    @JsonProperty("coupon_type_id")
    private Long couponType;

    @Positive(message = "Discount value phải lớn hơn 0")
    @JsonProperty("discount_value")
    private double discountValue;

    @JsonProperty("max_discount_amount")
    private Integer  maxDiscountAmount;

    @NotNull(message = "Start date không được để trống")
    @JsonProperty("start_date")
    private LocalDateTime startDate;

    @NotNull(message = "End date không được để trống")
    @JsonProperty("end_date")
    private LocalDateTime endDate;

    @PositiveOrZero(message = "Min order amount phải >= 0")
    @JsonProperty("min_order_value")
    private Double minOrderAmount;

    @PositiveOrZero(message = "Quantity phải >= 0")
    @JsonProperty("quantity")
    private int quantity;

    @PositiveOrZero(message = "Max uses per user phải >= 0")
    @JsonProperty("max_uses_per_user")
    private int maxUsesPerUser;

    @PositiveOrZero(message = "Min product quantity phải >= 0")
    @JsonProperty("min_product_quantity")
    private int minProductQuantity;

    @JsonProperty("is_active")
    private boolean isActive;

    public Integer getMaxDiscountAmount() {
        return maxDiscountAmount;
    }

    public void setMaxDiscountAmount(Integer maxDiscountAmount) {
        this.maxDiscountAmount = maxDiscountAmount;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getCouponType() {
        return couponType;
    }

    public void setCouponType(Long couponType) {
        this.couponType = couponType;
    }

    public double getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(double discountValue) {
        this.discountValue = discountValue;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public Double getMinOrderAmount() {
        return minOrderAmount;
    }

    public void setMinOrderAmount(Double minOrderAmount) {
        this.minOrderAmount = minOrderAmount;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getMaxUsesPerUser() {
        return maxUsesPerUser;
    }

    public void setMaxUsesPerUser(int maxUsesPerUser) {
        this.maxUsesPerUser = maxUsesPerUser;
    }

    public int getMinProductQuantity() {
        return minProductQuantity;
    }

    public void setMinProductQuantity(int minProductQuantity) {
        this.minProductQuantity = minProductQuantity;
    }
}