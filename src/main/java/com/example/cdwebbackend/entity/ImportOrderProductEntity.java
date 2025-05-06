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
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;

    @Column(name = "quantity")
    private int quantity;
    @Column(name = "price")
    private int price;


    // Constructor, Getters, Setters
    public ImportOrderProductEntity() {
    }

    public ImportOrderProductEntity(ImportOrderEntity importOrder, ProductEntity product, int quantity, int price) {
        this.importOrder = importOrder;
        this.product = product;
        this.quantity = quantity;
        this.price = price;
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

    public ProductEntity getProduct() {
        return product;
    }

    public void setProduct(ProductEntity product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}

