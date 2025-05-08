package com.example.cdwebbackend.responses;

import com.example.cdwebbackend.entity.CartItemEntity;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CartItemResponse {
    private Long id;
    private Long productId;
    private String productName;
    private String productImage;
    private int price;
    private int quantity;
    private String size;
    private String color;

    public static CartItemResponse fromEntity(CartItemEntity entity){
        return CartItemResponse.builder()
                .id(entity.getId())
                .productId(entity.getProduct().getId())
                .productName(entity.getProduct().getNameProduct())
                .productImage(entity.getProduct().getProductColors().get(0).getImage())
                .price(entity.getProduct().getPrice())
                .quantity(entity.getQuantity())
                .size(entity.getProductSizeColor() != null ? entity.getProductSizeColor().getSize().getSize() : null)
                .color(entity.getProductSizeColor() != null ? entity.getProductSizeColor().getProductColor().getColor().getName() : null)
                .build();
    }

}
