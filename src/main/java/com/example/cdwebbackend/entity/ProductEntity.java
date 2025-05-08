package com.example.cdwebbackend.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product")
public class ProductEntity extends BaseEntity{
    @Column(name = "name_product", columnDefinition = "TEXT")
    private String nameProduct;
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    @ManyToOne
    @JoinColumn(name = "category_id") // Thêm khóa ngoại trong bảng Product
    private CategoryEntity category;
    @Column(name = "stock")
    private int stock;
    @Column(name = "price")
    private int price;

    @Column(name = "import_price", nullable = false)
    private int import_price = 0;
//
//    @Column(name = "image")
//    private String image;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private BrandEntity brand;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductColorEntity> productColors = new ArrayList<>();



    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductSizeColorEntity> productSizeColors;


//    public ProductEntity(String nameProduct, String description, CategoryEntity category, int stock, int price, int import_price, String image, BrandEntity brand, List<ProductSizeColorEntity> productSizeColors) {
//        this.nameProduct = nameProduct;
//        this.description = description;
//        this.category = category;
//        this.stock = stock;
//        this.price = price;
//        this.import_price = import_price;
////        this.image = image;
//        this.brand = brand;
//        this.productSizeColors = productSizeColors;
//    }


    public ProductEntity(String nameProduct, String description, CategoryEntity category, int stock, int price, int import_price, BrandEntity brand, List<ProductColorEntity> productColors, List<ProductSizeColorEntity> productSizeColors) {
        this.nameProduct = nameProduct;
        this.description = description;
        this.category = category;
        this.stock = stock;
        this.price = price;
        this.import_price = import_price;
        this.brand = brand;
        this.productColors = productColors;
        this.productSizeColors = productSizeColors;
    }

    public ProductEntity() {

    }

    public List<ProductColorEntity> getProductColors() {
        return productColors;
    }

    public void setProductColors(List<ProductColorEntity> productColors) {
        this.productColors = productColors;
    }

    //    public String getImage() {
//        return image;
//    }
//
//    public void setImage(String image) {
//        this.image = image;
//    }

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

    public int getImport_price() {
        return import_price;
    }

    public void setImport_price(int import_price) {
        this.import_price = import_price;
    }
}
