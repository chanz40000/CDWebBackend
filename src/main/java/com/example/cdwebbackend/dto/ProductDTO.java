package com.example.cdwebbackend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

import java.util.List;

public class ProductDTO extends AbstractDTO<ProductDTO> {
    @NotBlank(message = "Product name is required")
    @JsonProperty("name_product")
    private String nameProduct;

    @NotBlank(message = "Description is required")
    @JsonProperty("description")
    private String description;

    @JsonProperty("stock")
    private int stock;

    @JsonProperty("price")
    private int price;

    @NotBlank(message = "Category ID is required")
    @JsonProperty("category_id")
    private String categoryCode;

    @NotBlank(message = "Brand ID is required")
    @JsonProperty("brand_id")
    private String brandCode;

    @JsonProperty("image")
    private String imageUrl;

    @JsonProperty("productSizeColorDTOS")
    private List<ProductSizeColorDTO> productSizeColorDTOS; // Chỉ lưu danh sách mã size thay vì đối tượng SizeEntity

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getNameProduct() {
        return nameProduct;
    }

    public void setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getBrandCode() {
        return brandCode;
    }

    public void setBrandCode(String brandCode) {
        this.brandCode = brandCode;
    }

    public List<ProductSizeColorDTO> getProductSizeColorDTOS() {
        return productSizeColorDTOS;
    }

    public void setProductSizeColorDTOS(List<ProductSizeColorDTO> sizeCodes) {
        this.productSizeColorDTOS = sizeCodes;
    }
}
