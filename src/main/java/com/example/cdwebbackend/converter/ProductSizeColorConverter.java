package com.example.cdwebbackend.converter;

import com.example.cdwebbackend.dto.ProductSizeColorDTO;
import com.example.cdwebbackend.entity.ProductSizeColorEntity;
import org.springframework.stereotype.Component;

@Component
public class ProductSizeColorConverter {
    public ProductSizeColorDTO toDTO(ProductSizeColorEntity psc) {
        if (psc == null) {
            return null;
        }

        ProductSizeColorDTO dto = new ProductSizeColorDTO();
        dto.setId(psc.getId()); // Kế thừa từ AbstractDTO
        dto.setProductId(psc.getProduct() != null ? psc.getProduct().getId() : null);
        dto.setSizeCode(psc.getSize() != null ? psc.getSize().getId() : null);
        dto.setColorCode(psc.getProductColor() != null ? psc.getProductColor().getId() : null);
        dto.setStock(psc.getStock());

        return dto;
    }
}
