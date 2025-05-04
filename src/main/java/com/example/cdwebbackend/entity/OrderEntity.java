package com.example.cdwebbackend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
public class OrderEntity extends BaseEntity {

    // @ManyToOne: Quan hệ nhiều - một giữa OrderEntity và UserEntity
    // 1 user có thể có nhiều đơn hàng, nhưng 1 đơn hàng chỉ thuộc về 1 user
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(name = "total_price") // tổng tiền sản phẩm
    private int totalPrice;

    //tiền ship
    @Column(name = "shipping_fee")
    private int shippingFee;

    // Tổng toàn bộ
    @Column(name = "final_price")
    private int finalPrice;

    @Column(name = "note")
    private String note;

    // @ManyToOne: Quan hệ nhiều - một với StatusOrderEntity
    // 1 đơn hàng chỉ có 1 trạng thái, nhưng 1 trạng thái có thể áp dụng cho nhiều đơn hàng
    @ManyToOne
    @JoinColumn(name = "status_order_id")
    private StatusOrderEntity statusOrder;

    // 1 đơn hàng chỉ có 1 phương thức thanh toán, nhưng 1 phương thức có thể được dùng cho nhiều đơn hàng
    @ManyToOne
    @JoinColumn(name = "payment_id")
    private PaymentEntity payment;

    // Địa chỉ giao hàng
    @ManyToOne
    @JoinColumn(name = "shipping_address_id", nullable = false)
    private ShippingAddressEntity shippingAddress;

    // @OneToMany: Quan hệ một - nhiều với OrderDetailEntity
    // 1 đơn hàng có thể có nhiều chi tiết đơn hàng, nhưng mỗi chi tiết đơn hàng chỉ thuộc về 1 đơn hàng
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDetailEntity> orderDetails;

    // Getters and Setters


    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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

    public ShippingAddressEntity getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(ShippingAddressEntity shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public StatusOrderEntity getStatusOrder() {
        return statusOrder;
    }

    public void setStatusOrder(StatusOrderEntity statusOrder) {
        this.statusOrder = statusOrder;
    }

    public PaymentEntity getPayment() {
        return payment;
    }

    public void setPayment(PaymentEntity payment) {
        this.payment = payment;
    }

    public List<OrderDetailEntity> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetailEntity> orderDetails) {
        this.orderDetails = orderDetails;
    }
}