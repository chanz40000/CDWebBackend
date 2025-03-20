package com.example.cdwebbackend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.List;

@Entity
@Table(name = "coupon_type")
public class CouponTypeEntity extends BaseEntity {

    // @Column: Lưu loại giảm giá (Ví dụ: Giảm theo tiền hoặc phần trăm)
    @Column(name = "coupon_type")
    private String couponType;

    // @OneToMany: Quan hệ một - nhiều với CouponEntity
    @OneToMany(mappedBy = "couponType")
    private List<CouponEntity> coupons;

    // Getters và Setters

    public String getCouponType() {
        return couponType;
    }

    public void setCouponType(String couponType) {
        this.couponType = couponType;
    }

    public List<CouponEntity> getCoupons() {
        return coupons;
    }

    public void setCoupons(List<CouponEntity> coupons) {
        this.coupons = coupons;
    }
}