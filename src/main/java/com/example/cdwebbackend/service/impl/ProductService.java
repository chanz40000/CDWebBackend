package com.example.cdwebbackend.service.impl;

import com.example.cdwebbackend.converter.ColorConverter;
import com.example.cdwebbackend.converter.ProductConverter;

import com.example.cdwebbackend.converter.SizeConverter;
import com.example.cdwebbackend.dto.ProductDTO;
import com.example.cdwebbackend.dto.SizeDTO;
import com.example.cdwebbackend.dto.ColorDTO;
import com.example.cdwebbackend.dto.ProductColorDTO;
import com.example.cdwebbackend.dto.ProductDTO;
import com.example.cdwebbackend.dto.ProductSizeColorDTO;
import com.example.cdwebbackend.entity.*;
import com.example.cdwebbackend.exceptions.DataNotFoundException;
import com.example.cdwebbackend.repository.*;
import com.example.cdwebbackend.service.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DateTimeException;
import java.util.*;
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
    @Autowired
    ProductSizeColorRepository productSizeColorRepository;
    @Autowired
    ColorConverter colorConverter;
    @Autowired
    SizeConverter sizeConverter;

    @Autowired
    private ColorRepository colorRepository;

    @Autowired
    private SizeRepository sizeRepository;

    @Autowired
    private ProductColorRepository productColorRepository;


    @Override
    @Transactional
    public ProductEntity uploadImage(long productId,long colorId, String url)  throws DataNotFoundException {

        ProductEntity productEntity = productRepository.findById(productId)
                .orElseThrow(() -> new DataNotFoundException("Product not found"));
        if (url == null || url.trim().isEmpty()) {
            throw new IllegalArgumentException("Product url is empty");
        }
        ColorEntity colorEntity = colorRepository.findOneById(colorId);
        if (colorEntity == null) {
            throw new DataNotFoundException("Color not found");
        }

        ProductColorEntity productColorEntity = productColorRepository.findByColorIdAndProductId(colorId, productId);
        if (productColorEntity == null) {
            productColorEntity = new ProductColorEntity();
            productColorEntity.setProduct(productEntity);
            productColorEntity.setColor(colorEntity);
//            newProductColor.setImage(url);
        }

        productColorEntity.setImage(url);
        productColorRepository.save(productColorEntity);

        return productRepository.save(productEntity);
    }
    @Override
    public ProductSizeColorEntity addSizeByColor(Long productColorId, Long productId, Long sizeId, int stock) {
        // Kiểm tra xem productSizeColor đã tồn tại với productId, sizeId và productColorId chưa
        ProductSizeColorEntity existingEntity = productSizeColorRepository
                .findByProductIdAndSizeIdAndProductColorId(productId, sizeId, productColorId);

        if (existingEntity != null) {
            // Nếu đã tồn tại, trả về bản ghi đã có hoặc thông báo lỗi
            int totalStock = 0;
            List<ProductSizeColorEntity> productSizeColorEntities = productSizeColorRepository.findByProduct_Id(productId);
            for (ProductSizeColorEntity entity : productSizeColorEntities) {
                totalStock += entity.getStock();
            }

            existingEntity.setStock(existingEntity.getStock() + stock);
            if (totalStock + stock > existingEntity.getProduct().getStock()) {
                throw new IllegalArgumentException("Tổng số lượng sản phẩm vượt quá số lượng tồn kho của sản phẩm.");
            }
            productSizeColorRepository.save(existingEntity);
            return existingEntity; // hoặc throw new IllegalArgumentException("Product size color already exists");
        }

        // Kiểm tra tổng số stock hiện tại cho productId
        int totalStock = 0;
        List<ProductSizeColorEntity> productSizeColorEntities = productSizeColorRepository.findByProduct_Id(productId);
        for (ProductSizeColorEntity entity : productSizeColorEntities) {
            totalStock += entity.getStock();
        }

        // Lấy sản phẩm để kiểm tra stock tối đa
        ProductEntity product = productRepository.findOneById(productId);
        if (totalStock + stock > product.getStock()) {
            throw new IllegalArgumentException("Tổng số lượng sản phẩm vượt quá số lượng tồn kho của sản phẩm.");
        }

        // Tìm kiếm các đối tượng cần thiết
        ProductColorEntity productColorEntity = productColorRepository.findByIdAndProductId(productColorId, productId);
        SizeEntity sizeEntity = sizeRepository.findOneById(sizeId);

        // Tạo mới ProductSizeColorEntity
        ProductSizeColorEntity productSizeColorEntity = new ProductSizeColorEntity();
        productSizeColorEntity.setSize(sizeEntity);
        productSizeColorEntity.setProductColor(productColorEntity);
        productSizeColorEntity.setStock(stock);
        productSizeColorEntity.setProduct(product);

        // Lưu và trả về bản ghi mới
        return productSizeColorRepository.save(productSizeColorEntity);
    }

    @Override
    public ProductColorEntity addColorProduct(Long productId, Long colorId, String url) {
        ProductEntity product = productRepository.findOneById(productId);
        ColorEntity color = colorRepository.findOneById(colorId);
        // Kiểm tra nếu đã có bản ghi trùng productId và colorId
        ProductColorEntity existing = productColorRepository.findByProductIdAndColorId(productId, colorId);

        if (existing != null) {
            // Nếu tồn tại, cập nhật lại ảnh
            existing.setImage(url);
            return productColorRepository.save(existing);
        } else {
        ProductColorEntity productColorEntity = new ProductColorEntity();
        productColorEntity.setProduct(product);
        productColorEntity.setColor(color);
        productColorEntity.setImage(url);
        return productColorRepository.save(productColorEntity);
        }
    }

    @Override
    public void chooseInfomation(Long productId, Long colorId, String image, int stock, Long size) {
        ProductEntity product = productRepository.findOneById(productId);
//        if (product == null) throw new DataNotFoundException("Product not found with ID: " + productId);

        ColorEntity color = colorRepository.findOneById(colorId);
//        if (color == null) throw new DataNotFoundException("Color not found with ID: " + colorId);

        SizeEntity sizeEntity = sizeRepository.findOneById(size);
        // Tạo và lưu ProductColorEntity
        ProductColorEntity productColorEntity = new ProductColorEntity();
        productColorEntity.setProduct(product);
        productColorEntity.setColor(color);
        productColorEntity.setImage(image);
        ProductColorEntity savedColorEntity = productColorRepository.saveAndFlush(productColorEntity); // flush đảm bảo lưu

        // Tạo và lưu ProductSizeColorEntity, dùng lại đối tượng đã lưu
        ProductSizeColorEntity productSizeColorEntity = new ProductSizeColorEntity();
        productSizeColorEntity.setProduct(product);
        productSizeColorEntity.setProductColor(savedColorEntity);
        productSizeColorEntity.setStock(stock); // dùng stock thật
        productSizeColorEntity.setSize(sizeEntity);
        productSizeColorRepository.save(productSizeColorEntity);
    }


    @Override
    @Transactional
    public ProductEntity createProduct(ProductDTO productDTO) throws DataNotFoundException {

                CategoryEntity category = categoryRepository.findOneById(Long.parseLong(productDTO.getCategoryCode()));
        if (category == null) {
            throw new DataNotFoundException("Category not found with code: " + productDTO.getCategoryCode());
        }

        BrandEntity brandEntity = brandRepository.findOneById(Long.parseLong(productDTO.getBrandCode()));
        if (brandEntity == null) {
            throw new DataNotFoundException("Brand not found with code: " + productDTO.getBrandCode());
        }
        ProductEntity productEntity = productConverter.toEntity(productDTO);
                productEntity.setCategory(category);
        productEntity.setBrand(brandEntity);
        return productRepository.save(productEntity);
    }

    @Override
    @Transactional
    public ProductEntity updateProduct(ProductDTO productDTO, Long productId) throws DataNotFoundException {
        ProductEntity productEntity = productRepository.findOneById(productId);
        if (productEntity == null) {
            throw new DataNotFoundException("Product not found with id: " + productId);
        }

        CategoryEntity category = categoryRepository.findOneById(Long.parseLong(productDTO.getCategoryCode()));
        if (category == null) {
            throw new DataNotFoundException("Category not found with code: " + productDTO.getCategoryCode());
        }

        BrandEntity brandEntity = brandRepository.findOneById(Long.parseLong(productDTO.getBrandCode()));
        if (brandEntity == null) {
            throw new DataNotFoundException("Brand not found with code: " + productDTO.getBrandCode());
        }

        // Cập nhật từng trường
        productEntity.setNameProduct(productDTO.getNameProduct());
        productEntity.setDescription(productDTO.getDescription());
        productEntity.setStock(productDTO.getStock());
        productEntity.setPrice(productDTO.getPrice());
        productEntity.setCategory(category);
        productEntity.setBrand(brandEntity);

        // KHÔNG cập nhật hoặc clear các collection như productSizeColors nếu không cần thiết!

        return productRepository.save(productEntity);
    }



    @Override
    public List<ProductDTO> getAllProducts() {
        List<ProductEntity> productEntities = productRepository.findAll();
        return productEntities.stream()
                .map(productConverter::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ProductDTO getProductById(Long id) throws DataNotFoundException {
        ProductEntity productEntity = productRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy sản phẩm với id = " + id));
        return productConverter.toDTO(productEntity);
    }

    public List<ProductDTO> getProductByName(String productName) {
        List<ProductDTO>  products = new ArrayList<>();
        List<ProductEntity> productEntities= productRepository.findAllByNameProductContainingIgnoreCase(productName);
        for (ProductEntity p: productEntities){
             products.add(productConverter.toDTO(p));
        }
        return products;
    }
    //lay ra tat ca mau sac ma san pham co
    public List<ColorDTO>  getListColorByIdProduct(long idProduct){
         List<ColorDTO>colorDTOS = new ArrayList<>();

         List<ProductSizeColorEntity>productSizeColorEntities = productSizeColorRepository.findByProduct_Id(idProduct);
         for (ProductSizeColorEntity p: productSizeColorEntities){
             ColorEntity c = p.getProductColor().getColor();
             colorDTOS.add(colorConverter.toDTO(c));
         }
         return colorDTOS;
    }
    public List<SizeDTO>  getListSizeByIdProduct(long idProduct){
        List<SizeDTO>sizeDTOS = new ArrayList<>();

        List<ProductSizeColorEntity>productSizeColorEntities = productSizeColorRepository.findByProduct_Id(idProduct);
        for (ProductSizeColorEntity p: productSizeColorEntities){
            SizeEntity s = p.getSize();
            sizeDTOS.add(sizeConverter.toDTO(s));
        }
        return sizeDTOS;
    }

    public Long getProductSizeColorId(Long productId, Long colorId, Long sizeId) {
        ProductColorEntity productColorEntity = productColorRepository.findByColorIdAndProductId(colorId, productId);
        return productSizeColorRepository.findByProductIdAndSizeIdAndProductColorId(productId, sizeId, productColorEntity.getId()).getId();
    }
    @Override
    public ColorEntity getDefaultColor() {
        return null;
    }
    @Override
    public void deleteProduct(Long productId) throws DataNotFoundException {
        ProductEntity productEntity = productRepository.findOneById(productId);
        if (productEntity == null) {
            throw new DataNotFoundException("Không tìm thấy sản phẩm với ID: " + productId);
        }
        productRepository.delete(productEntity);
    }
    @Override
    public void deleteProductSizeColor(Long productSizeColorId) throws DataNotFoundException {
        ProductSizeColorEntity productSizeColorEntity = productSizeColorRepository.findOneById(productSizeColorId);
        if (productSizeColorEntity == null) {
            throw new DataNotFoundException("Không tìm thấy sản phẩm với ID: " + productSizeColorId);
        }
        productSizeColorRepository.delete(productSizeColorEntity);
    }

}
