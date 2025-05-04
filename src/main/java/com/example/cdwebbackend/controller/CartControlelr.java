package com.example.cdwebbackend.controller;

import com.example.cdwebbackend.converter.CartConverter;
import com.example.cdwebbackend.converter.CartItemConverter;
import com.example.cdwebbackend.dto.CartDTO;
import com.example.cdwebbackend.dto.CartItemDTO;
import com.example.cdwebbackend.entity.CartEntity;
import com.example.cdwebbackend.entity.CartItemEntity;
import com.example.cdwebbackend.entity.UserEntity;
import com.example.cdwebbackend.exceptions.DataNotFoundException;
import com.example.cdwebbackend.repository.CartRepository;
import com.example.cdwebbackend.repository.UserRepository;
import com.example.cdwebbackend.responses.CartItemResponse;
import com.example.cdwebbackend.responses.CartResponse;
import com.example.cdwebbackend.responses.ProductResponse;
import com.example.cdwebbackend.service.ICartService;
import com.example.cdwebbackend.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("${api.prefix}/carts")
public class CartControlelr {

    @Autowired
    private ICartService cartService;

    @Autowired
    private UserRepository  userRepository;

    @Autowired
    private CartConverter cartConverter;
    @Autowired
    private CartItemConverter cartItemConverter;

    @Autowired
    private CartRepository cartRepository;

    // Thêm sản phẩm vào giỏ hàng
    @PostMapping("/add")
    public ResponseEntity<?> addToCart(
                                       @RequestParam("productId") Long productId,
                                       @RequestParam("productSizeColorId") Long productSizeColorId,
                                       @RequestParam("quantity") int quantity) {
        try {

            // Lấy thông tin người dùng từ token
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            // Lấy user và cart tương ứng
            UserEntity user = userRepository.findOneByUsername(username)
                    .orElseThrow(() -> new DataNotFoundException("User not found"));


            cartService.addToCart(user.getId(), productId, productSizeColorId, quantity);
            // Lấy lại cart sau khi cập nhật và convert sang response
            CartDTO updatedCartDTO = cartService.getCartByUserId(user.getId());
            CartResponse response = CartResponse.fromEntity(cartConverter.toEntity(updatedCartDTO));

            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Thêm vào giỏ hàng thành công",
                    "data", response
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "status", "failed",
                    "message", e.getMessage()
            ));
        }
    }

    // Cập nhật số lượng sản phẩm trong giỏ hàng
    @PutMapping("/update-quantity")
    public ResponseEntity<?> updateQuantity(
            @RequestParam("cartItemId") Long cartItemId,
            @RequestParam("quantity") int quantity) {
        try {
            // Lấy thông tin người dùng từ token
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            // Lấy user và cart tương ứng
            UserEntity user = userRepository.findOneByUsername(username)
                    .orElseThrow(() -> new DataNotFoundException("User not found"));

            cartService.updateQuantity(user.getId(), cartItemId, quantity);

            // Lấy lại cart sau khi cập nhật và convert sang response
            CartDTO updatedCartDTO = cartService.getCartByUserId(user.getId());
            CartResponse response = CartResponse.fromEntity(cartConverter.toEntity(updatedCartDTO));

            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Cập nhật số lượng thành công",
                    "data", response
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "status", "failed",
                    "message", e.getMessage()
            ));
        }
    }

    // Xóa sản phẩm khỏi giỏ hàng
    @DeleteMapping("/remove-item")
    public ResponseEntity<?> removeItem(
            @RequestParam("cartItemId") Long cartItemId) {
        try {


            // Lấy thông tin người dùng từ token (SecurityContext)
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            // Lấy thông tin user và cart
            UserEntity user = userRepository.findOneByUsername(username)
                    .orElseThrow(() -> new DataNotFoundException("User not found"));

            // Gọi service để xóa sản phẩm
            cartService.removeItemFromCart(user.getId(), cartItemId);

            // Lấy lại thông tin giỏ hàng mới sau khi xóa
            CartEntity updatedCart = cartRepository.findByUserId(user.getId())
                    .orElseThrow(() -> new DataNotFoundException("Cart not found after update"));
            CartDTO updatedCartDTO = cartConverter.toDTO(updatedCart);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Xóa sản phẩm khỏi giỏ hàng thành công",
                    "data", updatedCartDTO
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "status", "failed",
                    "message", e.getMessage()
            ));
        }
    }
    @DeleteMapping("/remove-items")
    public ResponseEntity<?> removeItems(@RequestParam("cartItemId") List<Long> cartItemIds) {
        try {
            // Lấy thông tin người dùng từ token (SecurityContext)
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            // Lấy thông tin user
            UserEntity user = userRepository.findOneByUsername(username)
                    .orElseThrow(() -> new DataNotFoundException("User not found"));

            // Gọi service để xoá từng item theo ID
            for (Long cartItemId : cartItemIds) {
                cartService.removeItemFromCart(user.getId(), cartItemId);
            }

            // Lấy lại thông tin giỏ hàng mới
            CartEntity updatedCart = cartRepository.findByUserId(user.getId())
                    .orElseThrow(() -> new DataNotFoundException("Cart not found after update"));
            CartDTO updatedCartDTO = cartConverter.toDTO(updatedCart);
            CartResponse response = CartResponse.fromEntity(cartConverter.toEntity(updatedCartDTO));

            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Xoá nhiều sản phẩm khỏi giỏ hàng thành công",
                    "data", response
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "status", "failed",
                    "message", e.getMessage()
            ));
        }
    }


    // Cập nhật biến thể sản phẩm (màu/kích cỡ)
    @PutMapping("/update-size-color")
    public ResponseEntity<?> updateProductVariant(@RequestParam("cartItemId") Long cartItemId,
                                                  @RequestParam("newProductSizeColorId") Long newProductSizeColorId) {
        try {

            // Lấy thông tin người dùng từ token (SecurityContext)
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            // Lấy thông tin user và cart
            UserEntity user = userRepository.findOneByUsername(username)
                    .orElseThrow(() -> new DataNotFoundException("User not found"));
            CartEntity cart = cartRepository.findByUserId(user.getId())
                    .orElseGet(() -> {
                        CartEntity cartEntity = new CartEntity();
                        cartEntity.setUser(user);
                        return cartRepository.save(cartEntity);
                    });

            CartItemDTO cartItemDTO = cartService.updateProductVariant(cartItemId, newProductSizeColorId);
            // Convert DTO thành Entity (cần CartEntity để convert)
            CartItemEntity cartItemEntity = cartItemConverter.toEntity(cartItemDTO, cart);

            // Trả về response
            CartItemResponse response = CartItemResponse.fromEntity(cartItemEntity);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "Cập nhật biến thể sản phẩm thành công",
                    "data", response
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "status", "failed",
                    "message", e.getMessage()
            ));
        }
    }

@GetMapping
public ResponseEntity<?> getCartByUserIdFromToken() {
    try {
        // Lấy thông tin người dùng từ SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // Tìm user theo username
        UserEntity user = userRepository.findOneByUsername(username)
                .orElseThrow(() -> new DataNotFoundException("User not found"));

        CartDTO cartDTO = cartService.getCartByUserId(user.getId());
        if (cartDTO == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                    "status", "failed",
                    "message", "Không tìm thấy giỏ hàng cho userId = " + user.getId()
            ));
        }
        CartResponse response = CartResponse.fromEntity(cartConverter.toEntity(cartDTO));
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "Lấy giỏ hàng thành công",
                "data", response
        ));
    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "status", "failed",
                "message", "Đã xảy ra lỗi khi lấy giỏ hàng"
        ));
    }
}
}
