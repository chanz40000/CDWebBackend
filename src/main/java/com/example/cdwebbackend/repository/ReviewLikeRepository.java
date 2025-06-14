package com.example.cdwebbackend.repository;

import com.example.cdwebbackend.entity.ReviewEntity;
import com.example.cdwebbackend.entity.ReviewLikeEntity;
import com.example.cdwebbackend.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewLikeRepository  extends JpaRepository<ReviewLikeEntity, Long> {
    List<ReviewLikeEntity> findByUser(UserEntity user);
    Optional<ReviewLikeEntity> findByUserAndReview(UserEntity user, ReviewEntity review);
    void deleteByReviewId(Long reviewId);

}
