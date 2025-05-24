package com.example.cdwebbackend.repository;

import com.example.cdwebbackend.entity.RatingEntity;
import com.example.cdwebbackend.entity.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository  extends JpaRepository<ReviewEntity, Long> {
    ReviewEntity findOneById(long id);
    List<ReviewEntity> findByProductId(long productId);
    void deleteByIdAndUserId(long id, long userId);

}
