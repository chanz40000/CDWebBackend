package com.example.cdwebbackend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "product_size_color", indexes = {
        @Index(name = "idx_psc_product_id", columnList = "product_id"),
        @Index(name = "idx_psc_size_id", columnList = "size_id"),
        @Index(name = "idx_psc_product_color_id", columnList = "product_color_id"),
        @Index(name = "idx_psc_stock", columnList = "stock"),
        @Index(name = "idx_psc_composite", columnList = "product_id,size_id,product_color_id", unique = true)
})
public class ProductSizeColorEntity extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    @ManyToOne
    @JoinColumn(name = "size_id")
    private SizeEntity size;

    @ManyToOne
    @JoinColumn(name = "product_color_id")
    private ProductColorEntity productColor;

    @Column(name = "stock")
    private Integer stock;

    public ProductSizeColorEntity() {
    }

    public ProductSizeColorEntity(ProductEntity product, SizeEntity size, ProductColorEntity productColor, int stock) {
        this.product = product;
        this.size = size;
        this.productColor = productColor;
        this.stock = stock;
    }

    public ProductColorEntity getProductColor() {
        return productColor;
    }

    public void setProductColor(ProductColorEntity productColor) {
        this.productColor = productColor;
    }

    public ProductEntity getProduct() {
        return product;
    }

    public void setProduct(ProductEntity product) {
        this.product = product;
    }

    public SizeEntity getSize() {
        return size;
    }

    public void setSize(SizeEntity size) {
        this.size = size;
    }

    public int getStock() {
        if (stock == null) return 0;
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductSizeColorEntity)) return false;
        ProductSizeColorEntity that = (ProductSizeColorEntity) o;
        return this.getId() != null && this.getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}