package com.example.cdwebbackend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "product_color")
public class ProductColorEntity extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    @ManyToOne
    @JoinColumn(name = "color_id")
    private ColorEntity color;

    @Column(name = "image", columnDefinition = "TEXT")
    private String image;

    public ProductColorEntity(ProductEntity product, ColorEntity color, String image) {
        this.product = product;
        this.color = color;
        this.image = image;
    }

    public ProductColorEntity() {
    }

    public ProductEntity getProduct() {
        return product;
    }

    public void setProduct(ProductEntity product) {
        this.product = product;
    }

    public ColorEntity getColor() {
        return color;
    }

    public void setColor(ColorEntity color) {
        this.color = color;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
    @Override
    public String toString() {
        return "ProductColorEntity{" +
//                "id=" + id +
                ", color=" + (color != null ? color.getName() : "null") +
                ", image='" + image + '\'' +
                ", productId=" + (product != null ? product.getId() : "null") +
                '}';
    }

}
