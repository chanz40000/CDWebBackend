package com.example.cdwebbackend.service;

import com.example.cdwebbackend.dto.CartDTO;
import com.example.cdwebbackend.dto.CartItemDTO;
import com.example.cdwebbackend.entity.CartItemEntity;

public interface ICartService {
    public CartItemDTO addToCart(Long userId, Long productId, Long productSizeColorId, int quantity);
    public CartItemDTO updateQuantity(Long userId, Long cartItemId, int quantity);
    public void removeItemFromCart(Long userId, Long cartItemId);
    public CartItemDTO updateProductVariant(Long cartItemId, Long newProductSizeColorId);
    public CartDTO getCartByUserId(Long userId);
}
