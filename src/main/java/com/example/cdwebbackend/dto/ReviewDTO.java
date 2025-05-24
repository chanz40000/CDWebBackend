package com.example.cdwebbackend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ReviewDTO extends AbstractDTO<ReviewDTO> {

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("product_id")
    private Long productId;

    @JsonProperty("comment")
    private String comment;

    @JsonProperty("stars")
    private Integer stars;

    // Getters và Setters

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
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
