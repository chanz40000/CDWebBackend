package com.example.cdwebbackend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "online_payment", indexes = {
        @Index(name = "idx_online_payment_order_id", columnList = "order_id", unique = true),
        @Index(name = "idx_online_payment_payment_method_id", columnList = "payment_method_id"),
        @Index(name = "idx_online_payment_total_amount", columnList = "total_amount"),
        @Index(name = "idx_online_payment_bank_code", columnList = "bank_code")
})
public class OnlinePaymentEntity extends BaseEntity {

    @OneToOne(optional = false)
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    private OrderEntity order;

    @ManyToOne(optional = false)
    @JoinColumn(name = "payment_method_id", nullable = false)
    private PaymentEntity payment;

    @Column(name = "total_amount")
    private int totalAmount;

    @Column(name = "bank_code")
    private String bankCode;

    @Column(name = "language")
    private String language;

    // Getters and Setters

    public OrderEntity getOrder() {
        return order;
    }

    public void setOrder(OrderEntity order) {
        this.order = order;
    }

    public PaymentEntity getPayment() {
        return payment;
    }

    public void setPayment(PaymentEntity payment) {
        this.payment = payment;
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