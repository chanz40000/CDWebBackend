package com.example.cdwebbackend.dto;

public class ImportOrderProductDTO {

        private Long importOrderId;  // ID của ImportOrderEntity
        private Long product_size_colorId;     // ID của ProductEntity
        private int quantity;       // Số lượng của sản phẩm

        // Constructor
        public ImportOrderProductDTO() {
        }

        public ImportOrderProductDTO(Long importOrderId, Long productId, int quantity) {
            this.importOrderId = importOrderId;
            this.product_size_colorId = productId;
            this.quantity = quantity;
        }

        // Getters and Setters
        public Long getImportOrderId() {
            return importOrderId;
        }

        public void setImportOrderId(Long importOrderId) {
            this.importOrderId = importOrderId;
        }

        public Long getProductId() {
            return product_size_colorId;
        }

        public void setProductId(Long productId) {
            this.product_size_colorId = productId;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }


}
