package com.example.cdwebbackend.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product")
public class ProductEntity extends BaseEntity{
    @Column(name = "name_product")
    private String nameProduct;
    @Column(name = "description")
    private String description;
    @ManyToOne
    @JoinColumn(name = "category_id") // Thêm khóa ngoại trong bảng Product
    private CategoryEntity category;
    @Column(name = "stock")
    private int stock;
    @Column(name = "price")
    private int price;
    @ManyToOne
    @JoinColumn(name = "brand_id")
    private BrandEntity brand;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductSizeColorEntity> productSizeColors;


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

    public CategoryEntity getCategory() {
        return category;
    }

    public void setCategory(CategoryEntity category) {
        this.category = category;
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

    public BrandEntity getBrand() {
        return brand;
    }

    public void setBrand(BrandEntity brand) {
        this.brand = brand;
    }

    public List<ProductSizeColorEntity> getProductSizeColors() {
        return productSizeColors;
    }

    public void setProductSizeColors(List<ProductSizeColorEntity> productSizeColors) {
        this.productSizeColors = productSizeColors;
    }
}
