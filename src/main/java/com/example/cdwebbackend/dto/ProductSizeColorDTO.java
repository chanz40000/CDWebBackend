package com.example.cdwebbackend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProductSizeColorDTO extends AbstractDTO<ProductSizeColorDTO>{
    @JsonProperty("productId")
    private Long productId;
    @JsonProperty("sizeId")
    private Long sizeId;
    @JsonProperty("colorId")
    private Long colorId;
    @JsonProperty("stock")
    private int stock; // Số lượng sản phẩm có size & color cụ thể

    public ProductSizeColorDTO() {
    }

    public ProductSizeColorDTO(Long productId, Long sizeId, Long colorId, int stock) {
        this.productId = productId;
        this.sizeId = sizeId;
        this.colorId = colorId;
        this.stock = stock;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public long getSizeCode() {
        return sizeId;
    }

    public void setSizeCode(Long sizeId) {
        this.sizeId = sizeId;
    }

    public Long getColorCode() {
        return colorId;
    }

    public void setColorCode(Long colorId) {
        this.colorId = colorId;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
}
