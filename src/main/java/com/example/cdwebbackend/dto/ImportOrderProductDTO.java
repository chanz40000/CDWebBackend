package com.example.cdwebbackend.dto;

public class ImportOrderProductDTO extends AbstractDTO<ImportOrderProductDTO>{

        private Long importOrderId;  // ID của ImportOrderEntity
        private Long product_size_colorId;     //
        private int quantity;       // Số lượng của sản phẩm
        private int price;

    // Constructor
        public ImportOrderProductDTO() {
        }

    public ImportOrderProductDTO(Long importOrderId, Long product_size_colorId, int quantity, int price) {
        this.importOrderId = importOrderId;
        this.product_size_colorId = product_size_colorId;
        this.quantity = quantity;
        this.price = price;
    }

    public Long getProduct_size_colorId() {
        return product_size_colorId;
    }

    public void setProduct_size_colorId(Long product_size_colorId) {
        this.product_size_colorId = product_size_colorId;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
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
