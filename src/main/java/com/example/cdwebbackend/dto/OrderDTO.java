package com.example.cdwebbackend.dto;

import java.time.LocalDateTime;

public class OrderDTO extends AbstractDTO<OrderDTO> {
    private Long userId;
    private double totalPrice;
    private long statusOrder;
    private Long payment;
    private LocalDateTime createDate;

    // Getters and Setters

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public long getStatusOrder() {
        return statusOrder;
    }

    public void setStatusOrder(long statusOrder) {
        this.statusOrder = statusOrder;
    }

    public Long getPayment() {
        return payment;
    }

    public void setPayment(Long payment) {
        this.payment = payment;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }
}
