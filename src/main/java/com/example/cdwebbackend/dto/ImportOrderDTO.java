package com.example.cdwebbackend.dto;

import com.example.cdwebbackend.entity.BaseEntity;
import com.example.cdwebbackend.entity.UserEntity;

import java.util.Date;

public class ImportOrderDTO {
    public class ImportOrderEntity extends BaseEntity {
        private ProductDTO product;

        private int quantity;

        private int importPrice;

        private UserEntity user; // Liên kết với người nhập hàng (UserEntity)

        public ImportOrderEntity() {
        }

        public ImportOrderEntity(ProductDTO product, int quantity, int importPrice, UserEntity user) {
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

        public ProductDTO getProduct() {
            return product;
        }

        public void setProduct(ProductDTO product) {
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


}
