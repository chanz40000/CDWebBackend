package com.example.cdwebbackend.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name="size")
public class SizeEntity extends BaseEntity{
    @Column(name = "name")
    private String size;

    @ManyToMany(mappedBy = "sizes")
    List<ProductEntity>products;

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public List<ProductEntity> getProducts() {
        return products;
    }

    public void setProducts(List<ProductEntity> products) {
        this.products = products;
    }
}
