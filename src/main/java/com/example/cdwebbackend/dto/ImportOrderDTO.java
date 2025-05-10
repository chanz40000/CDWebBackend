package com.example.cdwebbackend.dto;
import java.util.Date;
import java.util.List;

public class ImportOrderDTO {
    private Date createdDate;
    private Date modifiedDate;
    private String modifiedBy;

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

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // DTO cho sản phẩm và số lượng


}
