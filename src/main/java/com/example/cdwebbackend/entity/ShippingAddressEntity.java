package com.example.cdwebbackend.entity;

import jakarta.persistence.*;
import lombok.Getter;

//@Getter
@Entity
@Table(name = "shipping_address")
public class ShippingAddressEntity extends BaseEntity{
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(name = "receiver_name")
    private String receiverName;

    @Column(name = "receiver_phone")
    private String receiverPhone;

    @Column(name = "province") // Tỉnh, thành phố
    private String province;

    @Column(name = "district") //Huyện, quận
    private String district;

    @Column(name = "ward") // Phường, xã
    private String ward;

    @Column(name = "address_detail")
    private String addressDetail;

    @Column(name = "is_default")
    private boolean isDefault = false; // Dùng để đánh dấu địa chỉ mặc định

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
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

}
