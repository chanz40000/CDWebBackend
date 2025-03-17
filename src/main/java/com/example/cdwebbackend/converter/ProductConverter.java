package com.example.cdwebbackend.converter;

import com.example.cdwebbackend.dto.ProductDTO;
import com.example.cdwebbackend.dto.ProductSizeColorDTO;
import com.example.cdwebbackend.entity.ColorEntity;
import com.example.cdwebbackend.entity.ProductEntity;
import com.example.cdwebbackend.entity.ProductSizeColorEntity;
import com.example.cdwebbackend.entity.SizeEntity;
import com.example.cdwebbackend.repository.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProductConverter {
    CategoryRepository categoryRepository;
    BrandRepository brandRepository;
    ProductRepository productRepository;
    SizeRepository sizeRepository;
    ColorRepository colorRepository;
    public ProductEntity toEntity(ProductDTO dto) {
        ProductEntity entity = new ProductEntity();
        entity.setNameProduct(dto.getNameProduct());
        entity.setDescription(dto.getDescription());
        entity.setStock(dto.getStock());
        entity.setPrice(dto.getPrice());

        // Lấy CategoryEntity từ DB thay vì tạo mới
        if (dto.getCategoryCode() != null) {
            entity.setCategory(categoryRepository.findOneById(dto.getId()));
        }

        // Lấy BrandEntity từ DB thay vì tạo mới
        if (dto.getBrandCode() != null) {
            entity.setBrand(brandRepository.findOneById(dto.getId()));
        }


        List<ProductSizeColorDTO>list = dto.getProductSizeColorDTOS();
        List<ProductSizeColorEntity>listResult = new ArrayList<>();
        for (int i=0; i< list.size(); i++){
            ProductSizeColorDTO productSizeColorDTO = list.get(i);
            ProductEntity product = productRepository.findOneById(productSizeColorDTO.getProductId());
            SizeEntity size = sizeRepository.findOneById(productSizeColorDTO.getSizeCode());
            ColorEntity color = colorRepository.findOneById(productSizeColorDTO.getColorCode());
            int stock = productSizeColorDTO.getStock();

            ProductSizeColorEntity productSizeColorEntity = new ProductSizeColorEntity(product, size, color, stock);
            listResult.add(productSizeColorEntity);
        }
        entity.setProductSizeColors(listResult);

        return entity;
    }
}
