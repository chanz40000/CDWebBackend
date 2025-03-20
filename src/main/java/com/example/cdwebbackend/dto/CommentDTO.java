package com.example.cdwebbackend.dto;

import java.time.LocalDateTime;

public class CommentDTO extends AbstractDTO<CommentDTO> {
    private Long userId;
    private Long productId;
    private String comment;
    private LocalDateTime date;

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }
}