package com.example.cdwebbackend.converter;

import com.example.cdwebbackend.dto.ImportOrderDTO;
import com.example.cdwebbackend.dto.ImportOrderProductDTO;
import com.example.cdwebbackend.dto.ProductDTO;
import com.example.cdwebbackend.dto.UserDTO;
import com.example.cdwebbackend.entity.*;
import com.example.cdwebbackend.repository.ProductRepository;
import com.example.cdwebbackend.repository.ProductSizeColorRepository;
import com.example.cdwebbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class ImportOrderConverter {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductSizeColorRepository productSizeColorRepository;

    // Convert từ ImportOrderDTO sang ImportOrderEntity
    public ImportOrderEntity toEntity(ImportOrderDTO dto) {
        ImportOrderEntity entity = new ImportOrderEntity();

        // Lấy UserEntity từ DB thay vì tạo mới
        if (dto.getUsername() != null) {
            System.out.println("username import la: "+ dto.getUsername());
            Optional<UserEntity> userEntity = userRepository.findOneByUsername(dto.getUsername());
            entity.setUser(userEntity.get());
        }else {
            System.out.println("khong co user");
        }

//        entity.setCreatedDate(dto.getCreatedDate());
//        entity.setModifiedDate(dto.getModifiedDate());
        // Xử lý danh sách sản phẩm và số lượng
        if (dto.getProducts() != null) {
            List<ImportOrderProductEntity> importOrderProductEntities = new ArrayList<>();
            for (ImportOrderProductDTO importOrderProductDTO : dto.getProducts()) {
                // Tìm kiếm sản phẩm từ DB
                //sa pham chi tiet
                ProductSizeColorEntity productEntity = productSizeColorRepository.findOneById(importOrderProductDTO.getProduct_size_colorId());

                // Tạo liên kết giữa ImportOrder và Product thông qua ImportOrderProductEntity
                ImportOrderProductEntity productEntityRel = new ImportOrderProductEntity(entity, productEntity, importOrderProductDTO.getQuantity(), importOrderProductDTO.getPrice());

                // Thêm vào danh sách các sản phẩm trong đơn hàng
                importOrderProductEntities.add(productEntityRel);
            }
            // Liên kết danh sách sản phẩm và số lượng với ImportOrderEntity
            entity.setImportOrderProducts(importOrderProductEntities);
        }else {
            System.out.println("khong co product");
        }

        // Thiết lập giá trị nhập hàng
        entity.setImportPrice(dto.getImportPrice());
        entity.setId(dto.getId());

        return entity;
    }

    // Convert từ ImportOrderEntity sang ImportOrderDTO
    public ImportOrderDTO toDTO(ImportOrderEntity entity) {
        ImportOrderDTO dto = new ImportOrderDTO();
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setModifiedDate(entity.getModifiedDate());
        dto.setId(entity.getId());
        dto.setImportPrice(entity.getImportPrice());

        // Chuyển đổi danh sách ImportOrderProductEntity sang ProductQuantityDTO
        if (entity.getImportOrderProducts() != null) {
            List<ImportOrderProductDTO> productDTOs = new ArrayList<>();
            for (ImportOrderProductEntity importOrderProductEntity : entity.getImportOrderProducts()) {
                // Tạo DTO cho từng sản phẩm và số lượng
                ImportOrderProductDTO productDTO = new ImportOrderProductDTO();
                productDTO.setProductId(importOrderProductEntity.getProduct().getId());
                productDTO.setQuantity(importOrderProductEntity.getQuantity());
                productDTOs.add(productDTO);
            }
            // Thêm vào danh sách sản phẩm trong DTO
            dto.setProducts(productDTOs);
        }
        dto.setUser(entity.getUser().getUsername());
        return dto;
    }
}