
package com.example.cdwebbackend.entity;

import jakarta.persistence.*;
        import java.util.Date;

@Entity
@Table(name = "import_order")
public class ImportOrderEntity extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "import_price")
    private int importPrice;

    @ManyToOne
    @JoinColumn(name = "username_import", nullable = false)
    private UserEntity user; // Liên kết với người nhập hàng (UserEntity)

    public ImportOrderEntity() {
    }

    public ImportOrderEntity(ProductEntity product, int quantity, int importPrice, UserEntity user) {
        this.product = product;
        this.quantity = quantity;
        this.importPrice = importPrice;
        this.user = user;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
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

    public int getImportPrice() {
        return importPrice;
    }

    public void setImportPrice(int importPrice) {
        this.importPrice = importPrice;
    }

}
