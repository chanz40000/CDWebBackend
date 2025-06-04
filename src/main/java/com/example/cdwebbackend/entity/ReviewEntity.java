package com.example.cdwebbackend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "review")
public class ReviewEntity extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;

    @Column(name = "comment", columnDefinition = "TEXT")
    private String comment;

    @Column(name = "image", columnDefinition = "TEXT")
    private String image;

    @Column(name = "likes", nullable = false)
    private Integer likes = 0;  // mặc định 0


    @Column(name = "stars")
    private Integer stars; // từ 1 đến 5

    @ManyToOne
    @JoinColumn(name = "parent_review_id")
    private ReviewEntity parentReview;

    @OneToMany(mappedBy = "parentReview", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewEntity> replies = new ArrayList<>();


    // Getters và Setters


    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public ReviewEntity getParentReview() {
        return parentReview;
    }

    public void setParentReview(ReviewEntity parentReview) {
        this.parentReview = parentReview;
    }

    public List<ReviewEntity> getReplies() {
        return replies;
    }

    public void setReplies(List<ReviewEntity> replies) {
        this.replies = replies;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getStars() {
        return stars;
    }

    public void setStars(Integer stars) {
        this.stars = stars;
    }

}
