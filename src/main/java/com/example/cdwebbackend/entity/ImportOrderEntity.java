
package com.example.cdwebbackend.entity;

import jakarta.persistence.*;
        import java.util.Date;
import java.util.List;

@Entity
@Table(name = "import_order")
public class ImportOrderEntity extends BaseEntity {

    @OneToMany(mappedBy = "importOrder", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ImportOrderProductEntity> importOrderProducts; // Danh sách các sản phẩm và số lượng

    @Column(name = "import_price")
    private int importPrice;

    @ManyToOne
    @JoinColumn(name = "username_import", nullable = false)
    private UserEntity user;

    // Constructor, Getters, Setters
    public ImportOrderEntity() {
    }

    public ImportOrderEntity(List<ImportOrderProductEntity> importOrderProducts, int importPrice, UserEntity user) {
        this.importOrderProducts = importOrderProducts;
        this.importPrice = importPrice;
        this.user = user;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public List<ImportOrderProductEntity> getImportOrderProducts() {
        return importOrderProducts;
    }

    public void setImportOrderProducts(List<ImportOrderProductEntity> importOrderProducts) {
        this.importOrderProducts = importOrderProducts;
    }

    public int getImportPrice() {
        return importPrice;
    }

    public void setImportPrice(int importPrice) {
        this.importPrice = importPrice;
    }
}
