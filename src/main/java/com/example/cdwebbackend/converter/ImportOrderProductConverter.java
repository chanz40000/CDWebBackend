//package com.example.cdwebbackend.converter;
//
//import com.example.cdwebbackend.dto.ImportOrderDTO;
//import com.example.cdwebbackend.dto.ImportOrderProductDTO;
//import com.example.cdwebbackend.entity.ImportOrderEntity;
//import com.example.cdwebbackend.entity.ImportOrderProductEntity;
//import com.example.cdwebbackend.entity.ProductSizeColorEntity;
//import com.example.cdwebbackend.entity.UserEntity;
//import com.example.cdwebbackend.service.impl.ImportOrderService;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//public class ImportOrderProductConverter {
//    ImportOrderService importOrderService;
//    public ImportOrderProductEntity toEntity(ImportOrderProductDTO dto) {
//        ImportOrderProductEntity entity = new ImportOrderProductEntity();
//        entity.setId(dto.getId());
//        entity.setCreatedBy(dto.getCreatedBy());
//        entity.setModifiedBy(dto.getModifiedBy());
//        enti
//        entity
//        // Thiết lập giá trị nhập hàng
//        entity.setImportPrice(dto.getImportPrice());
//        entity.setId(dto.getId());
//
//        return entity;
//    }
//
//    // Convert từ ImportOrderEntity sang ImportOrderDTO
//    public ImportOrderDTO toDTO(ImportOrderProductEntity entity) {
//        ImportOrderDTO dto = new ImportOrderDTO();
//        dto.setCreatedDate(entity.getCreatedDate());
//        dto.setModifiedDate(entity.getModifiedDate());
//        dto.setId(entity.getId());
//        dto.setImportPrice(entity.getImportPrice());
//
//        // Chuyển đổi danh sách ImportOrderProductEntity sang ProductQuantityDTO
//        if (entity.getImportOrderProducts() != null) {
//            List<ImportOrderProductDTO> productDTOs = new ArrayList<>();
//            for (ImportOrderProductEntity importOrderProductEntity : entity.getImportOrderProducts()) {
//                // Tạo DTO cho từng sản phẩm và số lượng
//                ImportOrderProductDTO productDTO = new ImportOrderProductDTO();
//                productDTO.setProductId(importOrderProductEntity.getProduct().getId());
//                productDTO.setQuantity(importOrderProductEntity.getQuantity());
//                productDTOs.add(productDTO);
//            }
//            // Thêm vào danh sách sản phẩm trong DTO
//            dto.setProducts(productDTOs);
//        }
//        dto.setUser(entity.getUser().getUsername());
//        return dto;
//    }
//}
