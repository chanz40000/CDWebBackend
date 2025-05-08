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
//    private List<P>
    public static ProductResponse fromEntity(ProductEntity product) {
        String image = null;
        if (product.getProductColors() != null && !product.getProductColors().isEmpty()) {
            image = product.getProductColors().get(0).getImage();
        }
        return ProductResponse.builder()
                .id(product.getId())
                .nameProduct(product.getNameProduct())
                .description(product.getDescription())
                .stock(product.getStock())
                .price(product.getPrice())
                .image(image)
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


//package com.example.cdwebbackend.responses;
//
//import com.example.cdwebbackend.entity.BrandEntity;
//import com.example.cdwebbackend.entity.CategoryEntity;
//import com.example.cdwebbackend.entity.ProductEntity;
//import com.example.cdwebbackend.entity.ProductSizeColorEntity;
//import jakarta.persistence.Column;
//import jakarta.persistence.JoinColumn;
//import jakarta.persistence.ManyToOne;
//import lombok.Builder;
//import lombok.Getter;
//
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//@Getter
//@Builder
//public class ProductResponse {
//    private Long id;
//    private String nameProduct;
//    private String description;
//    private int stock;
//    private int price;
//    private int import_price;
//    private String image;
//    private String categoryName;
//    private String brandName;
//    private List<ColorWithSizes> colors;
//
//    public static ProductResponse fromEntity(ProductEntity product) {
//        String image = null;
//        if (product.getProductColors() != null && !product.getProductColors().isEmpty()) {
//            image = product.getProductColors().get(0).getImage();
//        }
//
//        // Group theo ProductColor (tức là theo màu)
//        Map<Long, List<ProductSizeColorEntity>> grouped = product.getProductSizeColors().stream()
//                .collect(Collectors.groupingBy(
//                        p -> p.getProductColor().getId()
//                ));
//
//        // Chuyển mỗi nhóm thành 1 ColorWithSizes
//        List<ColorWithSizes> colorResponses = grouped.entrySet().stream().map(entry -> {
//            Long colorId = entry.getKey();
//            List<ProductSizeColorEntity> sizeColorList = entry.getValue();
//
//            String colorImage = sizeColorList.get(0).getProductColor().getImage(); // ảnh theo màu
//
//            List<SizeStock> sizes = sizeColorList.stream().map(sce ->
//                    SizeStock.builder()
//                            .sizeId(sce.getSize().getId())
//                            .stock(sce.getStock())
//                            .build()
//            ).collect(Collectors.toList());
//
//            return ColorWithSizes.builder()
//                    .colorId(colorId)
//                    .image(colorImage)
//                    .sizes(sizes)
//                    .build();
//        }).collect(Collectors.toList());
//
//        return ProductResponse.builder()
//                .id(product.getId())
//                .nameProduct(product.getNameProduct())
//                .description(product.getDescription())
//                .stock(product.getStock())
//                .price(product.getPrice())
//                .import_price(product.getImport_price())
//                .image(image)
//                .categoryName(product.getCategory() != null ? product.getCategory().getName() : null)
//                .brandName(product.getBrand() != null ? product.getBrand().getName() : null)
//                .colors(colorResponses)
//                .build();
//    }
//
//    // ====== INNER CLASSES ============
//    @Getter
//    @Builder
//    public static class ColorWithSizes {
//        private Long colorId;
//        private String image;
//        private List<SizeStock> sizes;
//    }
//
//    @Getter
//    @Builder
//    public static class SizeStock {
//        private Long sizeId;
//        private int stock;
//    }
//}
