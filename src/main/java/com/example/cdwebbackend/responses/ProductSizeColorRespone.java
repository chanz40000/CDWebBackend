package com.example.cdwebbackend.responses;

import com.example.cdwebbackend.entity.ColorEntity;
import com.example.cdwebbackend.entity.ProductEntity;
import com.example.cdwebbackend.entity.ProductSizeColorEntity;
import com.example.cdwebbackend.entity.SizeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductSizeColorRespone {
    private String size;
    private String color;
    private int stock;

    public static ProductSizeColorRespone fromEntity(ProductSizeColorEntity entity) {
        return ProductSizeColorRespone.builder()
                .size(entity.getSize().getSize())
                .color(entity.getColor().getName())
                .stock(entity.getStock())
                .build();
    }
}
