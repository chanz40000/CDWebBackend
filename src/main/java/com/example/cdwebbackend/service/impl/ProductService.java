package com.example.cdwebbackend.service.impl;

import com.example.cdwebbackend.converter.ColorConverter;
import com.example.cdwebbackend.converter.ProductConverter;
import com.example.cdwebbackend.converter.SizeConverter;
import com.example.cdwebbackend.dto.ColorDTO;
import com.example.cdwebbackend.dto.ProductDTO;
import com.example.cdwebbackend.dto.SizeDTO;
import com.example.cdwebbackend.entity.*;
import com.example.cdwebbackend.exceptions.DataNotFoundException;
import com.example.cdwebbackend.repository.*;
import com.example.cdwebbackend.service.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
    private ProductSizeColorRepository productSizeColorRepository;

    @Autowired
    private ColorConverter colorConverter;

    @Autowired
    private SizeConverter sizeConverter;

    @Autowired
    private ColorRepository colorRepository;

    @Autowired
    private SizeRepository sizeRepository;

    @Autowired
    private ProductColorRepository productColorRepository;

    @Override
    @Transactional
    @CachePut(value = "products", key = "#productId", condition = "#productId != null && #colorId != null && #url != null")
    public ProductEntity uploadImage(long productId, long colorId, String url) throws DataNotFoundException {
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
        }

        productColorEntity.setImage(url);
        productColorRepository.save(productColorEntity);

        return productRepository.save(productEntity);
    }

    @Override
    @Transactional
    @CachePut(value = "productDetails", key = "#productId + '-' + #sizeId + '-' + #productColorId", condition = "#productColorId != null && #productId != null && #sizeId != null")
    public ProductSizeColorEntity addSizeByColor(Long productColorId, Long productId, Long sizeId, int stock) {
        ProductSizeColorEntity existingEntity = productSizeColorRepository
                .findByProductIdAndSizeIdAndProductColorId(productId, sizeId, productColorId);

        if (existingEntity != null) {
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
            return existingEntity;
        }

        int totalStock = 0;
        List<ProductSizeColorEntity> productSizeColorEntities = productSizeColorRepository.findByProduct_Id(productId);
        for (ProductSizeColorEntity entity : productSizeColorEntities) {
            totalStock += entity.getStock();
        }

        ProductEntity product = productRepository.findOneById(productId);
        if (totalStock + stock > product.getStock()) {
            throw new IllegalArgumentException("Tổng số lượng sản phẩm vượt quá số lượng tồn kho của sản phẩm.");
        }

        ProductColorEntity productColorEntity = productColorRepository.findByIdAndProductId(productColorId, productId);
        SizeEntity sizeEntity = sizeRepository.findOneById(sizeId);

        ProductSizeColorEntity productSizeColorEntity = new ProductSizeColorEntity();
        productSizeColorEntity.setSize(sizeEntity);
        productSizeColorEntity.setProductColor(productColorEntity);
        productSizeColorEntity.setStock(stock);
        productSizeColorEntity.setProduct(product);
        productSizeColorEntity.setActive(true);

        return productSizeColorRepository.save(productSizeColorEntity);
    }

    @Override
    @Transactional
    @CachePut(value = "productDetails", key = "#productId + '-' + #colorId", condition = "#productId != null && #colorId != null && #url != null")
    public ProductColorEntity addColorProduct(Long productId, Long colorId, String url) {
        ProductEntity product = productRepository.findOneById(productId);
        ColorEntity color = colorRepository.findOneById(colorId);
        ProductColorEntity existing = productColorRepository.findByProductIdAndColorId(productId, colorId);

        if (existing != null) {
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
    @Transactional
    @CacheEvict(value = {"products", "productDetails"}, allEntries = true, condition = "#productId != null && #colorId != null && #image != null && #size != null")
    public void chooseInfomation(Long productId, Long colorId, String image, int stock, Long size) {
        ProductEntity product = productRepository.findOneById(productId);
        ColorEntity color = colorRepository.findOneById(colorId);
        SizeEntity sizeEntity = sizeRepository.findOneById(size);

        ProductColorEntity productColorEntity = new ProductColorEntity();
        productColorEntity.setProduct(product);
        productColorEntity.setColor(color);
        productColorEntity.setImage(image);
        ProductColorEntity savedColorEntity = productColorRepository.saveAndFlush(productColorEntity);

        ProductSizeColorEntity productSizeColorEntity = new ProductSizeColorEntity();
        productSizeColorEntity.setProduct(product);
        productSizeColorEntity.setProductColor(savedColorEntity);
        productSizeColorEntity.setStock(stock);
        productSizeColorEntity.setSize(sizeEntity);
        productSizeColorRepository.save(productSizeColorEntity);
    }

    @Override
    @Transactional
    @CachePut(value = "products", key = "#result.id", condition = "#productDTO != null")
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
                productEntity.setActive(true);

        productEntity.setBrand(brandEntity);
        return productRepository.save(productEntity);
    }

    @Override
    @Transactional
    @CachePut(value = "products", key = "#productId", condition = "#productDTO != null && #productId != null")
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

        productEntity.setNameProduct(productDTO.getNameProduct());
        productEntity.setDescription(productDTO.getDescription());
        productEntity.setStock(productDTO.getStock());
        productEntity.setPrice(productDTO.getPrice());
        productEntity.setCategory(category);
        productEntity.setBrand(brandEntity);

        return productRepository.save(productEntity);
    }

    @Override
    @Cacheable(value = "products", key = "'allProducts'")
    public List<ProductDTO> getAllProducts() {
        List<ProductEntity> productEntities = productRepository.findAll();
        return productEntities.stream()
                .map(productConverter::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "products", key = "#id", condition = "#id != null")
    public ProductDTO getProductById(Long id) throws DataNotFoundException {
        ProductEntity productEntity = productRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy sản phẩm với id = " + id));
        return productConverter.toDTO(productEntity);
    }

    public ProductDTO getProductByIdTrue(Long id) throws DataNotFoundException {
        ProductEntity productEntity = productRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new DataNotFoundException("Không tìm thấy sản phẩm với id = " + id));
        // Lọc productSizeColors chỉ lấy cái nào isActive = true
        List<ProductSizeColorEntity> activeSizeColors = productEntity.getProductSizeColors().stream()
                .filter(psc -> Boolean.TRUE.equals(psc.isActive()))
                .collect(Collectors.toList());

        productEntity.setProductSizeColors(activeSizeColors);
        return productConverter.toDTO(productEntity);
    }



    @Cacheable(value = "products", key = "#productName", condition = "#productName != null")
    public List<ProductDTO> getProductByName(String productName) {
        List<ProductDTO> products = new ArrayList<>();
        List<ProductEntity> productEntities = productRepository.findAllByNameProductContainingIgnoreCase(productName);
        for (ProductEntity p : productEntities) {
            products.add(productConverter.toDTO(p));
        }
        return products;
    }


    @Cacheable(value = "productDetails", key = "#idProduct + '-colors'", condition = "#idProduct != null")
    public List<ColorDTO> getListColorByIdProduct(long idProduct) {
        List<ColorDTO> colorDTOS = new ArrayList<>();
        Set<Long> uniqueColorIds = new HashSet<>();

        List<ProductSizeColorEntity> productSizeColorEntities = productSizeColorRepository.findByProduct_Id(idProduct);
        for (ProductSizeColorEntity p : productSizeColorEntities) {
            ColorEntity c = p.getProductColor().getColor();
            if (c != null && uniqueColorIds.add(c.getId())) {
                colorDTOS.add(colorConverter.toDTO(c));
            }
        }
        return colorDTOS;
    }


    @Cacheable(value = "productDetails", key = "#idProduct + '-sizes'", condition = "#idProduct != null")
    public List<SizeDTO> getListSizeByIdProduct(long idProduct) {
        List<SizeDTO> sizeDTOS = new ArrayList<>();
        Set<Long> uniqueSizeIds = new HashSet<>();

        List<ProductSizeColorEntity> productSizeColorEntities = productSizeColorRepository.findByProduct_Id(idProduct);
        for (ProductSizeColorEntity p : productSizeColorEntities) {
            SizeEntity s = p.getSize();
            if (s != null && uniqueSizeIds.add(s.getId())) {
                sizeDTOS.add(sizeConverter.toDTO(s));
            }
        }
        return sizeDTOS;
    }


    @Cacheable(value = "productDetails", key = "#productId + '-' + #colorId + '-' + #sizeId", condition = "#productId != null && #colorId != null && #sizeId != null")
    public Long getProductSizeColorId(Long productId, Long colorId, Long sizeId) {
        ProductColorEntity productColorEntity = productColorRepository.findByColorIdAndProductId(colorId, productId);
        return productSizeColorRepository.findByProductIdAndSizeIdAndProductColorId(productId, sizeId, productColorEntity.getId()).getId();
    }

    @Override
    public ColorEntity getDefaultColor() {
        return null;
    }
//    @CacheEvict(value = {"products", "productDetails"}, key = "#productId", condition = "#productId != null")
    @Override
    public void changeActiveProduct(Long productId, Boolean active) throws DataNotFoundException {

        ProductEntity productEntity = productRepository.findOneById(productId);
        if (productEntity == null) {
            throw new DataNotFoundException("Không tìm thấy sản phẩm với ID: " + productId);
        }
        productEntity.setActive(active);
        productRepository.save(productEntity);
    }
//    @CacheEvict(value = "productDetails", key = "#productSizeColorId", condition = "#productSizeColorId != null")
    @Override
    public void changeActiveProductSizeColor(Long productSizeColorId, Boolean active) throws DataNotFoundException {

        ProductSizeColorEntity productSizeColorEntity = productSizeColorRepository.findOneById(productSizeColorId);
        if (productSizeColorEntity == null) {
            throw new DataNotFoundException("Không tìm thấy sản phẩm với ID: " + productSizeColorId);
        }
        productSizeColorEntity.setActive(active);
        productSizeColorRepository.save(productSizeColorEntity);
    }


    @Cacheable(value = "products", key = "'page-' + #pageable?.pageNumber + '-' + #pageable?.pageSize", condition = "#pageable != null")
    public Page<ProductDTO> getAllProductsPaginated(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(product -> productConverter.toDTO(product));
    }
    public Page<ProductDTO> getAllProductsPaginated(Boolean isActive, Pageable pageable) {
        Page<ProductEntity> products;

        if (isActive == null) {
            // Nếu không truyền hoặc null thì lấy tất cả
            products = productRepository.findAll(pageable);
        } else {
            // Lọc theo isActive = true / false
            products = productRepository.findAllByIsActive(isActive, pageable);
        }

        return products.map(product -> productConverter.toDTO(product));
    }



    @Cacheable(value = "products", key = "'zeroStockLastThreeMonths'")
    public List<ProductSizeColorEntity> getProductsWithZeroStockPurchasedInLastThreeMonths() {
        LocalDateTime threeMonthsAgo = LocalDateTime.now().minusMonths(3);
        return productRepository.findProductSizeColorsWithZeroStockPurchasedInLastThreeMonths(threeMonthsAgo);
    }


    @Cacheable(value = "productDetails", key = "#id", condition = "#id != null")
    public String getImageByProductSizeColorId(Long id) {
        return productSizeColorRepository.getImage(id);
    }
}