package com.example.cdwebbackend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "order_detail")
public class OrderDetailEntity extends BaseEntity {
    // 1 chi tiết đơn hàng thuộc về 1 đơn hàng duy nhất
    @ManyToOne
    @JoinColumn(name = "order_id")
    private OrderEntity order;


    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    @Column(name = "quantity")
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "color_id")
    private ColorEntity color;

    @ManyToOne
    @JoinColumn(name = "size_id")
    private SizeEntity size;

    @Column(name = "price_unit")
    private int priceUnit;

    @Column(name = "subtotal")
    private int subtotal;

    // Getters and Setters

    public OrderEntity getOrder() {
        return order;
    }

    public void setOrder(OrderEntity order) {
        this.order = order;
    }

    public ProductEntity getProduct() {
        return product;
    }

    public void setProduct(ProductEntity product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public ColorEntity getColor() {
        return color;
    }

    public void setColor(ColorEntity color) {
        this.color = color;
    }

    public SizeEntity getSize() {
        return size;
    }

    public void setSize(SizeEntity size) {
        this.size = size;
    }

    public int getPriceUnit() {
        return priceUnit;
    }

    public void setPriceUnit(int priceUnit) {
        this.priceUnit = priceUnit;
    }

    public int getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(int subtotal) {
        this.subtotal = subtotal;
    }
}
