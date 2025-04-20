package com.example.cdwebbackend.service.impl;

import com.example.cdwebbackend.converter.ProductConverter;
import com.example.cdwebbackend.dto.ProductDTO;
import com.example.cdwebbackend.entity.BrandEntity;
import com.example.cdwebbackend.entity.CategoryEntity;
import com.example.cdwebbackend.entity.ProductEntity;
import com.example.cdwebbackend.exceptions.DataNotFoundException;
import com.example.cdwebbackend.repository.BrandRepository;
import com.example.cdwebbackend.repository.CategoryRepository;
import com.example.cdwebbackend.repository.ProductRepository;
import com.example.cdwebbackend.service.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DateTimeException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private ProductConverter productConverter;


    @Override
    @Transactional
    public ProductEntity uploadImage(long productId, String url)  throws DataNotFoundException {

        ProductEntity productEntity = productRepository.findById(productId)
                .orElseThrow(() -> new DataNotFoundException("Product not found"));
        if (url == null || url.trim().isEmpty()) {
            throw new IllegalArgumentException("Product url is empty");
        }
        productEntity.setImage(url);
        return productRepository.save(productEntity);
    }

    @Override
    public ProductEntity createProduct(ProductDTO productDTO)  throws DataNotFoundException  {
        // kiểm tra category co tồn tại không
//        CategoryEntity category = categoryRepository.findOneById(Long.parseLong(productDTO.getCategoryCode()))
        CategoryEntity category = categoryRepository.findOneById(Long.parseLong(productDTO.getCategoryCode()));
        if (category == null) {
            throw new DataNotFoundException("Category not found with code: " + productDTO.getCategoryCode());
        }
        BrandEntity brandEntity = brandRepository.findOneById(Long.parseLong(productDTO.getBrandCode()));
        if (brandEntity == null) {
            throw new DataNotFoundException("Brand not found with code: " + productDTO.getBrandCode());
        }

        //convert từ DTO sang entity
        ProductEntity productEntity = productConverter.toEntity(productDTO);

        // gán category và brand cho product
        productEntity.setCategory(category);
        productEntity.setBrand(brandEntity);

        return productRepository.save(productEntity);
    }

    @Override
    public List<ProductDTO> getAllProducts() {
        List<ProductEntity> productEntities = productRepository.findAll();
        return productEntities.stream()
                .map(productConverter::toDTO)
                .collect(Collectors.toList());
    }

}
