package com.example.cdwebbackend.dto;

public class ProductSizeColorDTO extends AbstractDTO<ProductSizeColorDTO>{
    private Long productId;
    private Long sizeCode;
    private Long colorCode;
    private int stock; // Số lượng sản phẩm có size & color cụ thể

    public ProductSizeColorDTO() {
    }

    public ProductSizeColorDTO(Long productId, Long sizeCode, Long colorCode, int stock) {
        this.productId = productId;
        this.sizeCode = sizeCode;
        this.colorCode = colorCode;
        this.stock = stock;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public long getSizeCode() {
        return sizeCode;
    }

    public void setSizeCode(Long sizeCode) {
        this.sizeCode = sizeCode;
    }

    public Long getColorCode() {
        return colorCode;
    }

    public void setColorCode(Long colorCode) {
        this.colorCode = colorCode;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
}
