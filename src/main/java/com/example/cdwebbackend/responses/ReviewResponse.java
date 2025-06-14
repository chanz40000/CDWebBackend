package com.example.cdwebbackend.responses;

import com.example.cdwebbackend.entity.ReviewEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class ReviewResponse {
    private Long id;
    private String comment;
    private String image;
    private Integer stars;
    private Integer likes;
    private Long userId;
    private String userFullName;
    private String username;
    private String avatar;
    private Long productId;

    private List<ReviewResponse> replies;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Date createdDate;

    public static ReviewResponse fromEntity(ReviewEntity entity) {
        return ReviewResponse.builder()
                .id(entity.getId())
                .comment(entity.getComment())
                .image(entity.getImage())
                .stars(entity.getStars())
                .likes(entity.getLikes())
                .userId(entity.getUser() != null ? entity.getUser().getId() : null)
                .userFullName(entity.getUser() != null ? entity.getUser().getFullname() : null)
                .username(entity.getUser() != null ? entity.getUser().getUsername() : null)
                .avatar(entity.getUser() != null ? entity.getUser().getAvatar() : null)
                .productId(entity.getProduct() != null ? entity.getProduct().getId() : null)
                .createdDate(entity.getCreatedDate())
                .replies(entity.getReplies() != null
                        ? entity.getReplies().stream()
                        .map(ReviewResponse::fromEntity)
                        .collect(Collectors.toList())
                        : null)
                .build();
    }
}
