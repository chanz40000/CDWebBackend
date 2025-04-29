package com.example.cdwebbackend.responses;

import com.example.cdwebbackend.entity.BrandEntity;
import com.example.cdwebbackend.entity.CategoryEntity;
import com.example.cdwebbackend.entity.ProductEntity;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class ProductResponse {
    private Long id;
    private String nameProduct;
    private String description;
    private int stock;
    private int price;
    private int import_price;
    private String image;
    private String categoryName;
    private String brandName;
    private List<ProductSizeColorRespone> sizeColorVariants;

    public static ProductResponse fromEntity(ProductEntity product) {
        return ProductResponse.builder()
                .id(product.getId())
                .nameProduct(product.getNameProduct())
                .description(product.getDescription())
                .stock(product.getStock())
                .price(product.getPrice())
                .image(product.getImage())
                .categoryName(product.getCategory() != null ? product.getCategory().getName() : null)
                .brandName(product.getBrand() != null ? product.getBrand().getName() : null)
                .sizeColorVariants(product.getProductSizeColors() != null
                        ? product.getProductSizeColors().stream()
                        .map(ProductSizeColorRespone::fromEntity)
                        .collect(Collectors.toList())
                        : null)
                .build();
    }
}
