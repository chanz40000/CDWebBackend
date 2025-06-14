package com.example.cdwebbackend.controller;

import com.example.cdwebbackend.dto.CouponDTO;
import com.example.cdwebbackend.entity.CouponEntity;
import com.example.cdwebbackend.entity.CouponTypeEntity;
import com.example.cdwebbackend.entity.CouponUserEntity;
import com.example.cdwebbackend.entity.UserEntity;
import com.example.cdwebbackend.exceptions.DataNotFoundException;
import com.example.cdwebbackend.repository.CouponRepository;
import com.example.cdwebbackend.repository.CouponTypeRepository;
import com.example.cdwebbackend.repository.CouponUserRepository;
import com.example.cdwebbackend.repository.UserRepository;
import com.example.cdwebbackend.responses.CouponResponse;
import com.example.cdwebbackend.responses.CouponUserResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("${api.prefix}/coupons")
public class CouponController {

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private CouponUserRepository couponUserRepository;

    @Autowired
    private CouponTypeRepository couponTypeRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<CouponResponse>> getAllCoupons() {
        List<CouponEntity> couponEntities = couponRepository.findByIsActiveTrue();
        List<CouponResponse> responses = couponEntities.stream()
                .map(CouponResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    // GET: Lấy danh sách coupon theo coupon_type_id = 3 và is_active = true
    @GetMapping("/type/3")
    public ResponseEntity<List<CouponResponse>> getCouponsType3() {
        List<CouponEntity> coupons = couponRepository.findByCouponTypeIdAndIsActiveTrue(3L);
        List<CouponResponse> responses = coupons.stream()
                .map(CouponResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    // GET: Lấy danh sách coupon theo coupon_type_id = 1 hoặc 2 và is_active = true
    @GetMapping("/type/1-2")
    public ResponseEntity<List<CouponResponse>> getCouponsType1And2() {
        List<Long> typeIds = List.of(1L, 2L);
        List<CouponEntity> coupons = couponRepository.findByCouponTypeIdInAndIsActiveTrue(typeIds);
        List<CouponResponse> responses = coupons.stream()
                .map(CouponResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }
    @GetMapping("/code/{couponCode}")
    public ResponseEntity<CouponResponse> getCouponByCode(@PathVariable("couponCode") String couponCode) {
        return couponRepository.findByCode(couponCode)
                .map(coupon -> ResponseEntity.ok(CouponResponse.fromEntity(coupon)))
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/user/type/1-2")
    public ResponseEntity<List<CouponUserResponse>> getAllCouponsByUserIdInType1And2
            () throws DataNotFoundException {
        List<Long> typeIds = List.of(1L, 2L);
        // Lấy thông tin người dùng từ SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        UserEntity user = userRepository.findOneByUsername(username)
                .orElseThrow(() -> new DataNotFoundException("User not found"));


        List<CouponUserEntity> couponUsers = couponUserRepository.findActiveCouponsByUserIdAndCouponTypeIds(user.getId(), typeIds);

        List<CouponUserResponse> couponUserResponses = couponUsers.stream()
                .map(CouponUserResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(couponUserResponses);
    }

    @GetMapping("/user/get")
    public ResponseEntity<List<CouponUserResponse>> getAllCouponsByUserIdInType3
            () throws DataNotFoundException {
        List<Long> typeIds = List.of(1L, 2L, 3L);
        // Lấy thông tin người dùng từ SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        UserEntity user = userRepository.findOneByUsername(username)
                .orElseThrow(() -> new DataNotFoundException("User not found"));


        List<CouponUserEntity> couponUsers = couponUserRepository.findActiveCouponsByUserIdAndCouponTypeIds(user.getId(), typeIds);

        List<CouponUserResponse> couponUserResponses = couponUsers.stream()
                .map(CouponUserResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(couponUserResponses);
    }

    @GetMapping("/user")
    public ResponseEntity<List<CouponUserResponse>> getAllCouponsByUserId
            () throws DataNotFoundException {
        List<Long> typeIds = List.of(1L, 2L);
        // Lấy thông tin người dùng từ SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        UserEntity user = userRepository.findOneByUsername(username)
                .orElseThrow(() -> new DataNotFoundException("User not found"));


        List<CouponUserEntity> couponUsers = couponUserRepository.findByUserId(user.getId());

        List<CouponUserResponse> couponUserResponses = couponUsers.stream()
                .map(CouponUserResponse::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(couponUserResponses);
    }

    // Thêm phương thức lưu mã giảm giá cho người dùng
    @PostMapping("/user/save")
    public ResponseEntity<?> saveCouponForUser(@RequestParam("couponCode") String couponCode) throws DataNotFoundException {
        // Lấy thông tin người dùng từ SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        UserEntity user = userRepository.findOneByUsername(username)
                .orElseThrow(() -> new DataNotFoundException("User not found"));

        // Tìm mã giảm giá theo couponCode
        CouponEntity coupon = couponRepository.findByCode(couponCode)
                .orElseThrow(() -> new DataNotFoundException("Coupon not found"));

        // Kiểm tra mã giảm giá có hợp lệ không
        LocalDateTime now = LocalDateTime.now();
        if (!coupon.isActive()) {
            throw new DataNotFoundException("Coupon is not active");
        }
        if (coupon.getStartDate().isAfter(now) || coupon.getEndDate().isBefore(now)) {
            throw new DataNotFoundException("Coupon is not valid at this time");
        }
        if (coupon.getQuantity() <= 0) {
            throw new DataNotFoundException("Coupon is out of stock");
        }

        // Kiểm tra xem người dùng đã lưu mã này chưa
        CouponUserEntity couponUser = couponUserRepository.findByUserIdAndCouponId(user.getId(), coupon.getId())
                .orElse(null);

        if (couponUser != null) {
            // Kiểm tra số lần sử dụng
            if (couponUser.getUsageCount() >= coupon.getMaxUsesPerUser()) {
                throw new DataNotFoundException("User has reached the maximum usage limit for this coupon");
            }
            // Tăng usageCount
            couponUser.setUsageCount(couponUser.getUsageCount() + 1);
            if (couponUser.getUsageCount() >= coupon.getMaxUsesPerUser()) {
                couponUser.setUsed(true);
            }
        } else {
            // Tạo mới CouponUserEntity
            couponUser = new CouponUserEntity();
            couponUser.setUser(user);
            couponUser.setCoupon(coupon);
            couponUser.setUsageCount(0);
            couponUser.setUsed(coupon.getMaxUsesPerUser() == 1);
        }

        // Lưu bản ghi CouponUserEntity
        couponUser = couponUserRepository.save(couponUser);

        // Giảm số lượng mã giảm giá
        coupon.setQuantity(coupon.getQuantity() - 1);
        couponRepository.save(coupon);

        return ResponseEntity.ok(CouponUserResponse.fromEntity(couponUser));
    }

    // Lấy chi tiết coupon theo ID
    @GetMapping("/{id}")
    public ResponseEntity<CouponResponse> getCouponById(@PathVariable("id")  Long id) {
        return couponRepository.findById(id)
                .map(coupon -> ResponseEntity.ok(CouponResponse.fromEntity(coupon)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCoupon(@PathVariable("id") Long id) {
        if (!couponRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        couponRepository.deleteById(id);
        return ResponseEntity.ok("Coupon deleted successfully");
    }

    @PostMapping
    public ResponseEntity<?> createCoupon(@Valid @RequestBody CouponDTO dto) throws DataNotFoundException {
        // Tìm coupon type theo couponTypeId trong DTO
        CouponTypeEntity couponType = couponTypeRepository.findById(dto.getCouponType())
                .orElseThrow(() -> new DataNotFoundException("Coupon type not found"));

        // Tạo coupon entity mới
        CouponEntity coupon = new CouponEntity();
        coupon.setCode(dto.getCode());
        coupon.setCouponType(couponType);
        coupon.setDiscountValue((int) dto.getDiscountValue()); // Nếu discountValue của bạn là int trong entity
        coupon.setStartDate(dto.getStartDate());
        coupon.setEndDate(dto.getEndDate());
        coupon.setMaxDiscountAmount(dto.getMaxDiscountAmount());
        coupon.setMinOrderValue(dto.getMinOrderAmount() != null ? dto.getMinOrderAmount().intValue() : null);
        coupon.setQuantity(dto.getQuantity());
        coupon.setMaxUsesPerUser(dto.getMaxUsesPerUser());
        coupon.setMinProductQuantity(dto.getMinProductQuantity());
        coupon.setActive(dto.isActive());

        couponRepository.save(coupon);

        return ResponseEntity.ok(CouponResponse.fromEntity(coupon));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCoupon(@PathVariable("id") Long id, @Valid @RequestBody CouponDTO dto) throws DataNotFoundException {
        CouponEntity existingCoupon = couponRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Coupon not found"));

        CouponTypeEntity couponType = couponTypeRepository.findById(dto.getCouponType())
                .orElseThrow(() -> new DataNotFoundException("Coupon type not found"));

        // Cập nhật các trường
        existingCoupon.setCode(dto.getCode());
        existingCoupon.setCouponType(couponType);
        existingCoupon.setDiscountValue((int) dto.getDiscountValue());
        existingCoupon.setStartDate(dto.getStartDate());
        existingCoupon.setMaxDiscountAmount(dto.getMaxDiscountAmount());
        existingCoupon.setEndDate(dto.getEndDate());
        existingCoupon.setMinOrderValue(dto.getMinOrderAmount() != null ? dto.getMinOrderAmount().intValue() : null);
        existingCoupon.setQuantity(dto.getQuantity());
        existingCoupon.setMaxUsesPerUser(dto.getMaxUsesPerUser());
        existingCoupon.setMinProductQuantity(dto.getMinProductQuantity());
        existingCoupon.setActive(dto.isActive());

        couponRepository.save(existingCoupon);

        return ResponseEntity.ok(CouponResponse.fromEntity(existingCoupon));
    }

    //	Cập nhật một phần (chỉ thay đổi những trường cần thiết) ch cần cập nhật cái is_active
    @PatchMapping("/{id}/toggle-active")
    public ResponseEntity<?> toggleCouponActiveStatus(@PathVariable("id") Long id, @RequestParam("active") boolean active) throws DataNotFoundException {
        CouponEntity coupon = couponRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Coupon not found"));
        coupon.setActive(active);
        couponRepository.save(coupon);
        return ResponseEntity.ok(CouponResponse.fromEntity(coupon));
    }

    @GetMapping("/user/has")
    public ResponseEntity<Boolean> checkIfUserHasCoupon(@RequestParam("couponCode") String couponCode) throws DataNotFoundException {
        // Lấy thông tin người dùng hiện tại từ SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        UserEntity user = userRepository.findOneByUsername(username)
                .orElseThrow(() -> new DataNotFoundException("User not found"));

        // Tìm mã giảm giá theo couponCode
        CouponEntity coupon = couponRepository.findByCode(couponCode)
                .orElseThrow(() -> new DataNotFoundException("Coupon not found"));

        // Kiểm tra xem người dùng đã có mã giảm giá này chưa
        boolean hasCoupon = couponUserRepository.existsByUserIdAndCouponId(user.getId(), coupon.getId());

        return ResponseEntity.ok(hasCoupon);
    }


}
