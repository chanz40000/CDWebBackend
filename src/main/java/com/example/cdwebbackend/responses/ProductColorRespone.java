package com.example.cdwebbackend.responses;


import com.example.cdwebbackend.entity.ProductColorEntity;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductColorRespone {
    private Long id;
    private String color;
    private String image;
    public static ProductColorRespone fromEntity(ProductColorEntity productColorEntity) {
        return ProductColorRespone.builder()
                .id(productColorEntity.getId())
                .color(productColorEntity.getColor().getName())
                .image(productColorEntity.getImage())
                .build();
    }
}
