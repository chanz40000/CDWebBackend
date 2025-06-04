package com.example.cdwebbackend.controller;

import com.example.cdwebbackend.entity.ProductEntity;
import com.example.cdwebbackend.entity.ReviewEntity;
import com.example.cdwebbackend.entity.UserEntity;
import com.example.cdwebbackend.exceptions.DataNotFoundException;
import com.example.cdwebbackend.repository.ProductRepository;
import com.example.cdwebbackend.repository.ReviewRepository;
import com.example.cdwebbackend.repository.UserRepository;
import com.example.cdwebbackend.responses.ReviewListResponse;
import com.example.cdwebbackend.responses.ReviewResponse;
import com.example.cdwebbackend.responses.ReviewStatsResponse;
import com.example.cdwebbackend.service.impl.ImageUploadService;
import com.example.cdwebbackend.util.BannedWordsUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.prefix}/reviews")
public class ReviewController {
    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    ImageUploadService imageUploadService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/product/{productId}")
    public ResponseEntity<ReviewListResponse> getReviewsByProductId(@PathVariable("productId") Long productId) {
        List<ReviewEntity> reviewEntities = reviewRepository.findByProductId(productId);

        // Chuyển thành DTO đánh giá
        List<ReviewResponse> reviewResponses = reviewEntities.stream()
                .map(ReviewResponse::fromEntity)
                .collect(Collectors.toList());

        // Thống kê theo số sao
        List<ReviewStatsResponse> stats = reviewEntities.stream()
                .collect(Collectors.groupingBy(
                        ReviewEntity::getStars,
                        java.util.stream.Collectors.counting()
                ))
                .entrySet().stream()
                .map(entry -> new ReviewStatsResponse(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());

        ReviewListResponse response = new ReviewListResponse(reviewResponses, stats);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<?> createReview(
            @RequestParam("comment") String comment,
            @RequestParam("stars") int stars,
            @RequestParam("productId") Long productId,
            @RequestParam(value = "image", required = false)
            MultipartFile imageFile) throws DataNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        UserEntity user = userRepository.findOneByUsername(username)
                .orElseThrow(() -> new DataNotFoundException("User not found"));

        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new DataNotFoundException("Product not found"));
        //Kiểm tra từ cấm
        if (BannedWordsUtil.containsBannedWords(comment)) {
            return ResponseEntity.badRequest().body("Comment contains inappropriate language");
        }
        ReviewEntity review = new ReviewEntity();
        review.setComment(comment);
        review.setProduct(product);
        review.setStars(stars);
        review.setUser(user);

        // Nếu có ảnh thì upload và lưu URL
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String imageUrl = imageUploadService.uploadFile(imageFile);
                review.setImage(imageUrl);
            } catch (Exception e) {
                return ResponseEntity.badRequest().body("Failed to upload image: " + e.getMessage());
            }
        }

        reviewRepository.save(review);
        return ResponseEntity.ok(ReviewResponse.fromEntity(review));
    }
    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReview(@PathVariable("id") Long id) throws DataNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserEntity user = userRepository.findOneByUsername(username)
                .orElseThrow(() -> new DataNotFoundException("User not found"));

        ReviewEntity review = reviewRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Review not found"));

        if (!review.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(403).body("You are not allowed to delete this review");
        }

        reviewRepository.deleteByIdAndUserId(id, user.getId());
        return ResponseEntity.ok().build();
    }

    // Xóa review không cần kiểm tra userId (ví dụ dành cho admin)
    @Transactional
    @DeleteMapping("/admin/{id}")
    public ResponseEntity<?> deleteReviewByAdmin(@PathVariable("id") Long id) {
        if (!reviewRepository.existsById(id)) {
            return ResponseEntity.badRequest().body("Review not found");
        }
        reviewRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateReview(
            @PathVariable("id") Long id,
            @RequestParam("comment") String comment,
            @RequestParam("stars") int stars) throws DataNotFoundException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        UserEntity user = userRepository.findOneByUsername(username)
                .orElseThrow(() -> new DataNotFoundException("User not found"));

        ReviewEntity review = reviewRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Review not found"));
        //Kiểm tra từ cấm
        if (BannedWordsUtil.containsBannedWords(comment)) {
            return ResponseEntity.badRequest().body("Comment contains inappropriate language");
        }
        // Chỉ chủ comment mới được sửa
        if (!review.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(403).body("You are not allowed to update this review");
        }

        review.setComment(comment);
        review.setStars(stars);

        reviewRepository.save(review);
        return ResponseEntity.ok(ReviewResponse.fromEntity(review));
    }


}
