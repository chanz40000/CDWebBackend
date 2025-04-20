package com.example.cdwebbackend.service.impl;

import com.example.cdwebbackend.entity.ProductEntity;
import com.example.cdwebbackend.exceptions.DataNotFoundException;
import com.example.cdwebbackend.repository.ProductRepository;
import com.example.cdwebbackend.service.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DateTimeException;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {

    @Autowired
    private ProductRepository productRepository;

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
}
