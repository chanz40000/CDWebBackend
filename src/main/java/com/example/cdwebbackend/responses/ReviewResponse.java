package com.example.cdwebbackend.responses;

import com.example.cdwebbackend.entity.ReviewEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Builder
public class ReviewResponse {
    private Long id;
    private String comment;
    private int stars;
    private Long userId;
    private String userFullName;
    private Long productId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Date createdDate;

    public static ReviewResponse fromEntity(ReviewEntity entity) {
        return ReviewResponse.builder()
                .id(entity.getId())
                .comment(entity.getComment())
                .stars(entity.getStars())
                .userId(entity.getUser() != null ? entity.getUser().getId() : null)
                .userFullName(entity.getUser() != null ? entity.getUser().getFullname() : null)
                .productId(entity.getProduct() != null ? entity.getProduct().getId() : null)
                .createdDate(entity.getCreatedDate())
                .build();
    }
}
