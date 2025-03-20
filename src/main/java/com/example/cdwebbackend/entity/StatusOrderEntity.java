package com.example.cdwebbackend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.List;

@Entity
@Table(name = "status_order")
public class StatusOrderEntity extends BaseEntity {

    @Column(name = "name")
    private String name;

    // 1 trạng thái có thể áp dụng cho nhiều đơn hàng
    @OneToMany(mappedBy = "statusOrder")
    private List<OrderEntity> orders;

    // Getters and Setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<OrderEntity> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderEntity> orders) {
        this.orders = orders;
    }
}
