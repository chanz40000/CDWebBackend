package com.example.cdwebbackend.dto;

public class CouponTypeDTO extends AbstractDTO<CouponTypeDTO> {
    private String couponType;

    public String getCouponType() { return couponType; }
    public void setCouponType(String couponType) { this.couponType = couponType; }
}
