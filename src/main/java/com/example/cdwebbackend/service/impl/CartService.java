package com.example.cdwebbackend.service.impl;

import com.example.cdwebbackend.converter.CartConverter;
import com.example.cdwebbackend.converter.CartItemConverter;
import com.example.cdwebbackend.dto.CartDTO;
import com.example.cdwebbackend.dto.CartItemDTO;
import com.example.cdwebbackend.entity.CartEntity;
import com.example.cdwebbackend.entity.CartItemEntity;
import com.example.cdwebbackend.entity.ProductSizeColorEntity;
import com.example.cdwebbackend.entity.UserEntity;
import com.example.cdwebbackend.repository.*;
import com.example.cdwebbackend.service.ICartService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartService implements ICartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductSizeColorRepository productSizeColorRepository;

    @Autowired
    private CartConverter cartConverter;

    @Autowired
    private CartItemConverter cartItemConverter;

    @Override
    @Transactional
    public CartItemDTO addToCart(Long userId, Long productId, Long productSizeColorId, int quantity) {

        // lay user
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User Not Found"));
        // lay gio hang
        CartEntity cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    CartEntity cartEntity = new CartEntity();
                    cartEntity.setUser(user);
                    return  cartRepository.save(cartEntity);
                });

        // Lấy thông tin productSizeColor
        ProductSizeColorEntity productSizeColor = productSizeColorRepository.findOneById(productSizeColorId);
        if (productSizeColor == null) {
            throw new RuntimeException("ProductSizeColor not found");
        }

        // kiem tra xem productSizeColor có đúng productId không
        if (!productSizeColor.getProduct().getId().equals(productId)) {
            throw new RuntimeException("Product and ProductSizeColor do not match");
        }


        // Kiểm tra xem item này (cartId + productSizeColorId) đã tồn tại chưa
        Optional<CartItemEntity> optionalCartItem = cartItemRepository.findByCartIdAndProductSizeColorId(cart.getId(), productSizeColorId);


        CartItemEntity cartItem;
        if (optionalCartItem.isPresent()) {
            //ton tai thi cong so luong
            cartItem = optionalCartItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        } else {
            cartItem = new CartItemEntity();
            cartItem.setCart(cart);
            cartItem.setProduct(productSizeColor.getProduct());
            cartItem.setProductSizeColor(productSizeColor);
            cartItem.setQuantity(quantity);
        }

        cartItem = cartItemRepository.save(cartItem);
        return cartItemConverter.toDTO(cartItem);
    }

    // Cập nhật số lượng
    @Override
    public CartItemDTO updateQuantity(Long userId, Long cartItemId, int quantity) {

        CartItemEntity cartItem = cartItemRepository.findOneById(cartItemId);

        if (cartItem != null && quantity > 0) {
            cartItem.setQuantity(quantity);
            CartItemEntity updatedItem = cartItemRepository.save(cartItem);
            return cartItemConverter.toDTO(updatedItem);
        }
        throw new RuntimeException("Cart item not found or invalid quantity");
    }

    //Xóa sản phẩm khỏi giỏ hàng
    @Override
    public void removeItemFromCart(Long userId, Long cartItemId) {
        CartItemEntity cartItem = cartItemRepository.findOneById(cartItemId);
        if (cartItem != null) {
            cartItemRepository.delete(cartItem);
        } else {
            throw new RuntimeException("Cart item not found");
        }
    }

    // Thay đổi màu sác hoặc kích thước của sản phẩm trong giỏ hàng
    @Override
    public CartItemDTO updateProductVariant(Long cartItemId, Long newProductSizeColorId) {
        CartItemEntity cartItem = cartItemRepository.findOneById(cartItemId);
        if (cartItem == null){
            throw new RuntimeException("Cart item not found");
        }
        ProductSizeColorEntity newProductSizeColor = productSizeColorRepository.findOneById(newProductSizeColorId);
        if (newProductSizeColor == null) {
            throw new RuntimeException("ProductSizeColor not found");
        }

        // Kiểm tra productId của cartItem có khớp với productId của biến thể mới
        Long productIdInCart = cartItem.getProduct().getId();
        Long productIdInVariant = newProductSizeColor.getProduct().getId();
        if (!productIdInCart.equals(productIdInVariant)) {
            throw new RuntimeException("Product and ProductSizeColor do not match");
        }


        // Cập nhật biến thể size/color mới
        cartItem.setProductSizeColor(newProductSizeColor);
        CartItemEntity updatedItem = cartItemRepository.save(cartItem);
        return cartItemConverter.toDTO(updatedItem);
    }

    //Lấy giỏ hàng theo userId
    @Override
    public CartDTO getCartByUserId(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        CartEntity cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    CartEntity cartEntity = new CartEntity();
                    cartEntity.setUser(user);
                    return cartRepository.save(cartEntity);
                });

        return cartConverter.toDTO(cart);
    }




}
