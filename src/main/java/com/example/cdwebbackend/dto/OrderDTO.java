package com.example.cdwebbackend.dto;

import com.example.cdwebbackend.entity.OrderDetailEntity;
import com.example.cdwebbackend.entity.ShippingAddressEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;

import java.util.List;

public class OrderDTO extends AbstractDTO<OrderDTO> {
    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("total_price")
    private int totalPrice;

    @JsonProperty("status_order_id")
    private Long statusOrder;

    @JsonProperty("payment_id")
    private Long payment;

    @JsonProperty("shipping_fee")
    private int shippingFee;

    @JsonProperty("final_price")
    private int finalPrice;

    @JsonProperty("note")
    private String note;

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

//    @JsonProperty("shipping_address_id")
//    private Long shippingAddress;

    @JsonProperty("orderDetailDTOs")
    private List<OrderDetailDTO> orderDetails;


    // Getters and Setters


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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public List<OrderDetailDTO> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetailDTO> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public int getShippingFee() {
        return shippingFee;
    }

    public void setShippingFee(int shippingFee) {
        this.shippingFee = shippingFee;
    }

    public int getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(int finalPrice) {
        this.finalPrice = finalPrice;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Long getStatusOrder() {
        return statusOrder;
    }

    public void setStatusOrder(Long statusOrder) {
        this.statusOrder = statusOrder;
    }

    public Long getPayment() {
        return payment;
    }

    public void setPayment(Long payment) {
        this.payment = payment;
    }

}
