package com.example.cdwebbackend.service;

import com.example.cdwebbackend.dto.ProductDTO;
import com.example.cdwebbackend.entity.ProductEntity;
import com.example.cdwebbackend.exceptions.DataNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IProductService {
    public ProductEntity uploadImage(long productId, String url) throws DataNotFoundException;
    public ProductEntity createProduct(ProductDTO productDTO)  throws DataNotFoundException ;

    public List<ProductDTO> getAllProducts();
    public ProductDTO getProductById(Long id) throws DataNotFoundException;
}
