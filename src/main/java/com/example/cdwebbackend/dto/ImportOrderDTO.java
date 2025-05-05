package com.example.cdwebbackend.dto;
import java.util.Date;
import java.util.List;

public class ImportOrderDTO {

    private List<ImportOrderProductDTO> products; // Danh sách sản phẩm và số lượng

    private int importPrice;
    private String username;
    private Long id;

    // Getter và Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // Getters and Setters
    public List<ImportOrderProductDTO> getProducts() {
        return products;
    }

    public void setProducts(List<ImportOrderProductDTO> products) {
        this.products = products;
    }

    public int getImportPrice() {
        return importPrice;
    }

    public void setImportPrice(int importPrice) {
        this.importPrice = importPrice;
    }

    public String getUsername() {
        return username;
    }

    public void setUser(String user) {
        this.username = user;
    }

    // DTO cho sản phẩm và số lượng

}
