package com.example.cdwebbackend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.List;

@Entity
@Table(name = "payment")
public class PaymentEntity extends BaseEntity {

    @Column(name = "method")
    private String method;

    // 1 phương thức thanh toán có thể được sử dụng trong nhiều đơn hàng
    @OneToMany(mappedBy = "payment")
    private List<OrderEntity> orders;

    // Getters and Setters

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public List<OrderEntity> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderEntity> orders) {
        this.orders = orders;
    }
}