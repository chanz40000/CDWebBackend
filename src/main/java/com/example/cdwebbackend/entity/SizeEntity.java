package com.example.cdwebbackend.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name="size")
public class SizeEntity extends BaseEntity{
    @Column(name = "name")
    private String size;

    @OneToMany(mappedBy = "size")
    List<ProductSizeColorEntity>productSizeColorEntities;

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public List<ProductSizeColorEntity> getProductSizeColorEntities() {
        return productSizeColorEntities;
    }

    public void setProductSizeColorEntities(List<ProductSizeColorEntity> productSizeColorEntities) {
        this.productSizeColorEntities = productSizeColorEntities;
    }
}
