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

    @JsonProperty("shipping_address_id")
    private Long shippingAddress;

    @JsonProperty("orderDetailDTOs")
    private List<OrderDetailDTO> orderDetails;


    // Getters and Setters


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

    public Long getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(Long shippingAddress) {
        this.shippingAddress = shippingAddress;
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
