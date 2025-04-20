package com.example.cdwebbackend.service;

import com.example.cdwebbackend.entity.ProductEntity;
import com.example.cdwebbackend.exceptions.DataNotFoundException;
import org.springframework.web.multipart.MultipartFile;

public interface IProductService {
    public ProductEntity uploadImage(long productId, String url) throws DataNotFoundException;
}
