package com.example.cdwebbackend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "coupon_user")
public class CouponUserEntity extends BaseEntity {

    // @ManyToOne: Quan hệ nhiều - một với UserEntity
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    // @ManyToOne: Quan hệ nhiều - một với CouponEntity
    @ManyToOne
    @JoinColumn(name = "coupon_id")
    private CouponEntity coupon;

    // Số lần đã sử dụng mã giảm giá này của user này
    @Column(name = "usage_count")
    private int usageCount;

    // @Column: Đánh dấu mã giảm giá đã sử dụng hay chưa
    @Column(name = "is_used")
    private boolean isUsed;

    // Getters và Setters


    public int getUsageCount() {
        return usageCount;
    }

    public void setUsageCount(int usageCount) {
        this.usageCount = usageCount;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public CouponEntity getCoupon() {
        return coupon;
    }

    public void setCoupon(CouponEntity coupon) {
        this.coupon = coupon;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public void setUsed(boolean used) {
        isUsed = used;
    }
}