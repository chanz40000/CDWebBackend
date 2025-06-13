package com.example.cdwebbackend.dto;

import com.example.cdwebbackend.entity.UserEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class ShippingAddressDTO extends AbstractDTO<ShippingAddressDTO> {

    @NotNull(message = "{validation.user_id_notNull}")
    @JsonProperty("user_id")
    private Long user;

    @NotBlank(message = "{validation.receiver_name_notBlank}")
    @Size(max = 100, message = "{validation.receiver_name_size}")
    @JsonProperty("receiver_name")
    private String receiverName;

    @NotBlank(message = "{validation.receiver_phone_notBlank}")
    @Pattern(regexp = "^\\d{10}$", message = "{validation.receiver_phone_pattern}")
    @JsonProperty("receiver_phone")
    private String receiverPhone;

    @NotBlank(message = "{validation.province_notBlank}")
    @Size(max = 100, message = "{validation.province_size}")
    @JsonProperty("province")
    private String province;

    @NotBlank(message = "{validation.district_notBlank}")
    @Size(max = 100, message = "{validation.district_size}")
    @JsonProperty("district")
    private String district;

    @NotBlank(message = "{validation.ward_notBlank}")
    @Size(max = 100, message = "{validation.ward_size}")
    @JsonProperty("ward")
    private String ward;

    @NotBlank(message = "{validation.address_detail_notBlank}")
    @Size(max = 255, message = "{validation.address_detail_size}")
    @JsonProperty("address_detail")
    private String addressDetail;

    @JsonProperty("is_default")
    private boolean isDefault;

    // Getters và Setters
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

    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }
}