package com.example.cdwebbackend.dto;

public class CouponUserDTO extends AbstractDTO<CouponUserDTO> {
    private Long userId;
    private Long couponId;
    private boolean used;

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getCouponId() { return couponId; }
    public void setCouponId(Long couponId) { this.couponId = couponId; }

    public boolean isUsed() { return used; }
    public void setUsed(boolean used) { this.used = used; }
}
