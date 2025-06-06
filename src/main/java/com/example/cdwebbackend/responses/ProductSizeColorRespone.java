package com.example.cdwebbackend.responses;

import com.example.cdwebbackend.entity.ColorEntity;
import com.example.cdwebbackend.entity.ProductEntity;
import com.example.cdwebbackend.entity.ProductSizeColorEntity;
import com.example.cdwebbackend.entity.SizeEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.ALWAYS)
public class ProductSizeColorRespone {
    private Long id;
    private String size;
    private String color;
    private String image;
    private int stock;
    private Boolean isActiveSize;

    public static ProductSizeColorRespone fromEntity(ProductSizeColorEntity entity) {
        return ProductSizeColorRespone.builder()
                .id(entity.getId())
                .size(entity.getSize().getSize())
                .color(entity.getProductColor().getColor().getName())
                .stock(entity.getStock())
                .image(entity.getProductColor().getImage())
                .isActiveSize(entity.isActive())
                .build();
    }
}
