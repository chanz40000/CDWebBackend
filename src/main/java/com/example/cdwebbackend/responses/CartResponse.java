package com.example.cdwebbackend.responses;

import com.example.cdwebbackend.entity.CartEntity;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class CartResponse {
    private Long id;
    private Long userId;
    private List<CartItemResponse> items;
    private int totalQuantity;
    private int totalPrice;
    private int totalQuantityProduct;
    public static CartResponse fromEntity(CartEntity entity){
        List<CartItemResponse> items = entity.getCartItems().stream()
                .map(CartItemResponse::fromEntity)
                .collect(Collectors.toList());

        int totalQuantity = items.stream().mapToInt(CartItemResponse::getQuantity).sum();
        int totalPrice = items.stream().mapToInt(item -> item.getPrice() * item.getQuantity()).sum();
        int totalQuantityProduct = items.size();
        return CartResponse.builder()
                .id(entity.getId())
                .userId(entity.getUser().getId())
                .items(items)
                .totalQuantity(totalQuantity)
                .totalPrice(totalPrice)
                .totalQuantityProduct(totalQuantityProduct)
                .build();
    }

}
