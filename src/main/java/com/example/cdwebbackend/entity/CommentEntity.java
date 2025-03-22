package com.example.cdwebbackend.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "comment")
public class CommentEntity extends BaseEntity {

    // @ManyToOne: Quan hệ nhiều - một với UserEntity
    // Mỗi bình luận thuộc về 1 người dùng
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(name = "comment")
    private String comment;

    @Column(name = "date")
    private LocalDateTime date;

    // @ManyToOne: Quan hệ nhiều - một với ProductEntity
    // Mỗi bình luận thuộc về 1 sản phẩm
    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    // Getters và Setters

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public ProductEntity getProduct() {
        return product;
    }

    public void setProduct(ProductEntity product) {
        this.product = product;
    }
}