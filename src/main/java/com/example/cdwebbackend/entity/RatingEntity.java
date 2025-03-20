package com.example.cdwebbackend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "rating")
public class RatingEntity extends BaseEntity {

    // @ManyToOne: Quan hệ nhiều - một với UserEntity
    // Mỗi đánh giá thuộc về 1 người dùng
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    // @ManyToOne: Quan hệ nhiều - một với ProductEntity
    // Mỗi đánh giá thuộc về 1 sản phẩm
    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    // từ 1 đến 5 sao
    @Column(name = "stars")
    private Integer stars;

    // Getters và Setters

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public ProductEntity getProduct() {
        return product;
    }

    public void setProduct(ProductEntity product) {
        this.product = product;
    }

    public Integer getStars() {
        return stars;
    }

    public void setStars(Integer stars) {
        this.stars = stars;
    }
}
