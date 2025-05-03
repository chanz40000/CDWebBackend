package com.example.cdwebbackend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CartItemDTO extends AbstractDTO<CartItemDTO>{
    @JsonProperty("product_id")
    private long productId;

    @JsonProperty("product_size_color_id")
    private long productSizeColorId;

    @JsonProperty("quantity")
    private int quantity;

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public long getProductSizeColorId() {
        return productSizeColorId;
    }

    public void setProductSizeColorId(long productSizeColorId) {
        this.productSizeColorId = productSizeColorId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
