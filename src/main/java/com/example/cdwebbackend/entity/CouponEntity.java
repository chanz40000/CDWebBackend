package com.example.cdwebbackend.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "coupon")
public class CouponEntity extends BaseEntity {

    // @Column: Mã giảm giá
    @Column(name = "code", unique = true)
    private String code;

    // @ManyToOne: Quan hệ nhiều - một với CouponTypeEntity
    @ManyToOne
    @JoinColumn(name = "coupon_type_id")
    private CouponTypeEntity couponType;

    // @Column: Giá trị giảm giá (có thể là tiền hoặc phần trăm)
    @Column(name = "discount_value")
    private int discountValue;

    @Column(name = "max_discount_amount", nullable = true)
    private Integer  maxDiscountAmount;

    // @Column: Ngày bắt đầu hiệu lực
    @Column(name = "start_date")
    private LocalDateTime startDate;

    // @Column: Ngày kết thúc hiệu lực
    @Column(name = "end_date")
    private LocalDateTime endDate;

    // @Column: Giá trị tối thiểu của đơn hàng để áp dụng mã (nullable)
    @Column(name = "min_order_value", nullable = true)
    private Integer minOrderValue;

    // @Column: Số lượng mã giảm giá
    @Column(name = "quantity")
    private int quantity;

    // @Column: Số lần tối đa mà mỗi người dùng có thể sử dụng mã
    @Column(name = "max_uses_per_user")
    private int maxUsesPerUser;

    // @Column: Số lượng sản phẩm tối thiểu để áp dụng mã (nullable)
    @Column(name = "min_product_quantity", nullable = true)
    private Integer minProductQuantity;

    // Bật tắt mã
    @Column(name = "is_active")
    private boolean isActive = true;

    public Integer getMaxDiscountAmount() {
        return maxDiscountAmount;
    }

    public void setMaxDiscountAmount(Integer maxDiscountAmount) {
        this.maxDiscountAmount = maxDiscountAmount;
    }
    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
// Getters và Setters

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public CouponTypeEntity getCouponType() {
        return couponType;
    }

    public void setCouponType(CouponTypeEntity couponType) {
        this.couponType = couponType;
    }

    public int getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(int discountValue) {
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

    public Integer getMinOrderValue() {
        return minOrderValue;
    }

    public void setMinOrderValue(Integer minOrderValue) {
        this.minOrderValue = minOrderValue;
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

    public Integer getMinProductQuantity() {
        return minProductQuantity;
    }

    public void setMinProductQuantity(Integer minProductQuantity) {
        this.minProductQuantity = minProductQuantity;
    }
}