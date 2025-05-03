package com.example.cdwebbackend.dto;

import com.example.cdwebbackend.entity.UserEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;

public class ShippingAddressDTO  extends AbstractDTO<ShippingAddressDTO>{
    @JsonProperty("user_id")
    private Long user;

    @JsonProperty("receiver_name")
    private String receiverName;

    @JsonProperty("receiver_phone")
    private String receiverPhone;

    @JsonProperty("province")// Tỉnh, thành phố
    private String province;

    @JsonProperty("district")//Huyện, quận
    private String district;

    @JsonProperty("ward") // Phường, xã
    private String ward;

    @JsonProperty("address_detail")
    private String addressDetail;

    @JsonProperty("is_default")
    private boolean isDefault;

    public Long getUser() {
        return user;
    }

    public void setUser(Long user) {
        this.user = user;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    public String getAddressDetail() {
        return addressDetail;
    }

    public void setAddressDetail(String addressDetail) {
        this.addressDetail = addressDetail;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }
}
