package com.example.cdwebbackend.converter;

import com.example.cdwebbackend.dto.OrderDetailDTO;
import com.example.cdwebbackend.dto.ProductSizeColorDTO;
import com.example.cdwebbackend.entity.OrderDetailEntity;
import com.example.cdwebbackend.entity.OrderEntity;
import com.example.cdwebbackend.entity.ProductSizeColorEntity;
import com.example.cdwebbackend.repository.ProductColorRepository;
import com.example.cdwebbackend.repository.ProductRepository;
import com.example.cdwebbackend.repository.ProductSizeColorRepository;
import com.example.cdwebbackend.repository.SizeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authorization.method.AuthorizeReturnObject;
import org.springframework.stereotype.Component;

@Component
public class ProductSizeColorConverter {

    @Autowired
    private ProductSizeColorRepository productSizeColorRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductColorRepository productColorRepository;

    @Autowired
    private SizeRepository sizeRepository;

    public ProductSizeColorEntity toEntity(ProductSizeColorDTO pscDTO) {
        ProductSizeColorEntity entity = new ProductSizeColorEntity();
        entity.setId(pscDTO.getId());
        entity.setStock(pscDTO.getStock());
        entity.setActive(pscDTO.isActive());

        if (pscDTO.getProductId() != null) {
            entity.setProduct(productRepository.findOneById(pscDTO.getProductId()));
        }

        if (pscDTO.getSizeId() != null){
            entity.setSize(sizeRepository.findOneById(pscDTO.getSizeId()));
        }

        if (pscDTO.getColorCode() != null){
            entity.setProductColor(productColorRepository.findOneById(pscDTO.getColorId()));
        }
        return entity;
    }

    public ProductSizeColorDTO toDTO(ProductSizeColorEntity psc) {
        if (psc == null) {
            return null;
        }

        ProductSizeColorDTO dto = new ProductSizeColorDTO();
        dto.setId(psc.getId()); // Kế thừa từ AbstractDTO
//        dto.setProductId(psc.getProduct() != null ? psc.getProduct().getId() : null);
//        dto.setSizeCode(psc.getSize() != null ? psc.getSize().getId() : null);
//        dto.setColorCode(psc.getProductColor() != null ? psc.getProductColor().getId() : null);
        dto.setStock(psc.getStock());
        dto.setActive(psc.isActive());


        if (psc.getProduct() != null) {
            dto.setProductId(psc.getProduct().getId());
        }

        if (psc.getSize() != null){
            dto.setSizeId(psc.getSize().getId());
        }

        if (psc.getProductColor() != null){
            dto.setColorId(psc.getProductColor().getId());
        }

        return dto;
    }
}
