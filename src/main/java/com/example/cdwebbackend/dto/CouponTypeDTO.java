package com.example.cdwebbackend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CouponTypeDTO extends AbstractDTO<CouponTypeDTO> {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("coupon_type")
    private String couponType;

    public String getCouponType() { return couponType; }
    public void setCouponType(String couponType) { this.couponType = couponType; }
}
