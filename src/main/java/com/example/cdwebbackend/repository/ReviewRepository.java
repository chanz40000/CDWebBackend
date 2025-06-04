package com.example.cdwebbackend.repository;

import com.example.cdwebbackend.entity.RatingEntity;
import com.example.cdwebbackend.entity.ReviewEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ReviewRepository  extends JpaRepository<ReviewEntity, Long> {
    ReviewEntity findOneById(long id);
    List<ReviewEntity> findByProductId(long productId);
    void deleteByIdAndUserId(long id, long userId);
    Page<ReviewEntity> findByProductId(Long productId, Pageable pageable);
    @Query("SELECT new map(r.stars as stars, COUNT(r) as total) FROM ReviewEntity r WHERE r.product.id = :productId GROUP BY r.stars")
    List<Map<String, Integer>> getStatsByProductId(@Param("productId") Long productId);

    Page<ReviewEntity> findByProductIdAndParentReviewIdIsNull(Long productId, Pageable pageable);
}
