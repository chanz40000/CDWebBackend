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

    @Column(name = "total_price")
    private int totalPrice;

    // @ManyToOne: Quan hệ nhiều - một với StatusOrderEntity
    // 1 đơn hàng chỉ có 1 trạng thái, nhưng 1 trạng thái có thể áp dụng cho nhiều đơn hàng
    @ManyToOne
    @JoinColumn(name = "status_order_id")
    private StatusOrderEntity statusOrder;

    // 1 đơn hàng chỉ có 1 phương thức thanh toán, nhưng 1 phương thức có thể được dùng cho nhiều đơn hàng
    @ManyToOne
    @JoinColumn(name = "payment_id")
    private PaymentEntity payment;

    @Column(name = "create_date")
    private LocalDateTime createDate;

    // @OneToMany: Quan hệ một - nhiều với OrderDetailEntity
    // 1 đơn hàng có thể có nhiều chi tiết đơn hàng, nhưng mỗi chi tiết đơn hàng chỉ thuộc về 1 đơn hàng
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDetailEntity> orderDetails;

    // Getters and Setters

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

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public List<OrderDetailEntity> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetailEntity> orderDetails) {
        this.orderDetails = orderDetails;
    }
}