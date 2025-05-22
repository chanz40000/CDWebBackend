package com.example.cdwebbackend.entity;

import jakarta.persistence.*;
import org.springframework.data.annotation.Id;

@Entity
@Table(name = "import_order_product")
public class ImportOrderProductEntity extends BaseEntity{

    @ManyToOne
    @JoinColumn(name = "import_order_id", nullable = false)
    private ImportOrderEntity importOrder;

    @ManyToOne
    @JoinColumn(name = "product_size_color_id", nullable = false)
    private ProductSizeColorEntity productSizeColor;

    @Column(name = "quantity")
    private int quantity;
    @Column(name = "price")
    private int price;


    // Constructor, Getters, Setters
    public ImportOrderProductEntity() {
    }

    public ImportOrderProductEntity(ImportOrderEntity importOrder, ProductSizeColorEntity product, int quantity, int price) {
        this.importOrder = importOrder;
        this.productSizeColor = product;
        this.quantity = quantity;
        this.price = price;
    }

    public ProductSizeColorEntity getProductSizeColor() {
        return productSizeColor;
    }

    public void setProductSizeColor(ProductSizeColorEntity productSizeColor) {
        this.productSizeColor = productSizeColor;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public ImportOrderEntity getImportOrder() {
        return importOrder;
    }

    public void setImportOrder(ImportOrderEntity importOrder) {
        this.importOrder = importOrder;
    }

    public ProductSizeColorEntity getProduct() {
        return productSizeColor;
    }

    public void setProduct(ProductSizeColorEntity product) {
        this.productSizeColor = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}

