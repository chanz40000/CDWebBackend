package com.example.cdwebbackend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "product_size_color")
public class ProductSizeColorEntity extends BaseEntity{

    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    @ManyToOne
    @JoinColumn(name = "size_id")
    private SizeEntity size;
//
//    @ManyToOne
//    @JoinColumn(name = "color_id")
//    private ColorEntity color;

    @ManyToOne
    @JoinColumn(name = "product_color_id")
    private ProductColorEntity productColor;


    @Column(name = "stock")
    private int stock;



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

    // Getters, Setters

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

//    public ColorEntity getColor() {
//        return color;
//    }
//
//    public void setColor(ColorEntity color) {
//        this.color = color;
//    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

//    public ProductSizeColorEntity(ProductEntity product, SizeEntity size, ColorEntity color, int stock) {
//        this.product = product;
//        this.size = size;
//        this.color = color;
//        this.stock = stock;
//    }
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
