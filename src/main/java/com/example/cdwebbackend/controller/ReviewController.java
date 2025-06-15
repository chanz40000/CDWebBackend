package com.example.cdwebbackend.controller;

import com.example.cdwebbackend.dto.ProductDTO;
import com.example.cdwebbackend.entity.ProductEntity;
import com.example.cdwebbackend.entity.ReviewEntity;
import com.example.cdwebbackend.entity.ReviewLikeEntity;
import com.example.cdwebbackend.entity.UserEntity;
import com.example.cdwebbackend.exceptions.DataNotFoundException;
import com.example.cdwebbackend.repository.ProductRepository;
import com.example.cdwebbackend.repository.ReviewLikeRepository;
import com.example.cdwebbackend.repository.ReviewRepository;
import com.example.cdwebbackend.repository.UserRepository;
import com.example.cdwebbackend.responses.ReviewListResponse;
import com.example.cdwebbackend.responses.ReviewResponse;
import com.example.cdwebbackend.responses.ReviewStatsResponse;
import com.example.cdwebbackend.service.impl.ImageUploadService;
import com.example.cdwebbackend.util.BannedWordsUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.*;
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

    @Autowired
    private ReviewLikeRepository reviewLikeRepository;

    @GetMapping("/product/{productId}")
    public ResponseEntity<?> getReviewsByProductId(
            @PathVariable("productId") Long productId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "3") int size) {

        Pageable pageable = PageRequest.of(page, size);

        // Lấy dữ liệu phân trang
        Page<ReviewEntity> reviewPage = reviewRepository.findByProductId(productId, pageable);

        List<ReviewResponse> reviewResponses = reviewPage.getContent().stream()
                .map(ReviewResponse::fromEntity)
                .collect(Collectors.toList());

        // Thống kê toàn bộ reviews theo sao (không phân trang)
        List<ReviewStatsResponse> stats = reviewRepository.findByProductId(productId).stream()
                .filter(r -> r.getStars() != null)
                .collect(Collectors.groupingBy(
                        ReviewEntity::getStars,
                        Collectors.counting()
                ))
                .entrySet().stream()
                .map(entry -> new ReviewStatsResponse(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());

        // Đóng gói phản hồi
        Map<String, Object> response = new HashMap<>();
        response.put("reviews", reviewResponses);              // Danh sách đánh giá trong trang hiện tại
        response.put("stats", stats);                          // Thống kê sao
        response.put("currentPage", reviewPage.getNumber());   // Trang hiện tại
        response.put("totalItems", reviewPage.getTotalElements()); // Tổng số đánh giá
        response.put("totalPages", reviewPage.getTotalPages());    // Tổng số trang
        response.put("pageSize", reviewPage.getSize());        // Kích thước mỗi trang

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

    @PostMapping("/reply")
    public ResponseEntity<?> replyToReview(
            @RequestParam("parentReviewId") Long parentReviewId,
            @RequestParam("comment") String comment
    ) throws DataNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        UserEntity user = userRepository.findOneByUsername(username)
                .orElseThrow(() -> new DataNotFoundException("User not found"));

        ReviewEntity parentReview = reviewRepository.findById(parentReviewId)
                .orElseThrow(() -> new DataNotFoundException("Parent review not found"));

        if (BannedWordsUtil.containsBannedWords(comment)) {
            return ResponseEntity.badRequest().body("Comment contains inappropriate language");
        }

        ReviewEntity reply = new ReviewEntity();
        reply.setComment(comment);
        reply.setUser(user);
        reply.setProduct(parentReview.getProduct()); // lấy từ review cha
        reply.setParentReview(parentReview);
        reply.setStars(null); // trả lời không có đánh giá sao

        reviewRepository.save(reply);

        return ResponseEntity.ok(ReviewResponse.fromEntity(reply));
    }

    @GetMapping("/product/{productId}/with-likes")
    public ResponseEntity<?> getReviewsWithLikes(
            @PathVariable("productId") Long productId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "3") int size) throws DataNotFoundException {

        // Lấy thông tin user đang đăng nhập
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserEntity user = userRepository.findOneByUsername(username)
                .orElseThrow(() -> new DataNotFoundException("User not found"));

        // Phân trang
        Pageable pageable = PageRequest.of(page, size);

        // Lấy chỉ các bình luận top-level (parentReviewId = null)
        Page<ReviewEntity> reviewPage = reviewRepository.findByProductIdAndParentReviewIdIsNull(productId, pageable);

        // Lấy danh sách các review mà user đã like
        List<Long> likedReviewIds = reviewLikeRepository.findByUser(user).stream()
                .map(reviewLike -> reviewLike.getReview().getId())
                .collect(Collectors.toList());

        // Chuyển đổi dữ liệu để trả về
        List<Map<String, Object>> reviewList = reviewPage.getContent().stream().map(review -> {
            Map<String, Object> reviewMap = new HashMap<>();
            reviewMap.put("id", review.getId());
            reviewMap.put("username", review.getUser().getUsername());
            reviewMap.put("userFullName", review.getUser().getFullname());
            reviewMap.put("avatar", review.getUser().getAvatar());
            reviewMap.put("stars", review.getStars());
            reviewMap.put("comment", review.getComment());
            reviewMap.put("image", review.getImage());
            reviewMap.put("likes", review.getLikes());
            reviewMap.put("createdDate", review.getCreatedDate());
            reviewMap.put("liked", likedReviewIds.contains(review.getId()));

            // Lấy danh sách replies (chỉ lấy các reply trực tiếp)
            List<Map<String, Object>> replies = review.getReplies().stream().map(reply -> {
                Map<String, Object> replyMap = new HashMap<>();
                replyMap.put("id", reply.getId());
                replyMap.put("username", reply.getUser().getUsername());
                replyMap.put("userFullName", reply.getUser().getFullname());
                replyMap.put("avatar", reply.getUser().getAvatar());
                replyMap.put("createdDate", reply.getCreatedDate());
                replyMap.put("likes", reply.getLikes());
                replyMap.put("comment", reply.getComment());
                return replyMap;
            }).collect(Collectors.toList());
            reviewMap.put("replies", replies);

            return reviewMap;
        }).collect(Collectors.toList());

        // Tính trung bình số sao
        double averageRating = reviewRepository.findByProductIdAndParentReviewIdIsNull(productId).stream()
                .filter(r -> r.getStars() != null)
                .mapToDouble(ReviewEntity::getStars)
                .average()
                .orElse(0.0);

        // Tính tổng số đánh giá (chỉ đếm các bình luận có sao)
        long totalReviews = reviewRepository.findByProductIdAndParentReviewIdIsNull(productId).stream()
                .filter(r -> r.getStars() != null)
                .count();

        // Trả về response
        Map<String, Object> response = new HashMap<>();
        response.put("reviews", reviewList);
        response.put("averageRating", averageRating);
        response.put("currentPage", reviewPage.getNumber());
        response.put("totalItems", reviewPage.getTotalElements());
        response.put("totalReviews", totalReviews);
        response.put("totalPages", reviewPage.getTotalPages());

        // Thêm thống kê ratings
        List<Map<String, Integer>> stats = reviewRepository.getStatsByProductId(productId);
        response.put("stats", stats);

        return ResponseEntity.ok(response);
    }
    @GetMapping("/rating-summary/{productId}")
    public ResponseEntity<?> getRatingSummary(@PathVariable("productId") Long productId) {
        // Lấy tất cả review cấp cao nhất của sản phẩm
        List<ReviewEntity> topLevelReviews = reviewRepository.findByProductIdAndParentReviewIdIsNull(productId);

        // Tính trung bình số sao (chỉ tính các review có sao)
        double averageRating = topLevelReviews.stream()
                .filter(r -> r.getStars() != null)
                .mapToDouble(ReviewEntity::getStars)
                .average()
                .orElse(0.0);

        // Đếm số review có số sao
        long totalReviews = topLevelReviews.stream()
                .filter(r -> r.getStars() != null)
                .count();

        // Thống kê chi tiết số lượng từng loại sao (nếu bạn có phương thức thống kê trong repository)
        List<Map<String, Integer>> stats = reviewRepository.getStatsByProductId(productId);

        // Trả về response
        Map<String, Object> response = new HashMap<>();
        response.put("averageRating", averageRating);
        response.put("totalReviews", totalReviews);
        response.put("stats", stats);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<?> toggleLikeReview(@PathVariable("id") Long id) throws DataNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        UserEntity user = userRepository.findOneByUsername(username)
                .orElseThrow(() -> new DataNotFoundException("User not found"));

        ReviewEntity review = reviewRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Review not found"));

        // Kiểm tra user đã like chưa
        var existingLike = reviewLikeRepository.findByUserAndReview(user, review);
        if (existingLike.isPresent()) {
            // Nếu đã like → unlike
            reviewLikeRepository.delete(existingLike.get());

            Integer currentLikes = review.getLikes();
            if (currentLikes != null && currentLikes > 0) {
                review.setLikes(currentLikes - 1);
            } else {
                review.setLikes(0);
            }
            reviewRepository.save(review);

            return ResponseEntity.ok(Map.of(
                    "message", "Unliked successfully",
                    "review", ReviewResponse.fromEntity(review),
                    "liked", false
            ));
        } else {
            // Nếu chưa like → like
            ReviewLikeEntity reviewLike = new ReviewLikeEntity();
            reviewLike.setUser(user);
            reviewLike.setReview(review);
            reviewLikeRepository.save(reviewLike);

            Integer currentLikes = review.getLikes();
            if (currentLikes == null) currentLikes = 0;
            review.setLikes(currentLikes + 1);
            reviewRepository.save(review);

            return ResponseEntity.ok(Map.of(
                    "message", "Liked successfully",
                    "review", ReviewResponse.fromEntity(review),
                    "liked", true
            ));
        }
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

        reviewLikeRepository.deleteByReviewId(id);
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
        reviewLikeRepository.deleteByReviewId(id);
        reviewRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateReview(
            @PathVariable("id") Long id,
            @RequestParam("comment") String comment,
            @RequestParam(value = "stars", required = false) Integer stars) throws DataNotFoundException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        UserEntity user = userRepository.findOneByUsername(username)
                .orElseThrow(() -> new DataNotFoundException("User not found"));

        ReviewEntity review = reviewRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Review not found"));

        // Kiểm tra từ cấm
        if (BannedWordsUtil.containsBannedWords(comment)) {
            return ResponseEntity.badRequest().body("Comment contains inappropriate language");
        }

        // Chỉ chủ comment mới được sửa
        if (!review.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(403).body("You are not allowed to update this review");
        }

        review.setComment(comment);

        // Chỉ cập nhật stars nếu người dùng truyền vào
        if (stars != null) {
            review.setStars(stars);
        }
        review.setCreatedDate(new Date());


        reviewRepository.save(review);
        return ResponseEntity.ok(ReviewResponse.fromEntity(review));
    }



}
