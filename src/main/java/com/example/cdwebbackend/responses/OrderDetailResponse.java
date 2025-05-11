package com.example.cdwebbackend.responses;

import com.example.cdwebbackend.entity.OrderDetailEntity;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderDetailResponse {
    private Long id;
    private Long orderId;
    private Long productId;
    private int quantity;
    private String size;
    private String color;
    private int priceUnit;
    private int subtotal;
    private String image;
    private String nameProduct;

    public static OrderDetailResponse fromEntity(OrderDetailEntity orderDetailEntity) {
        return OrderDetailResponse.builder()
                .id(orderDetailEntity.getId())
                .orderId(orderDetailEntity.getOrder().getId())
                .image(orderDetailEntity.getProductSizeColor().getProductColor().getImage())
                .nameProduct(orderDetailEntity.getProduct().getNameProduct())
                .productId(orderDetailEntity.getProduct().getId())
                .quantity(orderDetailEntity.getQuantity())
                .size(orderDetailEntity.getProductSizeColor() != null ? orderDetailEntity.getProductSizeColor().getSize().getSize() : null)
                .color(orderDetailEntity.getProductSizeColor() != null ? orderDetailEntity.getProductSizeColor().getProductColor().getColor().getName() : null)
                .priceUnit(orderDetailEntity.getPriceUnit())
                .subtotal(orderDetailEntity.getSubtotal())
                .build();
    }

}
