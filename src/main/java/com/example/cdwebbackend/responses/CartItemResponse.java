package com.example.cdwebbackend.responses;

import com.example.cdwebbackend.entity.CartItemEntity;
import com.example.cdwebbackend.entity.ProductColorEntity;
import com.example.cdwebbackend.entity.ProductEntity;
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
        ProductEntity product = entity.getProduct();

        // Tìm màu đã chọn trong danh sách productColors
        ProductColorEntity selectedColor = product.getProductColors().stream()
                .filter(pc -> pc.getColor().getName().equals(entity.getProductSizeColor().getProductColor().getColor().getName()))
                .findFirst()
                .orElse(null);

        // Lấy ảnh của màu đã chọn, nếu không có thì lấy ảnh mặc định
        String selectedImage = selectedColor != null ? selectedColor.getImage() : product.getProductColors().get(0).getImage(); // fallback ảnh đầu tiên nếu không có màu

        return CartItemResponse.builder()
                .id(entity.getId())
                .productId(entity.getProduct().getId())
                .productName(entity.getProduct().getNameProduct())
                .productImage(selectedImage)
                .price(entity.getProduct().getPrice())
                .quantity(entity.getQuantity())
                .size(entity.getProductSizeColor() != null ? entity.getProductSizeColor().getSize().getSize() : null)
                .color(entity.getProductSizeColor() != null ? entity.getProductSizeColor().getProductColor().getColor().getName() : null)
                .build();
    }

}
