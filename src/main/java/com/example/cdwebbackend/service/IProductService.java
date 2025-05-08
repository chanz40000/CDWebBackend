package com.example.cdwebbackend.service;

import com.example.cdwebbackend.dto.ProductDTO;
import com.example.cdwebbackend.entity.ColorEntity;
import com.example.cdwebbackend.entity.ProductColorEntity;
import com.example.cdwebbackend.entity.ProductEntity;
import com.example.cdwebbackend.entity.ProductSizeColorEntity;
import com.example.cdwebbackend.exceptions.DataNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IProductService {
    public ProductEntity uploadImage(long productId, long colorId, String url) throws DataNotFoundException;
    public ProductEntity createProduct(ProductDTO productDTO)  throws DataNotFoundException ;

    public List<ProductDTO> getAllProducts();
    public ProductDTO getProductById(Long id) throws DataNotFoundException;
    public ColorEntity getDefaultColor();
    public void chooseInfomation(Long productId, Long colorId, String image, int stock, Long size);
    public ProductSizeColorEntity addSizeByColor(Long productColorId, Long productId, Long sizeId, int stock);
    public ProductColorEntity addColorProduct(Long productId, Long colorId, String url);
    public ProductEntity updateProduct(ProductDTO productDTO, Long productId) throws DataNotFoundException;
    public void deleteProduct(Long productId) throws DataNotFoundException;
    public void deleteProductSizeColor(Long productSizeColorId) throws DataNotFoundException;
}
