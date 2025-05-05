package com.example.cdwebbackend.dto;

public class OnlinePaymentDTO extends AbstractDTO<OnlinePaymentDTO>{

    private Long orderId;
    private Long paymentMethodId;
    private int totalAmount;
    private String bankCode;
    private String language;

    // Constructors
    public OnlinePaymentDTO() {}

    public OnlinePaymentDTO(Long orderId, Long paymentMethodId, int totalAmount, String bankCode, String language) {
        this.orderId = orderId;
        this.paymentMethodId = paymentMethodId;
        this.totalAmount = totalAmount;
        this.bankCode = bankCode;
        this.language = language;
    }

    // Getters and Setters

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getPaymentMethodId() {
        return paymentMethodId;
    }

    public void setPaymentMethodId(Long paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
