package com.example.cdwebbackend.converter;

import com.example.cdwebbackend.dto.ProductDTO;
import com.example.cdwebbackend.dto.ProductSizeColorDTO;
import com.example.cdwebbackend.entity.*;
import com.example.cdwebbackend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProductConverter {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private SizeRepository sizeRepository;
    @Autowired
    private ColorRepository colorRepository;

    public ProductEntity toEntity(ProductDTO dto) {
        ProductEntity entity = new ProductEntity();
        entity.setId(dto.getId());
        entity.setNameProduct(dto.getNameProduct());
        entity.setDescription(dto.getDescription());
        entity.setStock(dto.getStock());
        entity.setPrice(dto.getPrice());
        entity.setImage(dto.getImageUrl());

        // Lấy CategoryEntity từ DB thay vì tạo mới
        if (dto.getCategoryCode() != null) {
            entity.setCategory(categoryRepository.findOneById(Long.parseLong(dto.getCategoryCode())));
        }

// Lấy BrandEntity từ DB thay vì tạo mới
        if (dto.getBrandCode() != null) {
            entity.setBrand(brandRepository.findOneById(Long.parseLong(dto.getBrandCode())));
        }


//
//        List<ProductSizeColorDTO>list = dto.getProductSizeColorDTOS();
//        List<ProductSizeColorEntity>listResult = new ArrayList<>();
//        for (int i=0; i< list.size(); i++){
//            ProductSizeColorDTO productSizeColorDTO = list.get(i);
//            ProductEntity product = productRepository.findOneById(productSizeColorDTO.getProductId());
//            SizeEntity size = sizeRepository.findOneById(productSizeColorDTO.getSizeCode());
//            ColorEntity color = colorRepository.findOneById(productSizeColorDTO.getColorCode());
//            int stock = productSizeColorDTO.getStock();
//
//            ProductSizeColorEntity productSizeColorEntity = new ProductSizeColorEntity(product, size, color, stock);
//            listResult.add(productSizeColorEntity);
//        }
//        entity.setProductSizeColors(listResult);
        // Xử lý danh sách size-color-stock
        List<ProductSizeColorDTO> dtoList = dto.getProductSizeColorDTOS();
        List<ProductSizeColorEntity> sizeColorEntities = new ArrayList<>();
        if (dtoList != null) {
            for (ProductSizeColorDTO item : dtoList) {
                SizeEntity size = sizeRepository.findOneById(item.getSizeCode());
                ColorEntity color = colorRepository.findOneById(item.getColorCode());

                ProductSizeColorEntity scEntity = new ProductSizeColorEntity();
                scEntity.setSize(size);
                scEntity.setColor(color);
                scEntity.setStock(item.getStock());
                scEntity.setProduct(entity); // thiết lập quan hệ hai chiều nếu cần

                sizeColorEntities.add(scEntity);
            }
            entity.setProductSizeColors(sizeColorEntities);
        }


        return entity;
    }
    public ProductDTO toDTO(ProductEntity entity) {
        ProductDTO dto = new ProductDTO();
        dto.setId(entity.getId());
        dto.setNameProduct(entity.getNameProduct());
        dto.setDescription(entity.getDescription());
        dto.setStock(entity.getStock());
        dto.setPrice(entity.getPrice());
        dto.setImageUrl(entity.getImage());

        // Gán category và brand
        if (entity.getCategory() != null) {
            dto.setCategoryCode(String.valueOf(entity.getCategory().getId()));
        }

        if (entity.getBrand() != null) {
            dto.setBrandCode(String.valueOf(entity.getBrand().getId()));
        }

        // Chuyển danh sách size - color - stock
        List<ProductSizeColorDTO> sizeColorDTOs = new ArrayList<>();
        if (entity.getProductSizeColors() != null) {
            for (ProductSizeColorEntity scEntity : entity.getProductSizeColors()) {
                ProductSizeColorDTO scDTO = new ProductSizeColorDTO();
                scDTO.setSizeCode(scEntity.getSize().getId());
                scDTO.setColorCode(scEntity.getColor().getId());
                scDTO.setStock(scEntity.getStock());

                sizeColorDTOs.add(scDTO);
            }
        }

        dto.setProductSizeColorDTOS(sizeColorDTOs);

        return dto;
    }

}
