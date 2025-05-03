package com.example.cdwebbackend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "cart_item")
public class CartItemEntity extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "cart_id")
    private CartEntity cart;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    @ManyToOne
    @JoinColumn(name = "product_size_color_id")
    private ProductSizeColorEntity productSizeColor;

    @Column
    private int quantity;

    public CartItemEntity() {
    }

    public CartEntity getCart() {
        return cart;
    }

    public void setCart(CartEntity cart) {
        this.cart = cart;
    }

    public ProductEntity getProduct() {
        return product;
    }

    public void setProduct(ProductEntity product) {
        this.product = product;
    }

    public ProductSizeColorEntity getProductSizeColor() {
        return productSizeColor;
    }

    public void setProductSizeColor(ProductSizeColorEntity productSizeColor) {
        this.productSizeColor = productSizeColor;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
